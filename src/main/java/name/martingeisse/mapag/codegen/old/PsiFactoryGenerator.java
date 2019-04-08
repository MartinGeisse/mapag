package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.MapagVelocityEngine;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.codegen.java.JavaPropertyNames;
import name.martingeisse.mapag.codegen.java.intellij.SymbolHolderClassGenerator;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PsiFactoryGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "psi.package";

	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private final InternalCodeGenerationParameters codeGenerationContext;

	public PsiFactoryGenerator(GrammarInfo grammarInfo, Configuration configuration, OutputFileFactory outputFileFactory, InternalCodeGenerationParameters codeGenerationContext) {
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
		this.codeGenerationContext = codeGenerationContext;
	}

	public void generate() throws ConfigurationException, IOException {
		String basePackageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE);

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("intellij", codeGenerationContext.isIntellij());
		if (!basePackageName.equals(configuration.getRequired(PACKAGE_NAME_PROPERTY))) {
			String symbolHolderPackage = basePackageName;
			String symbolHolderClass = configuration.getRequired(SymbolHolderClassGenerator.CLASS_NAME_PROPERTY);
			context.put("symbolHolderImport", "import " + symbolHolderPackage + '.' + symbolHolderClass + ';');
		} else {
			context.put("symbolHolderImport", "");
		}
		String symbolHolder = configuration.getRequired(SymbolHolderClassGenerator.CLASS_NAME_PROPERTY);
		context.put("symbolHolder", symbolHolder);

		List<FactoryCaseEntry> cases = new ArrayList<>();
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			if (nonterminalDefinition.getPsiStyle().isDistinctSymbolPerAlternative()) {
				for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
					FactoryCaseEntry caseEntry = new FactoryCaseEntry();
					caseEntry.elementType = IdentifierUtil.getAlternativeVariableIdentifier(nonterminalDefinition, alternative);
					caseEntry.psiClass = TypeSelectionUtil.getEffectiveTypeForAlternative(grammar, nonterminalDefinition, alternative);
					caseEntry.additionalConstructorArguments = "";
					cases.add(caseEntry);
				}
			} else {
				FactoryCaseEntry caseEntry = new FactoryCaseEntry();
				caseEntry.elementType = IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
				caseEntry.psiClass = TypeSelectionUtil.getEffectiveTypeForSymbol(grammar, nonterminalDefinition);
				caseEntry.additionalConstructorArguments = TypeSelectionUtil.getAdditionalConstructorArgumentsForSymbol(grammar, nonterminalDefinition, symbolHolder);
				cases.add(caseEntry);
			}
		}
		context.put("cases", cases);

		try (OutputStream outputStream = outputFileFactory.createSourceFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), "PsiFactory")) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("templates/PsiFactory.vm").merge(context, outputStreamWriter);
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

}
