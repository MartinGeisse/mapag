package name.martingeisse.mapag.ide;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagAnnotator implements Annotator {

	@Override
	public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
		// TODO
		System.out.println("*** " + psiElement);
	}

}
