package name.martingeisse.mapag.codegen.psi;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import name.martingeisse.mapag.util.UserMessageException;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Note: A dynamically named PSI element must currently be represented by its own nonterminal, because otherwise
 * we don't have a stable way of naming it in the properties. As long as the handling of dynamically named elements
 * is delegated to a utility class (as it is currently the case), that's okay, because without a nonterminal the
 * naming of the Parameter type of that utility method wouldn't be stable either. The cost of this is that the
 * author has to invent a new nonterminal for a dynamically named element if one doesn't exist yet, so that's
 * acceptable.
 *
 * TODO PsiNameIdentifierOwner
 */
public class PsiClassesGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "psi.package";
	public static final String PARSER_DEFINITION_CLASS_PROPERTY = "context.parserDefinitionClass";
	public static final String DYNAMICALLY_NAMED_CLASSES_PROPERTY = "psi.dynamicallyNamedClasses";
	public static final String REFERENCE_CLASSES_PROPERTY = "psi.referenceClasses";
	public static final String SAFE_DELETABLE_CLASSES_PROPERTY = "psi.safeDeletableClasses";
	public static final String PSI_UTIL_CLASS_PROPERTY = "psi.utilClass";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private ImmutableList<String> dynamicallyNamedClasses;
	private ImmutableList<String> referenceClasses;
	private ImmutableList<String> safeDeletableClasses;

	public PsiClassesGenerator(GrammarInfo grammarInfo, Configuration configuration, OutputFileFactory outputFileFactory) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
	}

	public void generate() throws ConfigurationException, IOException {
		dynamicallyNamedClasses = configuration.getStringList(DYNAMICALLY_NAMED_CLASSES_PROPERTY);
		referenceClasses = configuration.getStringList(REFERENCE_CLASSES_PROPERTY);
		safeDeletableClasses = configuration.getStringList(SAFE_DELETABLE_CLASSES_PROPERTY);
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			handleNonterminal(nonterminalDefinition);
		}
		generateOptionalClass();
		generateListNodeClass();
		generateFactoryClass();
		generateInternalPsiUtilClass();
	}

	private void handleNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		switch (nonterminalDefinition.getPsiStyle()) {

			case NORMAL:
				handleNormalStyledNonterminal(nonterminalDefinition);
				break;

			case OPTIONAL:
				// nothing to generate -- we use the generic "Optional" class instead
				break;

			case ZERO_OR_MORE:
				// nothing to generate -- we use the generic "ListNode" class instead
				break;

			case ONE_OR_MORE:
				// nothing to generate -- we use the generic "ListNode" class instead
				break;

			case SEPARATED_ONE_OR_MORE:
				throw new UnsupportedOperationException("TODO");

			case OPTIONAL_SEPARATED_ONE_OR_MORE:
				throw new UnsupportedOperationException("TODO");

			default:
				throw new RuntimeException("unknown PSI style: " + nonterminalDefinition.getPsiStyle());

		}
	}

	private void handleNormalStyledNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		if (nonterminalDefinition.getAlternatives().size() == 1) {
			generateSingleAlternativeClass(nonterminalDefinition, nonterminalDefinition.getAlternatives().get(0));
		} else {
			generateMultiAlternativeBaseClass(nonterminalDefinition);
			List<Alternative> sortedAlternatives = new ArrayList<>(nonterminalDefinition.getAlternatives());
			sortedAlternatives.sort(Comparators.alternativeComparator);
			for (Alternative alternative : sortedAlternatives) {
				generateMultiAlternativeCaseClass(nonterminalDefinition, alternative);
			}
		}
	}

	private void generateSingleAlternativeClass(NonterminalDefinition nonterminalDefinition, Alternative alternative) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.getAlternativeClassIdentifier(nonterminalDefinition, alternative);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private void generateMultiAlternativeBaseClass(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.getNonterminalClassIdentifier(nonterminalDefinition);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = true;
		classGenerator.alternative = null;
		classGenerator.generate();
	}

	private void generateMultiAlternativeCaseClass(NonterminalDefinition nonterminalDefinition, Alternative alternative) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.getAlternativeClassIdentifier(nonterminalDefinition, alternative);
		classGenerator.superclass = IdentifierUtil.getNonterminalClassIdentifier(nonterminalDefinition);
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private class PsiClassGenerator {

		String className;
		String superclass;
		boolean isAbstract;
		Alternative alternative;

		void generate() throws ConfigurationException, IOException {

			VelocityContext context = new VelocityContext();
			context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
			context.put("className", className);
			context.put("superclass", superclass);
			context.put("classModifiers", isAbstract ? "abstract" : "final");

			List<NodeGetter> nodeGetters = new ArrayList<>();
			if (alternative != null) {
				int childIndex = 0;
				for (ExpansionElement element : alternative.getExpansion().getElements()) {
					String expressionName = element.getExpressionName();
					if (expressionName != null) {
						NodeGetter nodeGetter = new NodeGetter();
						nodeGetter.childIndex = childIndex;
						nodeGetter.nodeType = TypeSelectionUtil.getEffectiveTypeForSymbol(grammar, element.getSymbol());
						nodeGetter.getterName = "get" + StringUtils.capitalize(expressionName);
						nodeGetters.add(nodeGetter);
					}
					childIndex++;
				}
			}
			context.put("nodeGetters", nodeGetters);

			if (dynamicallyNamedClasses.contains(className)) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
				if (isAbstract) {

					// this is a dynamically named multi-alternative nonterminal class
					context.put("dynamicallyNamedAbstract", true);
					context.put("dynamicallyNamedImplementation", false);

				} else {

					// this is either a dynamically named single-alternative nonterminal class or a
					// directly dynamically named alternative class from a multialternative nonterminal (the latter
					// is useful if only this alternative is dynamically named, and the others aren't)
					context.put("dynamicallyNamedAbstract", false);
					context.put("dynamicallyNamedImplementation", true);

				}
			} else if (dynamicallyNamedClasses.contains(superclass)) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
				if (isAbstract) {

					throw new UserMessageException("Found an abstract PSI class whose base class is " +
						"dynamically named: " + className + " -- this doesn't make sense");

				} else {

					// this is an alternative of a dynamically named nonterminal, so the naming is inherited
					context.put("dynamicallyNamedAbstract", false);
					context.put("dynamicallyNamedImplementation", true);

				}
			} else {

				// this class is not dynamically named
				context.put("dynamicallyNamedAbstract", false);
				context.put("dynamicallyNamedImplementation", false);

			}

			if (!isAbstract && (referenceClasses.contains(className) || referenceClasses.contains(superclass))) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
				context.put("isReference", true);
			} else {
				context.put("isReference", false);
			}

			if (safeDeletableClasses.contains(className) || safeDeletableClasses.contains(superclass)) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
				context.put("isSafeDeletableAbstract", isAbstract);
				context.put("isSafeDeletableImplementation", !isAbstract);
			} else {
				context.put("isSafeDeletableAbstract", false);
				context.put("isSafeDeletableImplementation", false);
			}

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

	private void generateOptionalClass() throws ConfigurationException, IOException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));

		try (OutputStream outputStream = outputFileFactory.createOutputFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), "Optional")) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("Optional.vm").merge(context, outputStreamWriter);
			}
		}

	}

	private void generateListNodeClass() throws ConfigurationException, IOException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));

		try (OutputStream outputStream = outputFileFactory.createOutputFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), "ListNode")) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("ListNode.vm").merge(context, outputStreamWriter);
			}
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
			if (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.OPTIONAL) {
				FactoryCaseEntry caseEntry = new FactoryCaseEntry();
				caseEntry.elementType = IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
				caseEntry.psiClass = TypeSelectionUtil.getEffectiveTypeForSymbol(grammar, nonterminalDefinition);
				cases.add(caseEntry);

				// TODO
//			} else if (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.ZERO_OR_MORE || nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.ONE_OR_MORE) {
//				FactoryCaseEntry caseEntry = new FactoryCaseEntry();
//				String listElementSymbol = "...";
//				String listElementType = getEffectiveTypeForSymbol(listElementSymbol);
//				caseEntry.elementType = IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
//				caseEntry.psiClass = "ListNode<" + listElementType + ">";
//				caseEntry.additionalConstructorArguments = "TokenSet., " + listElementType + ".class";
//				cases.add(caseEntry);

			} else {
				for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
					FactoryCaseEntry caseEntry = new FactoryCaseEntry();
					caseEntry.elementType = IdentifierUtil.getAlternativeVariableIdentifier(nonterminalDefinition, alternative);
					caseEntry.psiClass = IdentifierUtil.getAlternativeClassIdentifier(nonterminalDefinition, alternative);
					cases.add(caseEntry);
				}
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
		String additionalConstructorArguments;

		public String getElementType() {
			return elementType;
		}

		public String getPsiClass() {
			return psiClass;
		}

		public String getAdditionalConstructorArguments() {
			return additionalConstructorArguments;
		}
	}

	private void generateInternalPsiUtilClass() throws ConfigurationException, IOException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("parserDefinitionClass", configuration.getRequired(PARSER_DEFINITION_CLASS_PROPERTY));

		try (OutputStream outputStream = outputFileFactory.createOutputFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), "InternalPsiUtil")) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("InternalPsiUtil.vm").merge(context, outputStreamWriter);
			}
		}

	}
}
