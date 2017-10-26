package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public final class State {

	private final ImmutableSet<StateElement> elements;

	public State(ImmutableSet<StateElement> elements) {
		this.elements = ParameterUtil.ensureNotNullOrEmpty(elements, "elements");
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
		List<StateElement> sortedElements = new ArrayList<>(elements);
		sortedElements.sort(Comparators.stateElementComparator);
		return "\n" + StringUtils.join(sortedElements, '\n') + "\n";
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

		// All state elements are at the end and have the specified terminal as their follow-terminal. So they can
		// only differ in their left side or alternative. Either case is a reduce/reduce conflict. Note that equal
		// duplicates have been filtered out since StateElement has proper hashCode()/equals() support and the elements
		// are stored in a Set<>.
		if (elementsThatWantToReduce.size() > 1) {
			throw new StateMachineException.ReduceReduceConflict(this, terminal);
		}
		StateElement elementThatWantsToReduce = (elementsThatWantToReduce.isEmpty() ? null : elementsThatWantToReduce.iterator().next());

		// handle non-conflict cases
		if (elementsThatWantToShift.isEmpty()) {
			if (elementsThatWantToReduce.isEmpty()) {
				return null;
			} else {
				return getReduce(elementThatWantsToReduce);
			}
		} else {
			if (elementsThatWantToReduce.isEmpty()) {
				return getShift(grammarInfo, elementsThatWantToShift);
			} else {
				// shift/reduce conflict resolution, see below
			}
		}

		// handle shift/reduce conflicts by resolution
		TerminalDefinition shiftPrecedenceDefinition = grammarInfo.getGrammar().getTerminalDefinitions().get(terminal);
		if (shiftPrecedenceDefinition == null) {
			throw new RuntimeException("cannot determine terminal definition for terminal " + terminal);
		}
		String reducePrecedenceTerminal = elementThatWantsToReduce.getAlternative().getEffectivePrecedenceTerminal();
		TerminalDefinition reducePrecedenceDefinition = grammarInfo.getGrammar().getTerminalDefinitions().get(reducePrecedenceTerminal);
		if (reducePrecedenceDefinition != null) {
			if (shiftPrecedenceDefinition.getPrecedenceIndex() != null && reducePrecedenceDefinition.getPrecedenceIndex() != null) {

				// if the incoming terminal has higher precedence, then shift
				if (shiftPrecedenceDefinition.getPrecedenceIndex() > reducePrecedenceDefinition.getPrecedenceIndex()) {
					return getShift(grammarInfo, elementsThatWantToShift);
				}

				// if the incoming terminal has lower precedence, then reduce
				if (shiftPrecedenceDefinition.getPrecedenceIndex() < reducePrecedenceDefinition.getPrecedenceIndex()) {
					return getReduce(elementThatWantsToReduce);
				}

				// sanity check: same precedence implies same associativity
				if (shiftPrecedenceDefinition.getAssociativity() != reducePrecedenceDefinition.getAssociativity()) {
					throw new RuntimeException("terminals have same precedence but different associativity: " + terminal + " and " + reducePrecedenceTerminal);
				}

				// on same precedence and left-associativity, reduce
				if (shiftPrecedenceDefinition.getAssociativity() == Associativity.LEFT) {
					return getReduce(elementThatWantsToReduce);
				}

				// on same precedence and right-associativity, shift
				if (shiftPrecedenceDefinition.getAssociativity() == Associativity.RIGHT) {
					return getShift(grammarInfo, elementsThatWantToShift);
				}

			}
		}

		// resolution was not successful
		throw new StateMachineException.ShiftReduceConflict(this, terminal);

	}

	private static Action.Shift getShift(GrammarInfo grammarInfo, Set<StateElement> elements) {
		StateBuilder builder = new StateBuilder(grammarInfo);
		for (StateElement element : elements) {
			builder.addElementClosure(element.getShifted());
		}
		return new Action.Shift(builder.build());
	}

	private Action getReduce(StateElement element) {
		return new Action.Reduce(element.getLeftSide(), element.getAlternative()).checkForAcceptingRootSymbol();
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
