package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class OneOrMoreExpression extends Expression {

	private final Expression operand;

	public OneOrMoreExpression(Expression operand) {
		this(null, operand);
	}

	private OneOrMoreExpression(String name, Expression operand) {
		super(name);
		ParameterUtil.ensureNotNull(operand, "operand");
		this.operand = operand;
	}

	@Override
	public Expression withNameInternal(String name) {
		return new OneOrMoreExpression(name, operand);
	}

	public Expression getOperand() {
		return operand;
	}

	@Override
	public String toString() {
		return "(" + operand.toString() + ")+";
	}

}
