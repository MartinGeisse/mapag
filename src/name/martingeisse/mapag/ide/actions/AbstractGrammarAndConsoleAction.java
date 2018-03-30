package name.martingeisse.mapag.ide.actions;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.ide.actions.PinActiveTabAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.ide.MapagSpecificationLanguage;
import name.martingeisse.mapag.util.SelfDescribingRuntimeException;
import name.martingeisse.mapag.util.UserMessageException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Base class for all actions that operate on a grammar file and can output text to a console. The most important
 * action is, of course, generating the parser code. However, currently this class is also used to dump information
 * about a grammar -- namely, the canonical grammar and the state machine. These should better be opened in an editor
 * instead of being printed to a console, but I found IntelliJ's editor handling to be too confusing to do this in
 * the first version.
 */
public abstract class AbstractGrammarAndConsoleAction extends AnAction {

	public AbstractGrammarAndConsoleAction() {
	}

	public AbstractGrammarAndConsoleAction(Icon icon) {
		super(icon);
	}

	public AbstractGrammarAndConsoleAction(@Nullable String text) {
		super(text);
	}

	public AbstractGrammarAndConsoleAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
		super(text, description, icon);
	}

	@Override
	public void update(AnActionEvent event) {
		PsiFile psiFile = event.getDataContext().getData(CommonDataKeys.PSI_FILE);
		boolean enabled = psiFile != null && psiFile.getLanguage() == MapagSpecificationLanguage.INSTANCE;
		event.getPresentation().setEnabledAndVisible(enabled);
	}

	@Override
	public void actionPerformed(AnActionEvent event) {

		// we need a project to show a console
		Project project = getEventProject(event);
		if (project == null) {
			return;
		}
		RunContentDescriptor runContentDescriptor = createConsole(project, getConsoleTitle(event));
		ConsoleViewImpl console = (ConsoleViewImpl) runContentDescriptor.getExecutionConsole();
		onConsoleOpened(event, console);

		// we need a MaPaG input file to process
		PsiFile psiFile = event.getDataContext().getData(CommonDataKeys.PSI_FILE);
		if (!(psiFile instanceof MapagSourceFile)) {
			console.print("The input file is not a MaPaG grammar file", ConsoleViewContentType.ERROR_OUTPUT);
			return;
		}

		// we may need an associated properties file
		Configuration configuration;
		if (needsPropertiesFile(event)) {
			Properties properties = readAssociatedProperties(psiFile, console);
			if (properties == null) {
				return;
			}
			configuration = new Configuration(properties);
		} else {
			configuration = null;
		}

		// do it!
		try {
			execute(event, console, (MapagSourceFile)psiFile, configuration);
		} catch (UserMessageException e) {
			console.print(e.getMessage(), ConsoleViewContentType.ERROR_OUTPUT);
		} catch (SelfDescribingRuntimeException e) {
			printError(console, e::describe);
		} catch (Exception e) {
			console.print("unexpected exception\n", ConsoleViewContentType.ERROR_OUTPUT);
			printError(console, e::printStackTrace);
		}

	}

	protected abstract String getConsoleTitle(AnActionEvent event);

	protected void onConsoleOpened(AnActionEvent event, ConsoleViewImpl console) {
	}

	protected abstract boolean needsPropertiesFile(AnActionEvent event);

	protected abstract void execute(AnActionEvent event, ConsoleViewImpl console, MapagSourceFile sourceFile, Configuration configuration) throws Exception;

	private Properties readAssociatedProperties(PsiFile grammarPsiFile, ConsoleViewImpl console) {
		VirtualFile propertiesFile = findPropertiesFile(grammarPsiFile, console);
		if (propertiesFile == null) {
			return null;
		}
		try (InputStream inputStream = propertiesFile.getInputStream()) {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (IOException e) {
			console.print("Exception while reading associated properties file: " + e, ConsoleViewContentType.ERROR_OUTPUT);
			return null;
		}
	}

	private VirtualFile findPropertiesFile(PsiFile grammarPsiFile, ConsoleViewImpl console) {
		VirtualFile grammarFile = grammarPsiFile.getVirtualFile();
		if (grammarFile == null) {
			console.print("The associated properties file cannot be determined because the input grammar " +
					"is not located in a (virtual) file.", ConsoleViewContentType.ERROR_OUTPUT);
			return null;
		}
		String grammarFileName = grammarFile.getName();
		if (!grammarFileName.endsWith(".mapag")) {
			console.print("Grammar file extension is not .mapag", ConsoleViewContentType.ERROR_OUTPUT);
			return null;
		}
		String propertiesFileName = grammarFileName.substring(0, grammarFileName.length() - ".mapag".length()) + ".properties";
		VirtualFile propertiesFile = grammarFile.getParent().findChild(propertiesFileName);
		if (propertiesFile == null) {
			console.print("Could not find associated properties file", ConsoleViewContentType.ERROR_OUTPUT);
			return null;
		}
		return propertiesFile;
	}

	private static RunContentDescriptor createConsole(@NotNull Project project, String title) {

		ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();

		DefaultActionGroup toolbarActions = new DefaultActionGroup();
		JComponent consoleComponent = new JPanel(new BorderLayout());

		JPanel toolbarPanel = new JPanel(new BorderLayout());
		toolbarPanel.add(ActionManager.getInstance().createActionToolbar(ActionPlaces.RUNNER_TOOLBAR, toolbarActions, false).getComponent());
		consoleComponent.add(toolbarPanel, BorderLayout.WEST);
		consoleComponent.add(consoleView.getComponent(), BorderLayout.CENTER);

		RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, consoleComponent, title, null);

		Executor executor = DefaultRunExecutor.getRunExecutorInstance();
		for (AnAction action : consoleView.createConsoleActions()) {
			toolbarActions.add(action);
		}
		toolbarActions.add(new PinActiveTabAction());
		toolbarActions.add(new CloseAction(executor, descriptor, project));
		ExecutionManager.getInstance(project).getContentManager().showRunContent(executor, descriptor);
		consoleView.allowHeavyFilters();
		return descriptor;

	}

	protected static void printError(ConsoleViewImpl console, Consumer<PrintWriter> printable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		printable.accept(printWriter);
		printWriter.flush();
		console.print(stringWriter.toString(), ConsoleViewContentType.ERROR_OUTPUT);
	}

}
