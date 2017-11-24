package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class Production extends ASTWrapperPsiElement implements PsiNamedElement {

	public Production(@NotNull ASTNode node) {
		super(node);
	}

	String getName() {
		return PsiUtil.getName(this);
	}

	PsiElement setName(String name) throws IncorrectOperationException {
		return name.martingeisse.mapag.input.psi.PsiUtil.setName(name);
	}

}
