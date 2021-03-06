package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.codegen.java.JavaPropertyNames;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PsiFactoryGenerator {

	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;

	public PsiFactoryGenerator(CodeGenerationParameters parameters) {
		this.grammar = parameters.getGrammarInfo().getGrammar();
		this.configuration = parameters.getConfiguration();
		this.outputFileFactory = parameters.getOutputFileFactory();
	}

	public void generate() throws ConfigurationException, IOException {
		String basePackageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE);
		String cmPackageName = basePackageName + ".cm";
		String packageName = cmPackageName + ".impl";
		String symbolHolder = configuration.getRequired(SymbolHolderClassGenerator.CLASS_NAME_PROPERTY);

		VelocityContext context = new VelocityContext();
		context.put("basePackageName", basePackageName);
		context.put("cmPackageName", cmPackageName);
		context.put("packageName", packageName);
		context.put("symbolHolder", symbolHolder);

		List<FactoryCaseEntry> cases = new ArrayList<>();
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			if (nonterminalDefinition.getPsiStyle().isDistinctSymbolPerAlternative()) {
				for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
					FactoryCaseEntry caseEntry = new FactoryCaseEntry();
					caseEntry.elementType = IdentifierUtil.getAlternativeVariableIdentifier(nonterminalDefinition, alternative);
					caseEntry.psiClass = TypeSelectionUtil.getEffectiveTypeForAlternative(TypeSelectionUtil.Usage.PSI, grammar, nonterminalDefinition, alternative);
					caseEntry.additionalConstructorArguments = "";
					cases.add(caseEntry);
				}
			} else {
				FactoryCaseEntry caseEntry = new FactoryCaseEntry();
				caseEntry.elementType = IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
				caseEntry.psiClass = TypeSelectionUtil.getEffectiveTypeForSymbol(TypeSelectionUtil.Usage.PSI, grammar, nonterminalDefinition);
				caseEntry.additionalConstructorArguments = TypeSelectionUtil.getAdditionalConstructorArgumentsForSymbol(grammar, nonterminalDefinition, symbolHolder);
				cases.add(caseEntry);
			}
		}
		context.put("cases", cases);

		try (OutputStream outputStream = outputFileFactory.createSourceFile(packageName, "PsiFactory")) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("templates/intellij/PsiFactory.vm").merge(context, outputStreamWriter);
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
