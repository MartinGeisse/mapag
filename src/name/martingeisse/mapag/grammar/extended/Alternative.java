package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class Alternative {

	private final Expression expression;
	private final PrecedenceSpecificationType precedenceSpecificationType;
	private final String precedenceSpecification;

	public Alternative(Expression expression) {
		this(expression, PrecedenceSpecificationType.IMPLICIT, null);
	}

	public Alternative(Expression expression, PrecedenceSpecificationType precedenceSpecificationType, String precedenceSpecification) {
		this.expression = ParameterUtil.ensureNotNull(expression, "expression");
		this.precedenceSpecificationType = ParameterUtil.ensureNotNull(precedenceSpecificationType, "precedenceSpecificationType");
		this.precedenceSpecification = precedenceSpecification;
		if (precedenceSpecificationType == PrecedenceSpecificationType.EXPLICIT) {
			if (precedenceSpecification == null) {
				throw new IllegalArgumentException("missing precedence specification for precedence specification type " + precedenceSpecificationType);
			}
			if (precedenceSpecification.isEmpty()) {
				throw new IllegalArgumentException("empty precedence specification for precedence specification type " + precedenceSpecificationType);
			}
		} else {
			if (precedenceSpecification != null) {
				throw new IllegalArgumentException("precedence specification is invalid for precedence specification type " + precedenceSpecificationType);
			}
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public PrecedenceSpecificationType getPrecedenceSpecificationType() {
		return precedenceSpecificationType;
	}

	public String getPrecedenceSpecification() {
		return precedenceSpecification;
	}

	public enum PrecedenceSpecificationType {

		IMPLICIT {
			@Override
			public String toString(String specification) {
				return "";
			}
		},
		EXPLICIT {
			@Override
			public String toString(String specification) {
				return specification;
			}
		},
		UNDEFINED {
			@Override
			public String toString(String specification) {
				return "%precedence %undefined";
			}
		};

		public abstract String toString(String specification);

	}

	@Override
	public String toString() {
		return "{" + expression + " / " + precedenceSpecificationType.toString(precedenceSpecification) + '}';
	}

}
