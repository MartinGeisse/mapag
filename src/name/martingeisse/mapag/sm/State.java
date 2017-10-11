package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Pair;

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

	// Returns null to indicate a run-time syntax error
	public Action determineActionForTerminal(GrammarInfo grammarInfo, String terminal) {
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
				return getReduce(elementsThatWantToReduce);
			}
		} else {
			if (elementsThatWantToReduce.isEmpty()) {
				return getShift(grammarInfo, elementsThatWantToShift);
			} else {
				throw new StateMachineException("shift/reduce conflict in state " + this + " on terminal " + terminal);
			}
		}
	}

	private static Action.Reduce getReduce(Set<StateElement> elements) {
		Set<String> result = new HashSet<>();
		for (StateElement element : elements) {
			result.add(element.getLeftSide());
		}


		if (reductionNonterminals.isEmpty()) {
			throw new RuntimeException("cannot happen");
		} else if (reductionNonterminals.size() > 1) {
			throw new StateMachineException("reduce/reduce conflict in state " + this + " on terminal " + terminal);
		}

		return result;
	}

	private static Action.Shift getShift(GrammarInfo grammarInfo, Set<StateElement> elements) {
		StateBuilder builder = new StateBuilder(grammarInfo);
		for (StateElement element : elements) {
			builder.addElementClosure(element.getShifted());
		}
		return new Action.Shift(builder.build());
	}

	// Returns null if that nonterminal would cause a syntax error. This corresponds to an empty table entry and
	// cannot happen at runtime in LR(1).
	public State determineNextStateAfterShiftingNonterminal(GrammarInfo grammarInfo, String nonterminal) {
		StateBuilder builder = new StateBuilder(grammarInfo);
		for (StateElement originalElement : elements) {
			StateElement nextElement = originalElement.determineNextRootElementForNonterminal(nonterminal);
			if (nextElement != null) {
				builder.addElementClosure(nextElement);
			}
		}
		return builder.isEmpty() ? null : builder.build();
	}

}
