package name.martingeisse.mapag.grammar.expression;

/**
 *
 */
public final class SequenceExpression extends Expression {

	private final Expression left;
	private final Expression right;

	public SequenceExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

}
