package name.martingeisse.mapag.ide.actions;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;
import name.martingeisse.mapag.util.UserMessageException;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public abstract class AbstractGenerateAction extends AbstractGrammarAndConsoleAction {

	public AbstractGenerateAction(String text) {
		super(text);
	}

	protected String getConsoleTitle(AnActionEvent event) {
		return "MaPaG Generator";
	}

	protected void onConsoleOpened(AnActionEvent event, ConsoleViewImpl console) {
		console.print("Generating MaPaG Parser...", ConsoleViewContentType.NORMAL_OUTPUT);
	}

	protected boolean needsPropertiesFile(AnActionEvent event) {
		return true;
	}

	protected void execute(AnActionEvent event, ConsoleViewImpl console, MapagSourceFile sourceFile, Configuration configuration) throws Exception {

		// we need a module to place output files in. Should this use ModuleRootManager?
		Module module = event.getDataContext().getData(LangDataKeys.MODULE);
		if (module == null) {
			console.print("No module available to place output files in", ConsoleViewContentType.ERROR_OUTPUT);
			return;
		}

		// do it!
		name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar =
			new PsiToGrammarConverter(true).convert(sourceFile);
		name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar =
			new GrammarCanonicalizer(extendedGrammar).run().getResult();
		GrammarInfo grammarInfo = new GrammarInfo(canonicalGrammar);
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();
		ApplicationManager.getApplication().runWriteAction(() -> {
			try {

				// create the output folder and resource folder
				VirtualFile moduleFolder = module.getModuleFile().getParent();
				final VirtualFile existingOutputFolder = moduleFolder.findChild("gen");
				final VirtualFile outputFolder;
				if (existingOutputFolder == null) {
					try {
						outputFolder = moduleFolder.createChildDirectory(this, "gen");
					} catch (IOException e) {
						console.print("Could not create 'gen' folder: " + e, ConsoleViewContentType.ERROR_OUTPUT);
						return;
					}
				} else {
					outputFolder = existingOutputFolder;
				}
				final VirtualFile existingResourcesFolder = moduleFolder.findChild("gen_resources");
				final VirtualFile resourcesFolder;
				if (existingResourcesFolder == null) {
					try {
						resourcesFolder = moduleFolder.createChildDirectory(this, "gen_resources");
					} catch (IOException e) {
						console.print("Could not create 'gen_resources' folder: " + e, ConsoleViewContentType.ERROR_OUTPUT);
						return;
					}
				} else {
					resourcesFolder = existingResourcesFolder;
				}

				// this is our callback to generate output files
				OutputFileFactory outputFileFactory = new OutputFileFactory() {

					@Override
					public OutputStream createSourceFile(String packageName, String className) throws IOException {
						VirtualFile packageFolder = createPackageFolder(outputFolder, packageName);
						String fileName = className + ".java";
						VirtualFile javaFile = packageFolder.findChild(fileName);
						if (javaFile == null) {
							javaFile = packageFolder.createChildData(this, fileName);
						} else if (javaFile.isDirectory()) {
							throw new UserMessageException("collision with existing folder while creating output " +
								"file for package '" + packageName + "', class '" + className + "'");
						}
						return javaFile.getOutputStream(this);
					}

					@Override
					public OutputStream createResourceFile(String filename) throws IOException {
						VirtualFile file = resourcesFolder.findChild(filename);
						if (file == null) {
							file = resourcesFolder.createChildData(this, filename);
						} else if (file.isDirectory()) {
							throw new UserMessageException("collision with existing folder while creating resource file " + filename);
						}
						return file.getOutputStream(this);
					}

				};

				// run the code generator
				generateCode(grammarInfo, stateMachine, configuration, outputFileFactory);

			} catch (IOException e) {
				throw new RuntimeException("unexpected IOException", e);
			}
		});

		console.print("Done.", ConsoleViewContentType.NORMAL_OUTPUT);
	}

	private VirtualFile createPackageFolder(VirtualFile parent, String packageName) throws IOException {
		if (packageName == null || packageName.isEmpty()) {
			return parent;
		}
		int index = packageName.lastIndexOf('.');
		String localPackageName;
		if (index != -1) {
			parent = createPackageFolder(parent, packageName.substring(0, index));
			localPackageName = packageName.substring(index + 1);
		} else {
			localPackageName = packageName;
		}
		VirtualFile existing = parent.findChild(localPackageName);
		if (existing == null) {
			return parent.createChildDirectory(this, localPackageName);
		} else if (existing.isDirectory()) {
			return existing;
		} else {
			throw new UserMessageException("collision with existing file while creating output folders for package " + packageName);
		}
	}

	protected abstract void generateCode(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory) throws IOException;

}
