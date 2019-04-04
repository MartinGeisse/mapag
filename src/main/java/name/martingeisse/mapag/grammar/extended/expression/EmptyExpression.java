package name.martingeisse.mapag.grammar.extended.expression;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	public Expression withName(String name) {
		return new EmptyExpression(name);
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	protected boolean subclassEquals(Object obj) {
		return true;
	}

	@Override
	protected void buildSubclassHashCode(HashCodeBuilder builder) {
	}

}
