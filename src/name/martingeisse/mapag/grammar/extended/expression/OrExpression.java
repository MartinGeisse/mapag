package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class OrExpression extends Expression {

	private final Expression leftOperand;
	private final Expression rightOperand;

	public OrExpression(Expression leftOperand, Expression rightOperand) {
		ParameterUtil.ensureNotNull(leftOperand, "leftOperand");
		ParameterUtil.ensureNotNull(rightOperand, "rightOperand");
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	public Expression getLeftOperand() {
		return leftOperand;
	}

	public Expression getRightOperand() {
		return rightOperand;
	}

}
