package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PsiClassesGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "psi.package";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;
	private final PsiTypeMapper psiTypeMapper;
	private int alternativeCounter;

	public PsiClassesGenerator(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
		this.psiTypeMapper = new PsiTypeMapper(grammarInfo, configuration);
	}

	public void generate() throws ConfigurationException {
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			handleNonterminal(nonterminalDefinition);
		}
		generateFactoryClass();
	}

	private void handleNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException {
		switch (nonterminalDefinition.getAnnotation().getPsiStyle()) {

			case NORMAL:
			case OPTIONAL:
				// TODO optionals
				handleNormalStyledNonterminal(nonterminalDefinition);
				break;

			case ZERO_OR_MORE:
				handleRepetitionStyledNonterminal(nonterminalDefinition, true);
				break;

			case ONE_OR_MORE:
				handleRepetitionStyledNonterminal(nonterminalDefinition, false);
				break;

			default:
				throw new RuntimeException("unknown PSI style: " + nonterminalDefinition.getAnnotation().getPsiStyle());

		}
	}

	private void handleNormalStyledNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException {
		if (nonterminalDefinition.getAlternatives().size() == 1) {
			generateSingleAlternativeClass(nonterminalDefinition.getName(), nonterminalDefinition.getAlternatives().get(0));
		} else {
			generateMultiAlternativeBaseClass(nonterminalDefinition.getName());
			List<Alternative> sortedAlternatives = new ArrayList<>(nonterminalDefinition.getAlternatives());
			sortedAlternatives.sort(Comparators.alternativeComparator);
			alternativeCounter = 0;
			for (Alternative alternative : sortedAlternatives) {
				generateMultiAlternativeCaseClass(nonterminalDefinition.getName(), alternative);
				alternativeCounter++;
			}
		}
	}

	private void generateSingleAlternativeClass(String nonterminalName, Alternative alternative) throws ConfigurationException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = psiTypeMapper.getEffectiveTypeForNonterminal(nonterminalName);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private void generateMultiAlternativeBaseClass(String nonterminalName) throws ConfigurationException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = psiTypeMapper.getEffectiveTypeForNonterminal(nonterminalName);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = true;
		classGenerator.alternative = null;
		classGenerator.generate();
	}

	private void generateMultiAlternativeCaseClass(String nonterminalName, Alternative alternative) throws ConfigurationException {
		String alternativeName = (alternative.getAnnotation().getAlternativeName() == null ? Integer.toString(alternativeCounter) : alternative.getAnnotation().getAlternativeName());
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = psiTypeMapper.toIdentifier(nonterminalName + '/' + alternativeName, true);
		classGenerator.superclass = psiTypeMapper.toIdentifier(nonterminalName, true);
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private void handleRepetitionStyledNonterminal(NonterminalDefinition nonterminalDefinition, boolean zeroBased) throws ConfigurationException {

		// verify the nonterminal's structure and determine its element symbol and Java type
		String nonterminalName = nonterminalDefinition.getName();
		if (nonterminalDefinition.getAlternatives().size() != 2) {
			throw new RuntimeException("repetition-styled nonterminal " + nonterminalName + " has " +
				nonterminalDefinition.getAlternatives().size() + " alternatives, expected 2");
		}
		Alternative baseCaseAlternative, repetitionCaseAlternative;
		if (nonterminalDefinition.getAlternatives().get(0).getExpansion().size() == 2) {
			repetitionCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			baseCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else if (nonterminalDefinition.getAlternatives().get(1).getExpansion().size() == 2) {
			baseCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			repetitionCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else {
			throw new RuntimeException("could not find alternative with expansion length 2 as repetition case for repetition-styled nonterminal " + nonterminalName);
		}
		if (baseCaseAlternative.getExpansion().size() != (zeroBased ? 0 : 1)) {
			throw new RuntimeException("could not recognize base case for repetition-styled nonterminal " + nonterminalName);
		}
		if (!repetitionCaseAlternative.getExpansion().get(0).equals(nonterminalName)) {
			throw new RuntimeException("could not find left-recursion for repetition-styled nonterminal " + nonterminalName);
		}
		String elementSymbol = repetitionCaseAlternative.getExpansion().get(1);
		if (!zeroBased && !baseCaseAlternative.getExpansion().get(0).equals(elementSymbol)) {
			throw new RuntimeException("base-case uses different element symbol than repetition case for nonterminal " + elementSymbol);
		}
		String elementType = psiTypeMapper.getEffectiveTypeForSymbol(elementSymbol);

		// generate abstract class
		String abstractClassName = psiTypeMapper.getEffectiveTypeForNonterminal(nonterminalName);
		{
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = abstractClassName;
			classGenerator.superclass = "ASTWrapperPsiElement";
			classGenerator.isAbstract = true;
			classGenerator.alternative = null;
			classGenerator.isRepetitionAbstract = true;
			classGenerator.elementType = elementType;
			classGenerator.isZeroBasedRepetition = zeroBased;
			classGenerator.generate();
		}

		// generate base case class
		{
			// note: this fallback shouldn't be necessary since the alternatives of a repetition should always be named
			String alternativeName = (baseCaseAlternative.getAnnotation().getAlternativeName() == null ? "baseCase" : baseCaseAlternative.getAnnotation().getAlternativeName());
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = psiTypeMapper.toIdentifier(nonterminalName + '/' + alternativeName, true);
			classGenerator.superclass = abstractClassName;
			classGenerator.isAbstract = false;
			classGenerator.alternative = baseCaseAlternative;
			classGenerator.isRepetitionBaseCase = true;
			classGenerator.elementType = elementType;
			classGenerator.isZeroBasedRepetition = zeroBased;
			classGenerator.generate();
		}

		// generate repetition case class
		{
			// note: this fallback shouldn't be necessary since the alternatives of a repetition should always be named
			String alternativeName = (repetitionCaseAlternative.getAnnotation().getAlternativeName() == null ? "repetitionCase" : repetitionCaseAlternative.getAnnotation().getAlternativeName());
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = psiTypeMapper.toIdentifier(nonterminalName + '/' + alternativeName, true);
			classGenerator.superclass = abstractClassName;
			classGenerator.isAbstract = false;
			classGenerator.alternative = repetitionCaseAlternative;
			classGenerator.isRepetitionNextCase = true;
			classGenerator.elementType = elementType;
			classGenerator.isZeroBasedRepetition = zeroBased;
			classGenerator.generate();
		}

	}

	private class PsiClassGenerator {

		String className;
		String superclass;
		boolean isAbstract;
		Alternative alternative;

		boolean isRepetitionAbstract;
		boolean isRepetitionBaseCase;
		boolean isRepetitionNextCase;
		String elementType;
		boolean isZeroBasedRepetition;

		void generate() throws ConfigurationException {

			VelocityContext context = new VelocityContext();
			context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
			context.put("className", className);
			context.put("superclass", superclass);
			context.put("classModifiers", isAbstract ? "abstract" : "final");
			context.put("isRepetitionAbstract", isRepetitionAbstract);
			context.put("isRepetitionBaseCase", isRepetitionBaseCase);
			context.put("isRepetitionNextCase", isRepetitionNextCase);
			context.put("elementType", elementType);
			context.put("isZeroBasedRepetition", isZeroBasedRepetition);

			List<NodeGetter> nodeGetters = new ArrayList<>();
			if (alternative != null && alternative.getAnnotation().getExpressionNames() != null) {
				for (int i = 0; i < alternative.getAnnotation().getExpressionNames().size(); i++) {
					String symbol = alternative.getExpansion().get(i);
					String expressionName = alternative.getAnnotation().getExpressionNames().get(i);
					if (!expressionName.isEmpty()) {
						NodeGetter nodeGetter = new NodeGetter();
						nodeGetter.childIndex = i;
						nodeGetter.nodeType = psiTypeMapper.getEffectiveTypeForSymbol(symbol);
						nodeGetter.getterName = "get" + StringUtils.capitalize(expressionName);
						nodeGetters.add(nodeGetter);
					}
				}
			}
			context.put("nodeGetters", nodeGetters);

			StringWriter sw = new StringWriter();
			MapagVelocityEngine.engine.getTemplate("PsiClass.vm").merge(context, sw);
			System.out.println(sw);

		}

	}

	public class NodeGetter {

		int childIndex;
		String nodeType;
		String getterName;

		public int getChildIndex() {
			return childIndex;
		}

		public String getNodeType() {
			return nodeType;
		}

		public String getGetterName() {
			return getterName;
		}

	}

	private void generateFactoryClass() throws ConfigurationException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));

		StringWriter sw = new StringWriter();
		MapagVelocityEngine.engine.getTemplate("PsiFactory.vm").merge(context, sw);
		System.out.println(sw);

	}

}
