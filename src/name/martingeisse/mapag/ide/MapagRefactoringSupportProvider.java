package name.martingeisse.mapag.ide;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.input.psi.Production;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagRefactoringSupportProvider extends RefactoringSupportProvider {

	@Override
	public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
		return element instanceof Production;
	}

}
