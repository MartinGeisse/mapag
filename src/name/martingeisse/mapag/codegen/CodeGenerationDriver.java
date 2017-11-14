package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

import java.io.IOException;

/**
 *
 */
public class CodeGenerationDriver {

	private final GrammarInfo grammarInfo;
	private final StateMachine stateMachine;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;

	public CodeGenerationDriver(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory) {
		this.grammarInfo = grammarInfo;
		this.stateMachine = stateMachine;
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
	}

	public void generate() throws ConfigurationException, IOException {
		new ParserClassGenerator(grammarInfo, stateMachine, configuration, outputFileFactory).generate();
		if (configuration.getRequired("symbolHolder.generate").equals("true")) {
			new SymbolHolderClassGenerator(grammarInfo, configuration, outputFileFactory).generate();
		}
		if (configuration.getRequired("psi.generate").equals("true")) {
			new PsiClassesGenerator(grammarInfo, configuration, outputFileFactory).generate();
		}
	}

}
