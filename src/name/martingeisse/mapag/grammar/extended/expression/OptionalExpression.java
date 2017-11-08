package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class OptionalExpression extends Expression {

	private final Expression operand;

	public OptionalExpression(Expression operand) {
		this(null, operand);
	}

	private OptionalExpression(String name, Expression operand) {
		super(name);
		ParameterUtil.ensureNotNull(operand, "operand");
		this.operand = operand;
	}

	@Override
	public Expression withNameInternal(String name) {
		return new OptionalExpression(name, operand);
	}

	public Expression getOperand() {
		return operand;
	}

	@Override
	public String toString() {
		return "(" + operand.toString() + ")?";
	}

}
