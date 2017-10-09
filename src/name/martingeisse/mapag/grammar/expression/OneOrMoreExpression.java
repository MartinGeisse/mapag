package name.martingeisse.mapag.grammar.expression;

/**
 *
 */
public final class OneOrMoreExpression extends Expression {

	private final Expression operand;

	public OneOrMoreExpression(Expression operand) {
		this.operand = operand;
	}

	public Expression getOperand() {
		return operand;
	}
	
}
