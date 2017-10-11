package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class Production {

	private final String leftHandSide;
	private final Expression rightHandSide;

	public Production(String leftHandSide, Expression rightHandSide) {
		ParameterUtil.ensureNotNullOrEmpty(leftHandSide, "leftHandSide");
		ParameterUtil.ensureNotNull(rightHandSide, "rightHandSide");
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
	}

	public String getLeftHandSide() {
		return leftHandSide;
	}

	public Expression getRightHandSide() {
		return rightHandSide;
	}

}
