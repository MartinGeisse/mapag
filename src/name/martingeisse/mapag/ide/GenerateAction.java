package name.martingeisse.mapag.ide;

import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import name.martingeisse.mapag.input.PsiToGrammarConverter;

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
		PsiFile psiFile = event.getDataContext().getData(CommonDataKeys.PSI_FILE);
		if (!(psiFile instanceof MapagSourceFile)) {
			return;
		}
		Properties properties = readAssociatedProperties(psiFile);
		if (properties == null) {
			return;
		}
		new PsiToGrammarConverter().convert((MapagSourceFile)psiFile);

		// TODO

		System.out.println("generating... done!");
	}

	private Properties readAssociatedProperties(PsiFile grammarPsiFile) {
		VirtualFile propertiesFile = findPropertiesFile(grammarPsiFile);
		if (propertiesFile == null) {
			return null;
		}
		try (InputStream inputStream = propertiesFile.getInputStream()) {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (IOException e) {
			return null;
		}
	}

	private VirtualFile findPropertiesFile(PsiFile grammarPsiFile) {
		VirtualFile grammarFile = grammarPsiFile.getVirtualFile();
		if (grammarFile == null) {
			return null;
		}
		String grammarFileName = grammarFile.getName();
		if (!grammarFileName.endsWith(".mapag")) {
			return null;
		}
		String propertiesFileName = grammarFileName.substring(0, grammarFileName.length() - ".mapag".length()) + ".properties";
		return grammarFile.getParent().findChild(propertiesFileName);
	}

}
