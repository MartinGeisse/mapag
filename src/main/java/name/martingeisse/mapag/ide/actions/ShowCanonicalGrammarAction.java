package name.martingeisse.mapag.ide.actions;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.CmToGrammarConverter;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 */
public class ShowCanonicalGrammarAction extends AbstractGrammarAndConsoleAction {

	public ShowCanonicalGrammarAction() {
		super("show canonical grammar");
	}

	protected String getConsoleTitle(AnActionEvent event) {
		return "canonical grammar";
	}

	protected boolean needsPropertiesFile(AnActionEvent event) {
		return false;
	}

	protected void execute(AnActionEvent event, ConsoleViewImpl console, MapagSourceFile sourceFile, Configuration configuration) throws Exception {

		name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar =
			new CmToGrammarConverter(true).convert(sourceFile);
		name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar =
			new GrammarCanonicalizer(extendedGrammar).run().getResult();

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		canonicalGrammar.dump(printWriter);
		printWriter.flush();
		console.print(stringWriter.toString(), ConsoleViewContentType.NORMAL_OUTPUT);

	}

}
