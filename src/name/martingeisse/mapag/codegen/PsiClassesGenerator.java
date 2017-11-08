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
		generateFactoryClass();
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

	private class PsiClassGenerator {

		String className;
		String superclass;
		boolean isAbstract;
		Alternative alternative;

		void generate() throws ConfigurationException {

			VelocityContext context = new VelocityContext();
			context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
			context.put("className", className);
			context.put("superclass", superclass);
			context.put("classModifiers", isAbstract ? "abstract" : "final");

			List<NodeGetter> nodeGetters = new ArrayList<>();
			if (alternative != null && alternative.getAnnotation().getExpressionNames() != null) {
				for (int i = 0; i < alternative.getAnnotation().getExpressionNames().size(); i++) {
					String symbol = alternative.getExpansion().get(i);
					String expressionName = alternative.getAnnotation().getExpressionNames().get(i);
					if (!expressionName.isEmpty()) {
						NodeGetter nodeGetter = new NodeGetter();
						nodeGetter.childIndex = i;
						if (grammar.getTerminalDefinitions().get(symbol) != null) {
							nodeGetter.nodeType = "LeafPsiElement";
						} else if (grammar.getNonterminalDefinitions().get(symbol) != null) {
							nodeGetter.nodeType = psiTypeMapper.getEffectiveTypeForNonterminal(symbol);
						} else {
							throw new RuntimeException("unknown symbol: " + symbol);
						}
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
