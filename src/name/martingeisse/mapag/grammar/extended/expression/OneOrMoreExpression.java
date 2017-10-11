package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class OneOrMoreExpression extends Expression {

	private final Expression operand;

	public OneOrMoreExpression(Expression operand) {
		ParameterUtil.ensureNotNull(operand, "operand");
		this.operand = operand;
	}

	public Expression getOperand() {
		return operand;
	}
	
}
