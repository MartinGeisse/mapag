package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public final class SequenceExpression extends Expression {

	private final Expression left;
	private final Expression right;

	public SequenceExpression(Expression left, Expression right) {
		this(null, left, right);
	}

	private SequenceExpression(String name, Expression left, Expression right) {
		super(name);
		ParameterUtil.ensureNotNull(left, "left");
		ParameterUtil.ensureNotNull(right, "right");
		this.left = left;
		this.right = right;
	}

	@Override
	public Expression withName(String name) {
		return new SequenceExpression(name, left, right);
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

	@Override
	protected boolean subclassEquals(Object obj) {
		SequenceExpression other = (SequenceExpression) obj;
		return left.equals(other.left) && right.equals(other.right);
	}

	@Override
	protected void buildSubclassHashCode(HashCodeBuilder builder) {
		builder.append(left).append(right);
	}

}
