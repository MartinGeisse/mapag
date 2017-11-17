package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Expression_Parenthesized extends Expression {

	public Expression_Parenthesized(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getInner() {
		return (Expression) (getChildren()[1]);
	}

}
