package name.martingeisse.mapag.ide;

import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

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
		System.out.println("generating... done!");
	}

}
