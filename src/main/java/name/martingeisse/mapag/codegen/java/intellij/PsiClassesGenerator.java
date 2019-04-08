package name.martingeisse.mapag.codegen.java.intellij;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.codegen.java.JavaPropertyNames;
import name.martingeisse.mapag.codegen.old.InternalCodeGenerationParameters;
import name.martingeisse.mapag.grammar.canonical.*;
import name.martingeisse.mapag.util.Comparators;
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
 */
public class PsiClassesGenerator {

	public static final String CLASSES_SUPPORT_PSI_NAMED_ELEMENT = "psi.supports.psiNamedElement";
	public static final String CLASSES_SUPPORT_PSI_NAME_IDENTIFIER_OWNER = "psi.supports.psiNameIdentifierOwner";
	public static final String CLASSES_SUPPORT_GET_REFERENCE = "psi.supports.getReference";
	public static final String CLASSES_SUPPORT_SAFE_DELETE = "psi.supports.safeDelete";
	public static final String PSI_UTIL_CLASS_PROPERTY = "psi.utilClass";

	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private final InternalCodeGenerationParameters codeGenerationContext;
	private ImmutableList<String> classesSupportPsiNamedElement;
	private ImmutableList<String> classesSupportPsiNameIdentifierOwner;
	private ImmutableList<String> classesSupportGetReference;
	private ImmutableList<String> classesSupportSafeDelete;

	public PsiClassesGenerator(InternalCodeGenerationParameters codeGenerationParameters) {
		this.grammar = codeGenerationParameters.getGrammarInfo().getGrammar();
		this.configuration = codeGenerationParameters.getConfiguration();
		this.outputFileFactory = codeGenerationParameters.getOutputFileFactory();
		this.codeGenerationContext = codeGenerationParameters;
	}

	public void generate() throws ConfigurationException, IOException {
		classesSupportPsiNamedElement = configuration.getStringList(CLASSES_SUPPORT_PSI_NAMED_ELEMENT);
		classesSupportPsiNameIdentifierOwner = configuration.getStringList(CLASSES_SUPPORT_PSI_NAME_IDENTIFIER_OWNER);
		classesSupportGetReference = configuration.getStringList(CLASSES_SUPPORT_GET_REFERENCE);
		classesSupportSafeDelete = configuration.getStringList(CLASSES_SUPPORT_SAFE_DELETE);
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			handleNonterminal(nonterminalDefinition);
		}
	}

	private void handleNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		if (nonterminalDefinition.getPsiStyle() instanceof PsiStyle.Normal) {
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
	}

	private void generateSingleAlternativeClass(NonterminalDefinition nonterminalDefinition, Alternative alternative) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.getAlternativeTypeIdentifier(nonterminalDefinition, alternative) + "Impl";
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = false;
		classGenerator.alternative = alternative;
		classGenerator.generate();
	}

	private void generateMultiAlternativeBaseClass(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.getNonterminalTypeIdentifier(nonterminalDefinition) + "Impl";
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = true;
		classGenerator.alternative = null;
		classGenerator.generate();
	}

	private void generateMultiAlternativeCaseClass(NonterminalDefinition nonterminalDefinition, Alternative alternative) throws ConfigurationException, IOException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = IdentifierUtil.getAlternativeTypeIdentifier(nonterminalDefinition, alternative) + "Impl";
		classGenerator.superclass = IdentifierUtil.getNonterminalTypeIdentifier(nonterminalDefinition) + "Impl";
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

			// determine full package name
			String basePackageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE);
			String cmPackageName = basePackageName + ".cm";
			String packageName = cmPackageName + ".impl";

			VelocityContext context = new VelocityContext();
			context.put("basePackageName", basePackageName);
			context.put("cmPackageName", cmPackageName);
			context.put("packageName", packageName);
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
						nodeGetter.nodeType = TypeSelectionUtil.getEffectiveTypeForSymbol(TypeSelectionUtil.Usage.CM, grammar, element.getSymbol());
						nodeGetter.getterName = "get" + StringUtils.capitalize(expressionName);
						nodeGetters.add(nodeGetter);
					}
					childIndex++;
				}
			}
			context.put("nodeGetters", nodeGetters);

			List<String> interfaces = new ArrayList<>();
			boolean customNameImplementation = false;
			boolean customNameIdentifierImplementation = false;

			if (classesSupportPsiNamedElement.contains(className)) {
				interfaces.add("PsiNamedElement");
				if (!isAbstract) {
					customNameImplementation = true;
				}
			} else if (classesSupportPsiNamedElement.contains(superclass)) {
				if (!isAbstract) {
					customNameImplementation = true;
				}
			}

			if (classesSupportPsiNameIdentifierOwner.contains(className)) {
				interfaces.add("PsiNameIdentifierOwner");
				if (!isAbstract) {
					customNameIdentifierImplementation = true;
				}
			} else if (classesSupportPsiNameIdentifierOwner.contains(superclass)) {
				if (!isAbstract) {
					customNameIdentifierImplementation = true;
				}
			}

			String extraInterfacesKey = "psi.implements." + className;
			String extraInterfacesText = configuration.getOptional(extraInterfacesKey);
			if (extraInterfacesText != null) {
				for (String extraInterfaceText : StringUtils.split(extraInterfacesText, ',')) {
					interfaces.add(extraInterfaceText.trim());
				}
			}

			if (customNameImplementation || customNameIdentifierImplementation) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
			}
			if (interfaces.isEmpty()) {
				context.put("interfaces", "");
			} else {
				context.put("interfaces", "implements " + StringUtils.join(interfaces, ','));
			}
			context.put("customNameImplementation", customNameImplementation);
			context.put("customNameIdentifierImplementation", customNameIdentifierImplementation);

			if (!isAbstract && (classesSupportGetReference.contains(className) || classesSupportGetReference.contains(superclass))) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
				context.put("supportsGetReference", true);
			} else {
				context.put("supportsGetReference", false);
			}

			context.put("safeDeleteBase", classesSupportSafeDelete.contains(className));
			if (classesSupportSafeDelete.contains(className) || classesSupportSafeDelete.contains(superclass)) {
				context.put("psiUtilClass", configuration.getRequired(PSI_UTIL_CLASS_PROPERTY));
				context.put("safeDeleteImplementation", !isAbstract);
			} else {
				context.put("safeDeleteImplementation", false);
			}

			try (OutputStream outputStream = outputFileFactory.createSourceFile(packageName, className)) {
				try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
					MapagVelocityEngine.engine.getTemplate("templates/intellij/PsiClass.vm").merge(context, outputStreamWriter);
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

}
