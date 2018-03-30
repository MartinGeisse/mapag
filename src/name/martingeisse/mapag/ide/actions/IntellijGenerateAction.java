package name.martingeisse.mapag.ide.actions;

import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.intellij.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

import java.io.IOException;

/**
 *
 */
public class IntellijGenerateAction extends AbstractGenerateAction {

	public IntellijGenerateAction() {
		super("generate classes for IntelliJ");
	}

	@Override
	protected void generateCode(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory) throws IOException {
		new CodeGenerationDriver(grammarInfo, stateMachine, configuration, outputFileFactory).generate();
	}

}
