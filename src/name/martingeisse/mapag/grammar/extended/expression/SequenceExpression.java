package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class SequenceExpression extends Expression {

	private final Expression left;
	private final Expression right;

	public SequenceExpression(Expression left, Expression right) {
		ParameterUtil.ensureNotNull(left, "left");
		ParameterUtil.ensureNotNull(right, "right");
		this.left = left;
		this.right = right;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "(" + left.toString() + ' ' + right.toString() + ')';
	}

}
