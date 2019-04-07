package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

import java.io.IOException;

/**
 *
 */
public class CodeGenerationDriver {

	private final CodeGenerationParameters parameters;

	public CodeGenerationDriver(CodeGenerationParameters parameters) {
		this.parameters = parameters;
	}

	public void generate() throws ConfigurationException, IOException {

		final GrammarInfo grammarInfo = parameters.getGrammarInfo();
		final StateMachine stateMachine = parameters.getStateMachine();
		final Configuration configuration = parameters.getConfiguration();
		final OutputFileFactory outputFileFactory = parameters.getOutputFileFactory();
		final CodeGenerationContext context = parameters.getContext();

		if (!context.isIntellij()) {
			new FrameworkClassGenerator(configuration, outputFileFactory).generate();
		}
		if (configuration.getRequired("symbolHolder.generate").equals("true")) {
			new SymbolHolderClassGenerator(grammarInfo, configuration, outputFileFactory, context).generate();
		}
		if (configuration.getRequired("psi.generate").equals("true")) {
			new PsiClassesGenerator(parameters).generate();
			new PsiFactoryGenerator(grammarInfo, configuration, outputFileFactory, context).generate();
		}
		new ParserClassGenerator(parameters).generate();
	}

}
