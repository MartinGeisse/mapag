package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class Production {

	private final String leftHandSide;
	private final ImmutableList<Alternative> alternatives;

	public Production(String leftHandSide, ImmutableList<Alternative> alternatives) {
		this.leftHandSide = ParameterUtil.ensureNotNullOrEmpty(leftHandSide, "leftHandSide");
		this.alternatives = ParameterUtil.ensureNotNullOrEmpty(alternatives, "alternatives");
		ParameterUtil.ensureNoNullElement(alternatives, "alternatives");
		if (leftHandSide.startsWith("%")) {
			throw new IllegalArgumentException("cannot define a production for special symbol " + leftHandSide);
		}
	}

	public String getLeftHandSide() {
		return leftHandSide;
	}

	public ImmutableList<Alternative> getAlternatives() {
		return alternatives;
	}

}
