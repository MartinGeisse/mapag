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
		if (psiFile == null) {
			return;
		}
		if (psiFile.getLanguage() != MapagSpecificationLanguage.INSTANCE) {
			return;
		}
		System.out.println("selected MaPaG specification file: " + psiFile);
	}

	@Override
	public void actionPerformed(AnActionEvent anActionEvent) {
		System.out.println("generating... done!");
	}

}
