package name.martingeisse.mapag.grammar.extended.expression;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

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
		checkNoName(name);
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
	public ImmutableList<Expression> determineOrOperands() {
		ImmutableList<Expression> leftResult = left.determineOrOperands();
		if (leftResult.size() != 1) {
			return super.determineOrOperands();
		}
		ImmutableList<Expression> rightResult = right.determineOrOperands();
		if (rightResult.size() != 1) {
			return super.determineOrOperands();
		}
		Expression leftResultElement = leftResult.get(0);
		Expression rightResultElement = rightResult.get(0);
		if (leftResultElement == left && rightResultElement == right) {
			return ImmutableList.of(this);
		} else {
			return ImmutableList.of(new SequenceExpression(leftResultElement, rightResultElement));
		}
	}

}
