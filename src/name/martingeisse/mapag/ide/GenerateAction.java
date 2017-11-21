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
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
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
		Configuration configuration = new Configuration(properties);

		// we need a module to place output files in
		Module module = event.getDataContext().getData(LangDataKeys.MODULE);
		if (module == null) {
			console.print("No module available to place output files in", ConsoleViewContentType.ERROR_OUTPUT);
			return;
		}
		VirtualFile moduleFolder = module.getModuleFile();
		final VirtualFile existingOutputFolder = moduleFolder.findChild("gen");
		final VirtualFile outputFolder;
		if (existingOutputFolder == null) {
			try {
				outputFolder = moduleFolder.createChildDirectory(this, "gen");
			} catch (IOException e) {
				console.print("Could not create 'gen' folder.", ConsoleViewContentType.ERROR_OUTPUT);
				return;
			}
		} else {
			outputFolder = existingOutputFolder;
		}

		// do it!
		name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar =
			new PsiToGrammarConverter().convert((MapagSourceFile) psiFile);
		name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar =
			new GrammarCanonicalizer(extendedGrammar).run().getResult();
		GrammarInfo grammarInfo = new GrammarInfo(canonicalGrammar);
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();
		OutputFileFactory outputFileFactory = (packageName, className) -> {
			VirtualFile packageFolder = createPackageFolder(outputFolder, packageName);
			VirtualFile javaFile = packageFolder.createChildData(this, className + ".java");
			return javaFile.getOutputStream(this);
		};
		try {
			new CodeGenerationDriver(grammarInfo, stateMachine, configuration, outputFileFactory).generate();
		} catch (ConfigurationException e) {
			console.print(e.getMessage(), ConsoleViewContentType.ERROR_OUTPUT);
			return;
		} catch (IOException e) {
			console.print("IOException during code generation: " + e, ConsoleViewContentType.ERROR_OUTPUT);
			return;
		}

		console.print("Done.", ConsoleViewContentType.NORMAL_OUTPUT);
	}

	private VirtualFile createPackageFolder(VirtualFile parent, String packageName) throws IOException {
		if (packageName == null || packageName.isEmpty()) {
			return parent;
		}
		int index = packageName.indexOf('.');
		if (index != -1) {
			parent = createPackageFolder(parent, packageName.substring(0, index));
			packageName = packageName.substring(index + 1);
		}
		return parent.createChildDirectory(this, packageName);
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
