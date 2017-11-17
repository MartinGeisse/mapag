package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class RightHandSide_WithPrecedenceResolver extends RightHandSide {

	public RightHandSide_WithPrecedenceResolver(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getExpression() {
		return (Expression) (getChildren()[0]);
	}

	public LeafPsiElement getPrecedenceDefiningTerminal() {
		return (LeafPsiElement) (getChildren()[2]);
	}

}
