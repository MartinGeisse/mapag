package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class OrExpression extends Expression {

	private final Expression leftOperand;
	private final Expression rightOperand;

	public OrExpression(Expression leftOperand, Expression rightOperand) {
		this(null, leftOperand, rightOperand);
	}

	private OrExpression(String name, Expression leftOperand, Expression rightOperand) {
		super(name);
		ParameterUtil.ensureNotNull(leftOperand, "leftOperand");
		ParameterUtil.ensureNotNull(rightOperand, "rightOperand");
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	@Override
	public Expression withNameInternal(String name) {
		return new OrExpression(name, leftOperand, rightOperand);
	}

	public Expression getLeftOperand() {
		return leftOperand;
	}

	public Expression getRightOperand() {
		return rightOperand;
	}

	@Override
	public String toString() {
		return "(" + leftOperand + " | " + rightOperand + ')';
	}

}
