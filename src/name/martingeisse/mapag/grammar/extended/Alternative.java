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
		IMPLICIT,
		EXPLICIT,
		UNDEFINED
	}

}
