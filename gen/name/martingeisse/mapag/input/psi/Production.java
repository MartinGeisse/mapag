package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public abstract class Production extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {

	public Production(@NotNull ASTNode node) {
		super(node);
	}

	public void superclassDelete() throws IncorrectOperationException {
		super.delete();
	}

}
