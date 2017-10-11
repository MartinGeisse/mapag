package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.grammar.canonical.info.AlternativeInfo;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 */
public final class StateElement {

	private final String leftSide;
	private final AlternativeInfo alternativeInfo;
	private final int position;
	private final String followTerminal;

	public StateElement(String leftSide, AlternativeInfo alternativeInfo, int position, String followTerminal) {
		this.leftSide = leftSide;
		this.alternativeInfo = alternativeInfo;
		this.position = position;
		this.followTerminal = followTerminal;
	}

	public String getLeftSide() {
		return leftSide;
	}

	public AlternativeInfo getAlternativeInfo() {
		return alternativeInfo;
	}

	public int getPosition() {
		return position;
	}

	public String getFollowTerminal() {
		return followTerminal;
	}

	public boolean isAtEnd() {
		return position == alternativeInfo.getExpansion().size();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StateElement) {
			StateElement other = (StateElement) obj;
			return leftSide.equals(other.leftSide) && alternativeInfo.equals(other.alternativeInfo) && position == other.position && followTerminal.equals(other.followTerminal);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(leftSide).append(alternativeInfo).append(position).append(followTerminal).toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(leftSide).append(" ::= ");
		int i = 0;
		for (String symbol : alternativeInfo.getExpansion()) {
			if (i == position) {
				builder.append(". ");
			}
			builder.append(symbol).append(' ');
			i++;
		}
		if (i == position) {
			builder.append(". ");
		}
		builder.append(" : ").append(followTerminal);
		return builder.toString();
	}

	public ActionType determineActionTypeForTerminal(String terminal) {
		if (isAtEnd()) {
			return followTerminal.equals(terminal) ? ActionType.REDUCE : ActionType.DROP_ELEMENT;
		} else if (alternativeInfo.getExpansion().get(position).equals(terminal)) {
			return ActionType.SHIFT;
		} else {
			return ActionType.DROP_ELEMENT;
		}
	}

	public enum ActionType {
		SHIFT,
		REDUCE,
		DROP_ELEMENT
	}

	public StateElement determineNextRootElementForNonterminal(String nonterminal) {
		if (isAtEnd()) {
			return null;
		} else if (alternativeInfo.getExpansion().get(position).equals(nonterminal)) {
			return getShifted();
		} else {
			return null;
		}
	}

	// returns this element with the first symbol shifted
	public StateElement getShifted() {
		if (position == alternativeInfo.getExpansion().size()) {
			throw new IllegalStateException("cannot shift -- remaining right side is already empty");
		}
		return new StateElement(leftSide, alternativeInfo, position + 1, followTerminal);
	}

}
