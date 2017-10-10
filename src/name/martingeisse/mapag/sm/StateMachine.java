package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.grammar.NonterminalDefinition;
import name.martingeisse.mapag.grammar.TerminalDefinition;
import name.martingeisse.mapag.grammar.info.GrammarInfo;
import name.martingeisse.mapag.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class StateMachine {

	private final GrammarInfo grammarInfo;
	private final Set<State> states = new HashSet<>();
	private final Map<State, Map<String, Action>> terminalActions = new HashMap<>();
	private final Map<State, Map<String, Action.ShiftAction>> nonterminalActions = new HashMap<>();
	private final State startState;

	/**
	 * Note: This object becomes invalid if the grammar gets changed after calling this constructor!
	 */
	public StateMachine(GrammarInfo grammarInfo) {
		this.grammarInfo = grammarInfo;
		this.startState = buildStartState(grammarInfo);
		addStates(startState);
	}

	private static State buildStartState(GrammarInfo grammarInfo) {
		Grammar grammar = grammarInfo.getGrammar();
		Alternative implicitAlternative = new Alternative(grammar.getStartSymbol());
		StateBuilder builder = new StateBuilder(grammarInfo);
		builder.addElementClosure(new StateElement(IMPLICIT_ROOT_NONTERMINAL, implicitAlternative, 0, Terminal.EOF));
		return builder.build();
	}

	private Map<String, Action> getOrCreateTerminalActionMap(State state) {
		Map<String, Action> result = terminalActions.get(state);
		if (result == null) {
			result = new HashMap<>();
			terminalActions.put(state, result);
		}
		return result;
	}

	private Map<String, Action.ShiftAction> getOrCreateNonterminalActionMap(State state) {
		Map<String, Action.ShiftAction> result = nonterminalActions.get(state);
		if (result == null) {
			result = new HashMap<>();
			nonterminalActions.put(state, result);
		}
		return result;
	}

	private void addStates(State state) {
		if (states.add(state)) {
			getOrCreateTerminalActionMap(state);
			getOrCreateNonterminalActionMap(state);

			// Determine reaction to terminals in this state.
			for (TerminalDefinition terminalDefinition : grammarInfo.getTerminalDefinitions().values()) {
				String terminal = terminalDefinition.getName();
				Pair<StateElement.ActionType, Set<StateElement>> reaction =  state.determineReactionToTerminal(terminal);
				if (reaction == null) {
					continue;
				}
				switch (reaction.getLeft()) {

					case DROP_ELEMENT:
						throw new RuntimeException();

					case SHIFT: {
						// note: the reaction contains the participating current elements, not the new roots.
						StateBuilder builder = new StateBuilder(grammarInfo);
						for (StateElement participatingElement : reaction.getRight()) {
							builder.addElementClosure(participatingElement.getShifted());
						}
						State nextState = builder.build();
						addStates(nextState);
						getOrCreateTerminalActionMap(state).put(terminal, new Action.ShiftAction(nextState));
						break;
					}

					case REDUCE: {
						String reduced = reaction.getRight().iterator().next().getLeftSide();
						getOrCreateTerminalActionMap(state).put(terminal, new Action.ReduceAction(reduced, null)); // TODO pass alternative
						break;
					}

					default:
						throw new RuntimeException();

				}
			}

			// Determine reaction to nonterminals in this state. This just tries all nonterminals for simplicity, and
			// thus adds unnessecary states if the grammar contains alternatives that can never match. We don't care.
			for (NonterminalDefinition nonterminalDefinition : grammarInfo.getNonterminalDefinitions().values()) {
				String nonterminal = nonterminalDefinition.getName();
				Set<StateElement> nextRootElements = state.determineRootElementsAfterShiftingNonterminal(nonterminal);
				if (nextRootElements.isEmpty()) {
					continue;
				}
				// note: the nextRootElements already contains the next roots, not the participating current state's elements
				StateBuilder builder = new StateBuilder(grammarInfo);
				for (StateElement rootElement : nextRootElements) {
					builder.addElementClosure(rootElement);
				}
				State nextState = builder.build();
				addStates(nextState);
				getOrCreateNonterminalActionMap(state).put(nonterminal, new Action.ShiftAction(nextState));
			}

		}
	}

	public GrammarInfo getGrammarInfo() {
		return grammarInfo;
	}

	public Set<State> getStates() {
		return states;
	}

	public Map<State, Map<String, Action>> getTerminalActions() {
		return terminalActions;
	}

	public Map<State, Map<String, Action.ShiftAction>> getNonterminalActions() {
		return nonterminalActions;
	}

	public State getStartState() {
		return startState;
	}

}
