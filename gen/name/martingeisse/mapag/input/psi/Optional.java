package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public final class Optional<T extends PsiElement> extends ASTWrapperPsiElement {

	public Optional(@NotNull ASTNode node) {
		super(node);
	}

	public T getIt() {
		return (T) InternalPsiUtil.getChild(this, 0);
	}

}
