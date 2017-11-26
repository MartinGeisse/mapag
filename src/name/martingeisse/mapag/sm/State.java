package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.AlternativeConflictResolver;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import name.martingeisse.mapag.util.ListUtil;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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

		//
		// TODO here we use the conflict resolver of the element that wants to reduce, so we can decide between SHIFT
		// and REDUCE. But this resolver actually tells which lookahead terminals are acceptable for a REDUCE,
		// so it could also be used to resolve REDUCE/REDUCE conflicts; the naming should be better in that case,
		// and defaults would be nice.
		//
		// No, that's only somewhat true. It doesn't just say which lookahead terminals are acceptable for a REDUCE,
		// but which of them have higher priority than shifting -- because there is no way to list "unacceptable"
		// terminals for shifting in the affected state(s). So this mechanism cannot simply be extended to cover
		// REDUCE/REDUCE too since the situation is different: For R/R, conflict resolution can be defined for
		// both conflicting alternatives, so saying "this has priority" in one of them is ugly (why not say in the
		// other that it *doesn't* have priority?)
		//
		// Also: Is any R/R conflict solved in a meaningful way just by looking at the lookahead token? Probably not;
		// most of them are just bugs in the grammar (unlike S/R). Even if it's not a bug, the lookaehad token
		// usually won't give any useful information to resolve the conflict.
		//
		// Bottom line: Don't try to solve R/R here. S/R is already nicely solved. Being able to specify a default
		// would be nice though.
		//
		// -----
		//
		// 1. %eof as lookahead in resolve blocks is probably useless because it cannot be shifted
		// 2. We don't need conflict resolution to solve the "%error at EOF" problem, but rather (probably)
		//    a way to define an alternative that is only valid at the end of the file. Idea: use %eof as a modifier
		//    keyword (like precedence, resolve blocks and %reduceOnError); This causes, during state building
		//    ("dot-into-nonterminal" closure) to include, for this alternative, only the state element with
		//    %eof as the lookahead (or not at all if %eof is not a valid lookahead in the state to build)
		// 3. Should probably collect precedence, resolve blocks, reduce-on-error and reduce-on-eof-only into a
		//    separate object to simplify the Alternative class. The current ConflictResolver is a starting point;
		//    add reduceOnError and reduceOnEofOnly to that.

		// handle shift/reduce conflicts by resolution
		AlternativeConflictResolver conflictResolver = elementThatWantsToReduce.getAlternative().getConflictResolver();
		if (conflictResolver != null) {

			if (conflictResolver.getEffectivePrecedenceTerminal() != null) {
				TerminalDefinition shiftPrecedenceDefinition = grammarInfo.getGrammar().getTerminalDefinitions().get(terminalOrEof);
				if (shiftPrecedenceDefinition == null) {
					throw new RuntimeException("cannot determine terminal definition for terminal " + terminalOrEof);
				}
				String reducePrecedenceTerminal = conflictResolver.getEffectivePrecedenceTerminal();
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

			if (conflictResolver.getTerminalToResolution() != null) {
				ConflictResolution resolution = conflictResolver.getTerminalToResolution().get(terminalOrEof);
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
	 * an actual syntax error.
	 */
	public Action getError() {
		// Here we must deal with the fact that the same alternative my appear in multiple state elements with
		// different lookahead terminals. That's not a conflict since it is the alternative that reduces on error,
		// not individual elements (since the error-causing lookahead token is irrelevant for the decision)
		Set<Pair<String, Alternative>> nonterminalsAndAlternativesThatWantToReduce = new HashSet<>();
		for (StateElement element : elements) {
			if (element.isAtEnd() && element.getAlternative().isReduceOnError()) {
				nonterminalsAndAlternativesThatWantToReduce.add(Pair.of(element.getLeftSide(), element.getAlternative()));
			}
		}
		if (nonterminalsAndAlternativesThatWantToReduce.isEmpty()) {
			// syntax error
			return null;
		}
		if (nonterminalsAndAlternativesThatWantToReduce.size() > 1) {
			// multiple elements want to reduce on error, causing a conflict
			throw new StateMachineException.OnErrorReduceReduceConflict(this,
				ImmutableSet.copyOf(nonterminalsAndAlternativesThatWantToReduce));
		}
		Pair<String, Alternative> nonterminalAndAlternative = nonterminalsAndAlternativesThatWantToReduce.iterator().next();
		return new Action.Reduce(nonterminalAndAlternative.getLeft(), nonterminalAndAlternative.getRight());
	}

}
