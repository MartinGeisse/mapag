package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public abstract class Expression {

	private final String name;

	public Expression(String name) {
		this.name = ParameterUtil.ensureNotEmpty(name, "name");
	}

	/**
	 * Returns a new expression with the same meaning as this one but with the specified name. This expression must not
	 * have a name, otherwise this method throws an {@link IllegalStateException}.
	 */
	public abstract Expression withName(String name);

	/**
	 * Like withName(name), but if this expression already has a name then this method keeps it.
	 */
	public final Expression withFallbackName(String fallbackName) {
		ParameterUtil.ensureNotNullOrEmpty(fallbackName, "fallbackName");
		return (name == null ? withName(fallbackName) : this);
	}

	public String getName() {
		return name;
	}

}
