package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.java.intellij.PsiClassesGenerator;
import name.martingeisse.mapag.codegen.java.intellij.PsiFactoryGenerator;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;

import java.io.IOException;

/**
 *
 */
public class OldCodeGenerationDriver {

	public static void generate(InternalCodeGenerationParameters parameters) throws ConfigurationException, IOException {
		new ParserClassGenerator(parameters).generate();
	}

}
