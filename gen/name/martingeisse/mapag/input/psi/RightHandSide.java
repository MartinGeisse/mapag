package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class RightHandSide extends ASTWrapperPsiElement {

	public RightHandSide(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getExpression() {
		return (Expression) InternalPsiUtil.getChild(this, 0);
	}

	public RightHandSide_WithExplicitResolver_Attributes getAttributes() {
		return (RightHandSide_WithExplicitResolver_Attributes) InternalPsiUtil.getChild(this, 1);
	}

}
