package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class Production {

	private final String leftHandSide;
	private final Expression rightHandSide;
	private final PrecedenceSpecificationType precedenceSpecificationType;

	TODO distinguish top-level alternatives from nested expressions. Allow precedence specs only at top level
	alternative operands (not at the root, but per alternative),
	both in classes and syntactically in the input grammar. Anything else is worse. Demand that toplevel alternatives
			have unambiguous rule precedence (defined or undefined).
	private final String precedenceSpecification;

	public Production(String leftHandSide, Expression rightHandSide) {
		this(leftHandSide, rightHandSide, PrecedenceSpecificationType.IMPLICIT, null);
	}

	public Production(String leftHandSide, Expression rightHandSide, PrecedenceSpecificationType precedenceSpecificationType, String precedenceSpecification) {
		ParameterUtil.ensureNotNullOrEmpty(leftHandSide, "leftHandSide");
		ParameterUtil.ensureNotNull(rightHandSide, "rightHandSide");
		ParameterUtil.ensureNotNull(precedenceSpecificationType, "precedenceSpecificationType");
		if (precedenceSpecificationType == PrecedenceSpecificationType.EXPLICIT) {
			if (precedenceSpecification == null) {
				throw new IllegalArgumentException("missing precedence specification for precedence specification type " + precedenceSpecificationType);
			}
		} else {
			if (precedenceSpecification != null) {
				throw new IllegalArgumentException("precedence specification is invalid for precedence specification type " + precedenceSpecificationType);
			}
		}
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
		this.precedenceSpecificationType = precedenceSpecificationType;
		this.precedenceSpecification = precedenceSpecification;
	}

	public String getLeftHandSide() {
		return leftHandSide;
	}

	public Expression getRightHandSide() {
		return rightHandSide;
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
