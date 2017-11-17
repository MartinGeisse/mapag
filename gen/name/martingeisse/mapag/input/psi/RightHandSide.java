package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class RightHandSide extends ASTWrapperPsiElement {

	public RightHandSide(@NotNull ASTNode node) {
		super(node);
	}

}
