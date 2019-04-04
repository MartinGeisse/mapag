package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.AlternativeAttributes;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import name.martingeisse.mapag.util.ListUtil;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public final class State {

	private final ImmutableSet<StateElement> elements;
	private int cachedHashCode = 0;

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
		if (cachedHashCode == 0) {
			cachedHashCode = elements.hashCode();
		}
		return cachedHashCode;
	}

	@Override
	public String toString() {
		ImmutableList<StateElement> sortedElements = ListUtil.sorted(elements, Comparators.stateElementComparator);
		return "\n" + StringUtils.join(sortedElements, '\n') + "\n";
	}

	// Returns null to indicate a run-time syntax error
	public Action determineActionForTerminalOrEof(GrammarInfo grammarInfo, StateMachineBuildingCache cache, String terminalOrEof) {

		// determine which elements want to shift that terminal, and which want to reduce when seeing that terminal
		Set<StateElement> elementsThatWantToShift = new HashSet<>();
		Set<StateElement> elementsThatWantToReduce = new HashSet<>();
		elementLoop:
		for (StateElement element : elements) {
			StateElement.ActionType actionType = element.determineActionTypeForTerminal(terminalOrEof);
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

		// handle cases without reducible elements (shift or error)
		if (elementsThatWantToReduce.isEmpty()) {
			if (elementsThatWantToShift.isEmpty()) {
				return getError();
			} else {
				return getShift(grammarInfo, cache, elementsThatWantToShift);
			}
		}

		// handle cases without shifting elements (reduce single element; R/R conflict if multiple elements)
		if (elementsThatWantToShift.isEmpty()) {
			if (elementsThatWantToReduce.size() > 1) {
				throw new StateMachineException.ReduceReduceConflict(this, terminalOrEof, ImmutableSet.copyOf(elementsThatWantToReduce));
			} else {
				return getReduce(elementsThatWantToReduce.iterator().next());
			}
		}

		// Now we have at least one reducible element (but possibly more than one), and we could also shift. That means
		// we now have a shift-reduce conflict, and if there is more than one reducible element then we also have a
		// reduce-reduce conflict.
		//
		// S/R conflicts can be handled by precedence and resolve blocks, if present. R/R conflicts in general cannot
		// be solved by those since they lack the expressive power to make sufficiently clear which of the alternatives
		// should be reduced unter which circumstances.
		//
		// However, there is one special case of R/R conflict that can still be resolved IF there is also an S/R
		// conflict, as is the case here -- observe that R/R conflicts without a simultaneous S/R conflict would have
		// thrown an exception above already. If we have multiple reducible elements, and if conflict resolution
		// determines to allow other elements to shift the terminal for all of them, the conflict disappears and we can
		// shift. This logic can never result in reducing though.
		//
		// Note that the elementsThatWantToReduce are equivalent to the alternatives that want to reduce, since all
		// of them are "at the end" and have the terminalOrEof as their follow terminal, so they can only differ
		// in the alternative to reduce.

		// handle the case of a single reducible element (no R/R conflict), i.e. a possibly resolvable S/R conflict
		if (elementsThatWantToReduce.size() == 1) {
			StateElement elementThatWantsToReduce = elementsThatWantToReduce.iterator().next();
			ConflictResolution resolution = resolveConflict(grammarInfo, elementThatWantsToReduce.getAlternative().getAttributes(), terminalOrEof);
			if (resolution == null) {
				throw new StateMachineException.ShiftReduceConflict(this, terminalOrEof,
					ImmutableSet.copyOf(elementsThatWantToShift), ImmutableSet.copyOf(elementsThatWantToReduce));
			}
			switch (resolution) {

				case SHIFT:
					return getShift(grammarInfo, cache, elementsThatWantToShift);

				case REDUCE:
					return getReduce(elementThatWantsToReduce);

				default:
					throw new RuntimeException("unknown conflict resolution: " + resolution);

			}
		}

		// Now we have multiple reducible elements (R/R conflict). Check which kinds of conflict resolution the elements allow.
		Set<StateElement> reducibleElementsWithShiftResolution = new HashSet<>();
		Set<StateElement> reducibleElementsWithReduceResolution = new HashSet<>();
		Set<StateElement> reducibleElementsWithoutResolution = new HashSet<>();
		for (StateElement elementThatWantsToReduce : elementsThatWantToReduce) {
			ConflictResolution resolution = resolveConflict(grammarInfo, elementThatWantsToReduce.getAlternative().getAttributes(), terminalOrEof);
			if (resolution == null) {
				reducibleElementsWithoutResolution.add(elementThatWantsToReduce);
			} else if (resolution == ConflictResolution.SHIFT) {
				reducibleElementsWithShiftResolution.add(elementThatWantsToReduce);
			} else if (resolution == ConflictResolution.REDUCE) {
				reducibleElementsWithReduceResolution.add(elementThatWantsToReduce);
			}
		}

		// If all reducible elements agree to shift, then the R/R conflict disappears.
		if (reducibleElementsWithoutResolution.isEmpty() && reducibleElementsWithReduceResolution.isEmpty()) {
			return getShift(grammarInfo, cache, elementsThatWantToShift);
		}

		// At this point we have an unresolvable R/R conflict. We now check for reducible elements that disagree in
		// their conflict resolution because then we want to show a more descriptive error message to the user.
		if (!reducibleElementsWithShiftResolution.isEmpty() && !reducibleElementsWithReduceResolution.isEmpty()) {
			throw new StateMachineException.ConflictResolutionDisagreement(this, terminalOrEof,
				ImmutableSet.copyOf(elementsThatWantToShift),
				ImmutableSet.copyOf(reducibleElementsWithShiftResolution),
				ImmutableSet.copyOf(reducibleElementsWithReduceResolution)
			);
		}

		// No conflict resolution disagreement, so we have a normal R/R conflict.
		throw new StateMachineException.ReduceReduceConflict(this, terminalOrEof, ImmutableSet.copyOf(elementsThatWantToReduce));

	}

	private static ConflictResolution resolveConflict(GrammarInfo grammarInfo, AlternativeAttributes attributes, String terminalOrEof) {
		if (attributes == null) {
			return null;
		}
		if (attributes.getEffectivePrecedenceTerminal() != null) {

			TerminalDefinition shiftPrecedenceDefinition = grammarInfo.getGrammar().getTerminalDefinitions().get(terminalOrEof);
			if (shiftPrecedenceDefinition == null) {
				throw new RuntimeException("cannot determine terminal definition for terminal " + terminalOrEof);
			}

			String reducePrecedenceTerminal = attributes.getEffectivePrecedenceTerminal();
			TerminalDefinition reducePrecedenceDefinition = grammarInfo.getGrammar().getTerminalDefinitions().get(reducePrecedenceTerminal);
			if (reducePrecedenceDefinition == null) {
				throw new RuntimeException("cannot determine terminal definition for terminal " + reducePrecedenceTerminal);
			}

			if (shiftPrecedenceDefinition.getPrecedenceIndex() != null && reducePrecedenceDefinition.getPrecedenceIndex() != null) {

				// if the incoming terminal has higher precedence, then shift
				if (shiftPrecedenceDefinition.getPrecedenceIndex() > reducePrecedenceDefinition.getPrecedenceIndex()) {
					return ConflictResolution.SHIFT;
				}

				// if the incoming terminal has lower precedence, then reduce
				if (shiftPrecedenceDefinition.getPrecedenceIndex() < reducePrecedenceDefinition.getPrecedenceIndex()) {
					return ConflictResolution.REDUCE;
				}

				// sanity check: same precedence implies same associativity
				if (shiftPrecedenceDefinition.getAssociativity() != reducePrecedenceDefinition.getAssociativity()) {
					throw new RuntimeException("terminals have same precedence but different associativity: " + terminalOrEof + " and " + reducePrecedenceTerminal);
				}

				// on same precedence and left-associativity, reduce
				if (shiftPrecedenceDefinition.getAssociativity() == Associativity.LEFT) {
					return ConflictResolution.REDUCE;
				}

				// on same precedence and right-associativity, shift
				if (shiftPrecedenceDefinition.getAssociativity() == Associativity.RIGHT) {
					return ConflictResolution.SHIFT;
				}

			}

		}

		if (attributes.getTerminalToResolution() != null) {
			ConflictResolution resolution = attributes.getTerminalToResolution().get(terminalOrEof);
			if (resolution != null) {
				return resolution;
			}
		}

		return null;
	}

	private static Action.Shift getShift(GrammarInfo grammarInfo, StateMachineBuildingCache cache, Set<StateElement> elementsThatWantToShift) {
		StateBuilder builder = new StateBuilder(grammarInfo, cache);
		for (StateElement element : elementsThatWantToShift) {
			builder.addElementClosure(element.getShifted());
		}
		return new Action.Shift(builder.build());
	}

	private Action getReduce(StateElement elementThatWantsToReduce) {
		return new Action.Reduce(elementThatWantsToReduce.getLeftSide(), elementThatWantsToReduce.getAlternative()).checkForAcceptingRootSymbol();
	}

	// Returns null if that nonterminal would cause a syntax error. This corresponds to an empty table entry and
	// cannot happen at runtime in LR(1).
	public State determineNextStateAfterShiftingNonterminal(GrammarInfo grammarInfo, StateMachineBuildingCache cache, String nonterminal) {
		StateBuilder builder = new StateBuilder(grammarInfo, cache);
		for (StateElement originalElement : elements) {
			StateElement nextElement = originalElement.determineNextRootElementForNonterminal(nonterminal);
			if (nextElement != null) {
				builder.addElementClosure(nextElement);
			}
		}
		return builder.isEmpty() ? null : builder.build();
	}

	/**
	 * Returns the action in case of a syntax error. This gives %reduceOnError a chance. Returns null to indicate
	 * that implicit reduce-on-error might be attempted, or an actual syntax error (that distinction is made at runtime).
	 */
	public Action getError() {

		// Here we must deal with the fact that the same alternative my appear in multiple state elements with
		// different lookahead terminals. That's not a conflict since it is the alternative that reduces on error,
		// not individual elements (since the error-causing lookahead token is irrelevant for the decision)
		Set<Pair<String, Alternative>> nonterminalsAndAlternativesThatWantToReduce = new HashSet<>();
		for (StateElement element : elements) {
			if (element.isAtEnd() && element.getAlternative().getAttributes().isReduceOnError()) {
				Pair<String, Alternative> pair = Pair.of(element.getLeftSide(), element.getAlternative());
				nonterminalsAndAlternativesThatWantToReduce.add(pair);
			}
		}

		// handle multiple user-defined %reduceOnError alternatives (now it's a conflict!)
		if (nonterminalsAndAlternativesThatWantToReduce.size() > 1) {
			// multiple elements want to reduce on error, causing a conflict
			throw new StateMachineException.OnErrorReduceReduceConflict(this,
				ImmutableSet.copyOf(nonterminalsAndAlternativesThatWantToReduce));
		}

		// handle a single user-defined %reduceOnError
		if (!nonterminalsAndAlternativesThatWantToReduce.isEmpty()) {
			Pair<String, Alternative> nonterminalAndAlternative = nonterminalsAndAlternativesThatWantToReduce.iterator().next();
			return new Action.Reduce(nonterminalAndAlternative.getLeft(), nonterminalAndAlternative.getRight());
		}

		// syntax error or implicit reduce-on-error
		return null;

	}

}
