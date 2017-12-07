package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 */
public final class StateElement {

	private final String leftSide;
	private final Alternative alternative;
	private final int position;
	private final String followTerminal;

	public StateElement(String leftSide, Alternative alternative, int position, String followTerminal) {
		this.leftSide = ParameterUtil.ensureNotNullOrEmpty(leftSide, "leftSide");
		this.alternative = ParameterUtil.ensureNotNull(alternative, "alternative");
		this.position = position;
		if (position < 0) {
			throw new IllegalArgumentException("position cannot be negative");
		}
		if (position > alternative.getExpansion().getElements().size()) {
			throw new IllegalArgumentException("position cannot be greater than the number of right-hand symbols");
		}
		this.followTerminal = ParameterUtil.ensureNotNullOrEmpty(followTerminal, "followTerminal");
	}

	public String getLeftSide() {
		return leftSide;
	}

	public Alternative getAlternative() {
		return alternative;
	}

	public int getPosition() {
		return position;
	}

	public String getFollowTerminal() {
		return followTerminal;
	}

	public boolean isAtEnd() {
		return position == alternative.getExpansion().getElements().size();
	}

	public String getNextSymbol() {
		if (isAtEnd()) {
			return followTerminal;
		} else {
			return alternative.getExpansion().getElements().get(position).getSymbol();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StateElement) {
			StateElement other = (StateElement) obj;
			return leftSide.equals(other.leftSide) && alternative.equals(other.alternative) && position == other.position && followTerminal.equals(other.followTerminal);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(leftSide).append(alternative).append(position).append(followTerminal).toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(leftSide).append(':').append(alternative.getName()).append(" ::= ");
		int i = 0;
		for (ExpansionElement element : alternative.getExpansion().getElements()) {
			if (i == position) {
				builder.append(". ");
			}
			builder.append(element.getSymbol()).append(' ');
			i++;
		}
		if (i == position) {
			builder.append(". ");
		}
		builder.append("   [").append(followTerminal).append(']');
		return builder.toString();
	}

	public ActionType determineActionTypeForTerminal(String terminal) {
		ParameterUtil.ensureNotNullOrEmpty(terminal, "terminal");
		if (isAtEnd()) {
			return followTerminal.equals(terminal) ? ActionType.REDUCE : ActionType.DROP_ELEMENT;
		} else if (alternative.getExpansion().getElements().get(position).getSymbol().equals(terminal)) {
			return ActionType.SHIFT;
		} else {
			return ActionType.DROP_ELEMENT;
		}
	}

	public StateElement determineNextRootElementForNonterminal(String nonterminal) {
		ParameterUtil.ensureNotNullOrEmpty(nonterminal, "nonterminal");
		if (isAtEnd()) {
			return null;
		} else if (alternative.getExpansion().getElements().get(position).getSymbol().equals(nonterminal)) {
			return getShifted();
		} else {
			return null;
		}
	}

	// returns this element with the first symbol shifted
	public StateElement getShifted() {
		if (isAtEnd()) {
			throw new IllegalStateException("cannot shift -- remaining right side is already empty");
		}
		return new StateElement(leftSide, alternative, position + 1, followTerminal);
	}

	public enum ActionType {
		SHIFT,
		REDUCE,
		DROP_ELEMENT
	}

}
