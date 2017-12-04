package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Expression_SeparatedOneOrMore extends Expression {

	public Expression_SeparatedOneOrMore(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getElement() {
		return (Expression) InternalPsiUtil.getChild(this, 1);
	}

	public Expression getSeparator() {
		return (Expression) InternalPsiUtil.getChild(this, 3);
	}

}
