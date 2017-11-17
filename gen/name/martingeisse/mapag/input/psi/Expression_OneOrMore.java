package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Expression_OneOrMore extends Expression {

	public Expression_OneOrMore(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getOperand() {
		return (Expression) (getChildren()[0]);
	}

}
