package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.parsergen.grammar.Nonterminal;
import name.martingeisse.parsergen.grammar.Terminal;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public final class State {

	private final ImmutableSet<StateElement> elements;

	public State(ImmutableSet<StateElement> elements) {
		this.elements = elements;
	}

	public ImmutableSet<StateElement> getElements() {
		return elements;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof State) {
			State other = (State) obj;
			return elements.equals(other.elements);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	@Override
	public String toString() {
		return elements.toString();
	}

	// Returns the action type and the set of participating elements, or null to indicate a run-time syntax error.
	public Pair<StateElement.ActionType, ImmutableSet<StateElement>> determineReactionToTerminal(Terminal terminal) {
		Set<StateElement> elementsThatWantToShift = new HashSet<>();
		Set<StateElement> elementsThatWantToReduce = new HashSet<>();
		elementLoop:
		for (StateElement element : elements) {
			StateElement.ActionType actionType = element.determineActionTypeForTerminal(terminal);
			switch (actionType) {

				case DROP_ELEMENT:
					continue elementLoop;

				case SHIFT:
					elementsThatWantToShift.add(element);
					break;

				case REDUCE:
					elementsThatWantToReduce.add(element);
					break;

				default:
					throw new RuntimeException();

			}
		}
		if (elementsThatWantToShift.isEmpty()) {
			if (elementsThatWantToReduce.isEmpty()) {
				return null;
			} else {
				Set<Nonterminal> reductionNonterminals = getReductionNonterminals(elementsThatWantToReduce);
				if (reductionNonterminals.isEmpty()) {
					throw new RuntimeException("cannot happen");
				} else if (reductionNonterminals.size() > 1) {
					throw new StateMachineException("reduce/reduce conflict in state " + this + " on terminal " + terminal);
				}
				return Pair.of(StateElement.ActionType.REDUCE, ImmutableSet.copyOf(elementsThatWantToReduce));
			}
		} else {
			if (elementsThatWantToReduce.isEmpty()) {
				return Pair.of(StateElement.ActionType.SHIFT, ImmutableSet.copyOf(elementsThatWantToShift));
			} else {
				throw new StateMachineException("shift/reduce conflict in state " + this + " on terminal " + terminal);
			}
		}
	}

	private static Set<Nonterminal> getReductionNonterminals(Set<StateElement> elements) {
		Set<Nonterminal> result = new HashSet<>();
		for (StateElement element : elements) {
			result.add(element.getLeftSide());
		}
		return result;
	}

	public ImmutableSet<StateElement> determineRootElementsAfterShiftingNonterminal(Nonterminal nonterminal) {
		Set<StateElement> result = new HashSet<>();
		for (StateElement originalElement : elements) {
			StateElement nextElement = originalElement.determineNextRootElementForNonterminal(nonterminal);
			if (nextElement != null) {
				result.add(nextElement);
			}
		}
		return ImmutableSet.copyOf(result);
	}

}
