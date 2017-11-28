package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public final class Production_Error2 extends Production {

	public Production_Error2(@NotNull ASTNode node) {
		super(node);
	}

	public String getName() {
		return PsiUtil.getName(this);
	}

	public PsiElement setName(String newName) throws IncorrectOperationException {
		return PsiUtil.setName(this, newName);
	}

	public void delete() throws IncorrectOperationException {
		PsiUtil.delete(this);
	}

}
