package name.martingeisse.mapag.grammar.extended.expression;

/**
 * This expression matches the empty symbol string.
 */
public final class EmptyExpression extends Expression {

	public EmptyExpression() {
		this(null);
	}

	private EmptyExpression(String name) {
		super(name);
	}

	@Override
	public Expression withNameInternal(String name) {
		return new EmptyExpression(name);
	}

	@Override
	public String toString() {
		return "";
	}

}
