package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Expression_Sequence extends Expression {

	public Expression_Sequence(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getLeft() {
		return (Expression) (getChildren()[0]);
	}

	public Expression getRight() {
		return (Expression) (getChildren()[1]);
	}

}
