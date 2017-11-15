package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PsiClassesGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "psi.package";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private int alternativeCounter;
	private Map<Alternative, String> effectiveAlternativeNames;

	public PsiClassesGenerator(GrammarInfo grammarInfo, Configuration configuration, OutputFileFactory outputFileFactory) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
	}

	public void generate() throws ConfigurationException, IOException {
		effectiveAlternativeNames = new HashMap<>();
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			handleNonterminal(nonterminalDefinition);
		}
		generateFactoryClass();
	}

	private void handleNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		switch (nonterminalDefinition.getAnnotation().getPsiStyle()) {

			case NORMAL:
				handleNormalStyledNonterminal(nonterminalDefinition);
				break;

			case OPTIONAL:
				handleOptionalStyledNonterminal(nonterminalDefinition);
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

	private void handleNormalStyledNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
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

	private void generateSingleAlternativeClass(String nonterminalName, Alternative alternative) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName, true);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private void generateMultiAlternativeBaseClass(String nonterminalName) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName, true);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = true;
		classGenerator.alternative = null;
		classGenerator.generate();
	}

	private void generateMultiAlternativeCaseClass(String nonterminalName, Alternative alternative) throws ConfigurationException, IOException {
		String alternativeName = (alternative.getAnnotation().getAlternativeName() == null ? Integer.toString(alternativeCounter) : alternative.getAnnotation().getAlternativeName());
		effectiveAlternativeNames.put(alternative, alternativeName);
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName + '/' + alternativeName, true);
		classGenerator.superclass = IdentifierUtil.toIdentifier(nonterminalName, true);
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private void handleRepetitionStyledNonterminal(NonterminalDefinition nonterminalDefinition, boolean zeroBased) throws ConfigurationException, IOException {

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
		String operandType = getEffectiveTypeForSymbol(elementSymbol);

		// generate abstract class
		String abstractClassName = IdentifierUtil.toIdentifier(nonterminalName, true);
		{
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = abstractClassName;
			classGenerator.superclass = "ASTWrapperPsiElement";
			classGenerator.isAbstract = true;
			classGenerator.alternative = null;
			classGenerator.isRepetitionAbstract = true;
			classGenerator.operandType = operandType;
			classGenerator.isZeroBasedRepetition = zeroBased;
			classGenerator.generate();
		}

		// generate base case class
		{
			// note: this fallback shouldn't be necessary since the alternatives of a repetition should always be named
			String alternativeName = (baseCaseAlternative.getAnnotation().getAlternativeName() == null ? "baseCase" : baseCaseAlternative.getAnnotation().getAlternativeName());
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName + '/' + alternativeName, true);
			classGenerator.superclass = abstractClassName;
			classGenerator.isAbstract = false;
			classGenerator.alternative = baseCaseAlternative;
			classGenerator.isRepetitionBaseCase = true;
			classGenerator.operandType = operandType;
			classGenerator.isZeroBasedRepetition = zeroBased;
			classGenerator.generate();
		}

		// generate repetition case class
		{
			// note: this fallback shouldn't be necessary since the alternatives of a repetition should always be named
			String alternativeName = (repetitionCaseAlternative.getAnnotation().getAlternativeName() == null ? "repetitionCase" : repetitionCaseAlternative.getAnnotation().getAlternativeName());
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName + '/' + alternativeName, true);
			classGenerator.superclass = abstractClassName;
			classGenerator.isAbstract = false;
			classGenerator.alternative = repetitionCaseAlternative;
			classGenerator.isRepetitionNextCase = true;
			classGenerator.operandType = operandType;
			classGenerator.isZeroBasedRepetition = zeroBased;
			classGenerator.generate();
		}

	}

	private void handleOptionalStyledNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {

		// verify the nonterminal's structure and determine its element symbol and Java type
		String nonterminalName = nonterminalDefinition.getName();
		if (nonterminalDefinition.getAlternatives().size() != 2) {
			throw new RuntimeException("optional-styled nonterminal " + nonterminalName + " has " +
				nonterminalDefinition.getAlternatives().size() + " alternatives, expected 2");
		}
		Alternative absentCaseAlternative, presentCaseAlternative;
		if (nonterminalDefinition.getAlternatives().get(0).getExpansion().size() == 0) {
			absentCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			presentCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else if (nonterminalDefinition.getAlternatives().get(1).getExpansion().size() == 0) {
			presentCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			absentCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else {
			throw new RuntimeException("could not find alternative with expansion length 0 as absent case for optional-styled nonterminal " + nonterminalName);
		}
		if (presentCaseAlternative.getExpansion().size() != 1) {
			throw new RuntimeException("could not recognize present case for optional-styled nonterminal " + nonterminalName);
		}
		String operandSymbol = presentCaseAlternative.getExpansion().get(0);
		String operandType = getEffectiveTypeForSymbol(operandSymbol);
		String operandName = presentCaseAlternative.getAnnotation().getExpressionNames().get(0);
		if (operandName == null) {
			operandName = "it";
		}
		String operandGetterName = "get" + StringUtils.capitalize(operandName);

		// generate abstract class
		String abstractClassName = IdentifierUtil.toIdentifier(nonterminalName, true);
		{
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = abstractClassName;
			classGenerator.superclass = "ASTWrapperPsiElement";
			classGenerator.isAbstract = true;
			classGenerator.alternative = null;
			classGenerator.operandType = operandType;
			classGenerator.isOptionalAbstract = true;
			classGenerator.optionalOperandGetterName = operandGetterName;
			classGenerator.generate();
		}

		// generate "absent" case
		{
			// note: this fallback shouldn't be necessary since the alternatives of a repetition should always be named
			String alternativeName = (absentCaseAlternative.getAnnotation().getAlternativeName() == null ? "baseCase" : absentCaseAlternative.getAnnotation().getAlternativeName());
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName + '/' + alternativeName, true);
			classGenerator.superclass = abstractClassName;
			classGenerator.isAbstract = false;
			classGenerator.alternative = absentCaseAlternative;
			classGenerator.operandType = operandType;
			classGenerator.isOptionalAbsentCase = true;
			classGenerator.optionalOperandGetterName = operandGetterName;
			classGenerator.generate();
		}

		// generate "present" case
		{
			// note: this fallback shouldn't be necessary since the alternatives of a repetition should always be named
			String alternativeName = (presentCaseAlternative.getAnnotation().getAlternativeName() == null ? "repetitionCase" : presentCaseAlternative.getAnnotation().getAlternativeName());
			PsiClassGenerator classGenerator = new PsiClassGenerator();
			classGenerator.className = IdentifierUtil.toIdentifier(nonterminalName + '/' + alternativeName, true);
			classGenerator.superclass = abstractClassName;
			classGenerator.isAbstract = false;
			classGenerator.alternative = presentCaseAlternative;
			classGenerator.operandType = operandType;
			classGenerator.isOptionalPresentCase = true;
			classGenerator.optionalOperandGetterName = operandGetterName;
			classGenerator.generate();
		}

	}
	public String getEffectiveTypeForSymbol(String symbol) {
		if (grammar.getTerminalDefinitions().get(symbol) != null) {
			return "LeafPsiElement";
		} else if (grammar.getNonterminalDefinitions().get(symbol) != null) {
			return IdentifierUtil.toIdentifier(symbol, true);
		} else {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
	}

	private class PsiClassGenerator {

		String className;
		String superclass;
		boolean isAbstract;
		Alternative alternative;

		String operandType;
		boolean isRepetitionAbstract;
		boolean isRepetitionBaseCase;
		boolean isRepetitionNextCase;
		boolean isZeroBasedRepetition;
		boolean isOptionalAbstract;
		boolean isOptionalAbsentCase;
		boolean isOptionalPresentCase;
		String optionalOperandGetterName;


		void generate() throws ConfigurationException, IOException {

			VelocityContext context = new VelocityContext();
			context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
			context.put("className", className);
			context.put("superclass", superclass);
			context.put("classModifiers", isAbstract ? "abstract" : "final");
			context.put("operandType", operandType);
			context.put("isRepetitionAbstract", isRepetitionAbstract);
			context.put("isRepetitionBaseCase", isRepetitionBaseCase);
			context.put("isRepetitionNextCase", isRepetitionNextCase);
			context.put("isZeroBasedRepetition", isZeroBasedRepetition);
			context.put("isOptionalAbstract", isOptionalAbstract);
			context.put("isOptionalAbsentCase", isOptionalAbsentCase);
			context.put("isOptionalPresentCase", isOptionalPresentCase);
			context.put("optionalOperandGetterName", optionalOperandGetterName);

			List<NodeGetter> nodeGetters = new ArrayList<>();
			if (alternative != null && alternative.getAnnotation().getExpressionNames() != null) {
				for (int i = 0; i < alternative.getAnnotation().getExpressionNames().size(); i++) {
					String symbol = alternative.getExpansion().get(i);
					String expressionName = alternative.getAnnotation().getExpressionNames().get(i);
					if (!expressionName.isEmpty()) {
						NodeGetter nodeGetter = new NodeGetter();
						nodeGetter.childIndex = i;
						nodeGetter.nodeType = getEffectiveTypeForSymbol(symbol);
						nodeGetter.getterName = "get" + StringUtils.capitalize(expressionName);
						nodeGetters.add(nodeGetter);
					}
				}
			}
			context.put("nodeGetters", nodeGetters);

			try (OutputStream outputStream = outputFileFactory.createOutputFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), className)) {
				try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
					MapagVelocityEngine.engine.getTemplate("PsiClass.vm").merge(context, outputStreamWriter);
				}
			}

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

	private void generateFactoryClass() throws ConfigurationException, IOException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		if (!configuration.getRequired(SymbolHolderClassGenerator.PACKAGE_NAME_PROPERTY).equals(configuration.getRequired(PACKAGE_NAME_PROPERTY))) {
			String symbolHolderPackage = configuration.getRequired(SymbolHolderClassGenerator.PACKAGE_NAME_PROPERTY);
			String symbolHolderClass = configuration.getRequired(SymbolHolderClassGenerator.CLASS_NAME_PROPERTY);
			context.put("symbolHolderImport", "import " + symbolHolderPackage + '.' + symbolHolderClass + ';');
		} else {
			context.put("symbolHolderImport", "");
		}
		context.put("symbolHolder", configuration.getRequired(SymbolHolderClassGenerator.CLASS_NAME_PROPERTY));

		List<FactoryCaseEntry> cases = new ArrayList<>();
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			for (Alternative alternative : nonterminalDefinition.getAlternatives()) {

				String concreteClassName;
				if (nonterminalDefinition.getAlternatives().size() == 1) {
					concreteClassName = IdentifierUtil.toIdentifier(nonterminalDefinition.getName(), true);
				} else {
					concreteClassName = IdentifierUtil.toIdentifier(nonterminalDefinition.getName() + '/' + effectiveAlternativeNames.get(alternative), true);
					generateMultiAlternativeCaseClass(nonterminalDefinition.getName(), alternative);
				}

				FactoryCaseEntry caseEntry = new FactoryCaseEntry();
				caseEntry.elementType = ; // TODO we actually need an element type per alternative, not per nonterminal!
				caseEntry.psiClass = concreteClassName;
				cases.add(caseEntry);

			}
		}
		context.put("cases", cases);

		try (OutputStream outputStream = outputFileFactory.createOutputFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), "PsiFactory")) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("PsiFactory.vm").merge(context, outputStreamWriter);
			}
		}

	}

	public static class FactoryCaseEntry {

		String elementType;
		String psiClass;

		public String getElementType() {
			return elementType;
		}

		public String getPsiClass() {
			return psiClass;
		}

	}

}
