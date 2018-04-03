package name.martingeisse.mapag.codegen.intellij;

import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.PsiClassesGenerator;

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
		if (configuration.getRequired("symbolHolder.generate").equals("true")) {
			new SymbolHolderClassGenerator(grammarInfo, configuration, outputFileFactory).generate();
		}
		if (configuration.getRequired("psi.generate").equals("true")) {
			new PsiClassesGenerator(parameters).generate();
			new PsiFactoryGenerator(grammarInfo, configuration, outputFileFactory).generate();
		}
		new ParserClassGenerator(grammarInfo, stateMachine, configuration, outputFileFactory).generate();
	}

}
