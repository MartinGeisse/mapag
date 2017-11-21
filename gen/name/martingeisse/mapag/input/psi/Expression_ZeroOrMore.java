package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Expression_ZeroOrMore extends Expression {

	public Expression_ZeroOrMore(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getOperand() {
		return (Expression) InternalPsiUtil.getChild(this, 0);
	}

}
