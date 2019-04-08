package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.java.intellij.SymbolHolderClassGenerator;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;

import java.io.IOException;

/**
 *
 */
public class OldCodeGenerationDriver {

	public static void generate(InternalCodeGenerationParameters parameters) throws ConfigurationException, IOException {

		final GrammarInfo grammarInfo = parameters.getGrammarInfo();
		final Configuration configuration = parameters.getConfiguration();
		final OutputFileFactory outputFileFactory = parameters.getOutputFileFactory();

		if (configuration.getRequired("psi.generate").equals("true")) {
			new PsiClassesGenerator(parameters).generate();
			new PsiFactoryGenerator(grammarInfo, configuration, outputFileFactory, parameters).generate();
		}
		new ParserClassGenerator(parameters).generate();
	}

}
