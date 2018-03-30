package name.martingeisse.mapag.ide.actions;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.intellij.StateMachineEncoder;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 */
public class ShowStateMachineAction extends AbstractGrammarAndConsoleAction {

	public ShowStateMachineAction() {
		super("show state machine");
	}

	protected String getConsoleTitle(AnActionEvent event) {
		return "state machine";
	}

	protected boolean needsPropertiesFile(AnActionEvent event) {
		return false;
	}

	protected void execute(AnActionEvent event, ConsoleViewImpl console, MapagSourceFile sourceFile, Configuration configuration) throws Exception {

		name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar =
			new PsiToGrammarConverter(true).convert(sourceFile);
		name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar =
			new GrammarCanonicalizer(extendedGrammar).run().getResult();
		GrammarInfo grammarInfo = new GrammarInfo(canonicalGrammar);
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();
		StateMachineEncoder stateMachineEncoder = new StateMachineEncoder(grammarInfo, stateMachine);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		stateMachineEncoder.dump(printWriter);
		printWriter.flush();
		console.print(stringWriter.toString(), ConsoleViewContentType.NORMAL_OUTPUT);

	}

}
