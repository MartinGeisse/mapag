package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * Note: expression objects must support equals() / hashCode().
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
		if (fallbackName != null && fallbackName.isEmpty()) {
			throw new IllegalArgumentException("fallbackName cannot be empty");
		}
		if (name != null) {
			return this;
		}
		if (fallbackName == null) {
			return this;
		}
		return withName(fallbackName);
	}

	public String getName() {
		return name;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj.getClass() != getClass()) {
			return false;
		}
		if (!Objects.equals(name, ((Expression) obj).getName())) {
			return false;
		}
		return subclassEquals(obj);
	}

	protected abstract boolean subclassEquals(Object obj);

	@Override
	public final int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder().append(getClass()).append(name);
		buildSubclassHashCode(builder);
		return builder.toHashCode();
	}

	protected abstract void buildSubclassHashCode(HashCodeBuilder builder);

}
