package name.martingeisse.mapag.grammar.expression;

/**
 *
 */
public final class OptionalExpression extends Expression {

	private final Expression operand;

	public OptionalExpression(Expression operand) {
		this.operand = operand;
	}

}
