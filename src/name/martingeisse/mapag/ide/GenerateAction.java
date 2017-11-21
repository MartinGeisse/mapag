package name.martingeisse.mapag.ide;

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
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class GenerateAction extends AnAction {

	public GenerateAction() {
		super("generate classes");
	}

	@Override
	public void update(AnActionEvent event) {
		PsiFile psiFile = event.getDataContext().getData(CommonDataKeys.PSI_FILE);
		boolean enabled = psiFile != null && psiFile.getLanguage() == MapagSpecificationLanguage.INSTANCE;
		// Note to myself: event.getPresentation() is correct here, not this.getTemplatePresentation()!
		event.getPresentation().setEnabledAndVisible(enabled);
	}

	@Override
	public void actionPerformed(AnActionEvent event) {

		// we need a project to show a console
		Project project = getEventProject(event);
		if (project == null) {
			return;
		}
		RunContentDescriptor runContentDescriptor = createConsole(project);
		ConsoleViewImpl console = (ConsoleViewImpl) runContentDescriptor.getExecutionConsole();
		console.print("Generating MaPaG Parser...", ConsoleViewContentType.NORMAL_OUTPUT);

		// we need a MaPaG input file to process
		PsiFile psiFile = event.getDataContext().getData(CommonDataKeys.PSI_FILE);
		if (!(psiFile instanceof MapagSourceFile)) {
			console.print("The input file is not a MaPaG grammar file", ConsoleViewContentType.ERROR_OUTPUT);
			return;
		}

		// we need an associated properties file
		Properties properties = readAssociatedProperties(psiFile, console);
		if (properties == null) {
			return;
		}

		// do it!
		new PsiToGrammarConverter().convert((MapagSourceFile) psiFile);
		// TODO

		console.print("Done.", ConsoleViewContentType.NORMAL_OUTPUT);
	}

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

	private static RunContentDescriptor createConsole(@NotNull Project project) {

		ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();

		DefaultActionGroup toolbarActions = new DefaultActionGroup();
		JComponent consoleComponent = new JPanel(new BorderLayout());

		JPanel toolbarPanel = new JPanel(new BorderLayout());
		toolbarPanel.add(ActionManager.getInstance().createActionToolbar(ActionPlaces.RUNNER_TOOLBAR, toolbarActions, false).getComponent());
		consoleComponent.add(toolbarPanel, BorderLayout.WEST);
		consoleComponent.add(consoleView.getComponent(), BorderLayout.CENTER);

		RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, null, consoleComponent, "MaPaG Generator", null);

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

}
