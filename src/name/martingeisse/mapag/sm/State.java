package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.util.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public final class State {

	private final Set<StateElement> elements;

	public State(Set<StateElement> elements) {
		this.elements = elements;
	}

	public Set<StateElement> getElements() {
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
	public Pair<StateElement.ActionType, Set<StateElement>> determineReactionToTerminal(String terminal) {
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
				Set<String> reductionNonterminals = getReductionNonterminals(elementsThatWantToReduce);
				if (reductionNonterminals.isEmpty()) {
					throw new RuntimeException("cannot happen");
				} else if (reductionNonterminals.size() > 1) {
					throw new StateMachineException("reduce/reduce conflict in state " + this + " on terminal " + terminal);
				}
				return new Pair<>(StateElement.ActionType.REDUCE, elementsThatWantToReduce);
			}
		} else {
			if (elementsThatWantToReduce.isEmpty()) {
				return new Pair<>(StateElement.ActionType.SHIFT, elementsThatWantToShift);
			} else {
				throw new StateMachineException("shift/reduce conflict in state " + this + " on terminal " + terminal);
			}
		}
	}

	private static Set<String> getReductionNonterminals(Set<StateElement> elements) {
		Set<String> result = new HashSet<>();
		for (StateElement element : elements) {
			result.add(element.getLeftSide());
		}
		return result;
	}

	public Set<StateElement> determineRootElementsAfterShiftingNonterminal(String nonterminal) {
		Set<StateElement> result = new HashSet<>();
		for (StateElement originalElement : elements) {
			StateElement nextElement = originalElement.determineNextRootElementForNonterminal(nonterminal);
			if (nextElement != null) {
				result.add(nextElement);
			}
		}
		return result;
	}

}
