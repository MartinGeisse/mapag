package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Expression_Named extends Expression {

	public Expression_Named(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getExpression() {
		return (Expression) InternalPsiUtil.getChild(this, 0);
	}

	public LeafPsiElement getExpressionName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 2);
	}

}
