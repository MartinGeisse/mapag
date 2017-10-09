package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import name.martingeisse.parsergen.grammar.Alternative;
import name.martingeisse.parsergen.grammar.Nonterminal;
import name.martingeisse.parsergen.grammar.Symbol;
import name.martingeisse.parsergen.grammar.Terminal;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class StateElement {

	private final Nonterminal leftSide;
	private final Alternative alternative;
	private final int position;
	private final Terminal followTerminal;

	public StateElement(Nonterminal leftSide, Alternative alternative, int position, Terminal followTerminal) {
		this.leftSide = leftSide;
		this.alternative = alternative;
		this.position = position;
		this.followTerminal = followTerminal;
	}

	public Nonterminal getLeftSide() {
		return leftSide;
	}

	public Alternative getAlternative() {
		return alternative;
	}

	public int getPosition() {
		return position;
	}

	public Terminal getFollowTerminal() {
		return followTerminal;
	}

	public boolean isAtEnd() {
		return position == alternative.getExpansionSymbols().size();
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
		builder.append(leftSide).append(" ::= ");
		int i = 0;
		for (Symbol symbol : alternative.getExpansionSymbols()) {
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

	public ActionType determineActionTypeForTerminal(Terminal terminal) {
		if (isAtEnd()) {
			return followTerminal.equals(terminal) ? ActionType.REDUCE : ActionType.DROP_ELEMENT;
		} else if (alternative.getExpansionSymbols().get(position).equals(terminal)) {
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

	public StateElement determineNextRootElementForNonterminal(Nonterminal nonterminal) {
		if (isAtEnd()) {
			return null;
		} else if (alternative.getExpansionSymbols().get(position).equals(nonterminal)) {
			return getShifted();
		} else {
			return null;
		}
	}

	// returns this element with the first symbol shifted
	public StateElement getShifted() {
		if (position == alternative.getExpansionSymbols().size()) {
			throw new IllegalStateException("cannot shift -- remaining right side is already empty");
		}
		return new StateElement(leftSide, alternative, position + 1, followTerminal);
	}

}
