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
public class OldCodeGenerationDriver {

	public static void generate(InternalCodeGenerationParameters parameters) throws ConfigurationException, IOException {

		final GrammarInfo grammarInfo = parameters.getGrammarInfo();
		final StateMachine stateMachine = parameters.getStateMachine();
		final Configuration configuration = parameters.getConfiguration();
		final OutputFileFactory outputFileFactory = parameters.getOutputFileFactory();

		if (!parameters.isIntellij()) {
			new FrameworkClassGenerator(configuration, outputFileFactory).generate();
		}
		if (configuration.getRequired("symbolHolder.generate").equals("true")) {
			new SymbolHolderClassGenerator(grammarInfo, configuration, outputFileFactory, parameters).generate();
		}
		if (configuration.getRequired("psi.generate").equals("true")) {
			new PsiClassesGenerator(parameters).generate();
			new PsiFactoryGenerator(grammarInfo, configuration, outputFileFactory, parameters).generate();
		}
		new ParserClassGenerator(parameters).generate();
	}

}
