package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.SpecialSymbols;
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
		ImmutableList<StateElement> sortedElements = ListUtil.sorted(elements, Comparators.stateElementComparator);
		return "\n" + StringUtils.join(sortedElements, '\n') + "\n";
	}

	// Returns null to indicate a run-time syntax error
	public Action determineActionForTerminalOrEof(GrammarInfo grammarInfo, String terminalOrEof) {
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

		// All state elements are at the end and have the specified terminal as their follow-terminal. So they can
		// only differ in their left side or alternative. Either case is a reduce/reduce conflict. Note that equal
		// duplicates have been filtered out since StateElement has proper hashCode()/equals() support and the elements
		// are stored in a Set<>.
		if (elementsThatWantToReduce.size() > 1) {
			throw new StateMachineException.ReduceReduceConflict(this, terminalOrEof, ImmutableSet.copyOf(elementsThatWantToReduce));
		}
		StateElement elementThatWantsToReduce = (elementsThatWantToReduce.isEmpty() ? null : elementsThatWantToReduce.iterator().next());

		// handle non-conflict cases
		if (elementsThatWantToShift.isEmpty()) {
			if (elementsThatWantToReduce.isEmpty()) {
				return getError();
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
		AlternativeAttributes attributes = elementThatWantsToReduce.getAlternative().getAttributes();
		if (attributes != null) {

			if (attributes.getEffectivePrecedenceTerminal() != null) {
				TerminalDefinition shiftPrecedenceDefinition = grammarInfo.getGrammar().getTerminalDefinitions().get(terminalOrEof);
				if (shiftPrecedenceDefinition == null) {
					throw new RuntimeException("cannot determine terminal definition for terminal " + terminalOrEof);
				}
				String reducePrecedenceTerminal = attributes.getEffectivePrecedenceTerminal();
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
							throw new RuntimeException("terminals have same precedence but different associativity: " + terminalOrEof + " and " + reducePrecedenceTerminal);
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
			}

			if (attributes.getTerminalToResolution() != null) {
				ConflictResolution resolution = attributes.getTerminalToResolution().get(terminalOrEof);
				if (resolution != null) {
					switch (resolution) {

						case SHIFT:
							return getShift(grammarInfo, elementsThatWantToShift);

						case REDUCE:
							return getReduce(elementThatWantsToReduce);

						default:
							throw new RuntimeException("unknown conflict resolution: " + resolution);

					}
				}
			}

		}

		// resolution was not successful
		throw new StateMachineException.ShiftReduceConflict(this, terminalOrEof,
			ImmutableSet.copyOf(elementsThatWantToShift), ImmutableSet.copyOf(elementsThatWantToReduce));

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
			if (element.getAlternative().getAttributes().isReduceOnError()) {
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
