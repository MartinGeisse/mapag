package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class ZeroOrMoreExpression extends Expression {

	private final Expression operand;

	public ZeroOrMoreExpression(Expression operand) {
		this(null, operand);
	}

	private ZeroOrMoreExpression(String name, Expression operand) {
		super(name);
		ParameterUtil.ensureNotNull(operand, "operand");
		this.operand = operand;
	}

	@Override
	public Expression withName(String name) {
		checkNoName(name);
		return new ZeroOrMoreExpression(name, operand);
	}

	public Expression getOperand() {
		return operand;
	}

	@Override
	public String toString() {
		return "(" + operand.toString() + ")*";
	}

}
