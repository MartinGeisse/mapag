package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.Expression;

/**
 *
 */
public final class Production {

	private final String leftHandSide;
	private final Expression rightHandSide;

	public Production(String leftHandSide, Expression rightHandSide) {
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
