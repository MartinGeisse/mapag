package name.martingeisse.mapag.ide;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class MapagFindUsagesProvider implements FindUsagesProvider {

	@Nullable
	@Override
	public WordsScanner getWordsScanner() {
		return null;
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		// TODO references too! Which PSI nodes are named / references?
		return false;
	}

	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement) {
		return null;
	}

	@NotNull
	@Override
	public String getType(@NotNull PsiElement psiElement) {
		return "symbol";
	}

	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement psiElement) {
		// TODO references too! Which PSI nodes are named / references?
		if (psiElement instanceof PsiNamedElement) {
			String name = ((PsiNamedElement) psiElement).getName();
			if (name != null) {
				return name;
			}
		}
		return psiElement.getText();
	}

	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement psiElement, boolean b) {
		// TODO references too! Which PSI nodes are named / references?
	}

}
