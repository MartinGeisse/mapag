package name.martingeisse.mapag.ide.actions;

import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.java.standalone.StandaloneImplementationCodeGenerationDriver;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

import java.io.IOException;

/**
 *
 */
public class StandaloneGenerateAction extends AbstractGenerateAction {

	public StandaloneGenerateAction() {
		super("generate implementation classes for standalone");
	}

	@Override
	protected void generateCode(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory) throws IOException {
		CodeGenerationParameters parameters = new CodeGenerationParameters(grammarInfo, stateMachine, configuration, outputFileFactory);
		new StandaloneImplementationCodeGenerationDriver().generate(parameters);
	}

}
