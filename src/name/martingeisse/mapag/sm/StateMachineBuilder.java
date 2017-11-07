package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.canonical.annotation.AlternativeAnnotation;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class StateMachineBuilder {

	private final GrammarInfo grammarInfo;
	private final Set<State> states = new HashSet<>();
	private final Map<State, Map<String, Action>> terminalOrEofActions = new HashMap<>();
	private final Map<State, Map<String, Action.Shift>> nonterminalActions = new HashMap<>();

	public StateMachineBuilder(GrammarInfo grammarInfo) {
		this.grammarInfo = ParameterUtil.ensureNotNull(grammarInfo, "grammarInfo");
	}

	private static <A, B, C> ImmutableMap<A, ImmutableMap<B, C>> makeImmutable(Map<A, Map<B, C>> original) {
		Map<A, ImmutableMap<B, C>> result = new HashMap<>();
		for (Map.Entry<A, Map<B, C>> outerEntry : original.entrySet()) {
			result.put(outerEntry.getKey(), ImmutableMap.copyOf(outerEntry.getValue()));
		}
		return ImmutableMap.copyOf(result);
	}

	public StateMachine build() {
		// TODO test that name of the implicit root alternative is null
		String startNonterminal = grammarInfo.getGrammar().getStartNonterminalName();
		Alternative implicitAlternative = new Alternative(ImmutableList.of(startNonterminal), null, AlternativeAnnotation.EMPTY);
		StateBuilder builder = new StateBuilder(grammarInfo);
		builder.addElementClosure(new StateElement(SpecialSymbols.ROOT_SYMBOL_NAME, implicitAlternative, 0, SpecialSymbols.EOF_SYMBOL_NAME));
		State startState = builder.build();
		addStates(startState);
		return new StateMachine(ImmutableSet.copyOf(states), makeImmutable(terminalOrEofActions), makeImmutable(nonterminalActions), startState);
	}

	private Map<String, Action> getOrCreateTerminalOrEofActionMap(State state) {
		Map<String, Action> result = terminalOrEofActions.get(state);
		if (result == null) {
			result = new HashMap<>();
			terminalOrEofActions.put(state, result);
		}
		return result;
	}

	private Map<String, Action.Shift> getOrCreateNonterminalActionMap(State state) {
		Map<String, Action.Shift> result = nonterminalActions.get(state);
		if (result == null) {
			result = new HashMap<>();
			nonterminalActions.put(state, result);
		}
		return result;
	}

	private void addStates(State state) {
		if (states.add(state)) {
			getOrCreateTerminalOrEofActionMap(state);
			getOrCreateNonterminalActionMap(state);

			// Determine reaction to terminals in this state.
			for (TerminalDefinition terminalDefinition : grammarInfo.getGrammar().getTerminalDefinitions().values()) {
				addTerminalOrEofActions(state, terminalDefinition.getName());
			}

			// Determine reaction to EOF in this state
			addTerminalOrEofActions(state, SpecialSymbols.EOF_SYMBOL_NAME);

			// Determine reaction to nonterminals in this state. This just tries all nonterminals for simplicity, and
			// thus adds unnessecary states if the grammar contains alternatives that can never match. We don't care.
			for (NonterminalDefinition nonterminalDefinition : grammarInfo.getGrammar().getNonterminalDefinitions().values()) {
				addNonterminalOrErrorActions(state, nonterminalDefinition.getName());
			}

			// Determine reaction to the error symbol in this state
			addNonterminalOrErrorActions(state, SpecialSymbols.ERROR_SYMBOL_NAME);

		}
	}

	private void addTerminalOrEofActions(State state, String terminalOrEof) {
		Action action = state.determineActionForTerminalOrEof(grammarInfo, terminalOrEof);
		if (action == null) {
			return;
		}
		getOrCreateTerminalOrEofActionMap(state).put(terminalOrEof, action);
		if (action instanceof Action.Shift) {
			addStates(((Action.Shift) action).getNextState());
		}
	}

	private void addNonterminalOrErrorActions(State state, String nonterminalOrError) {
		State nextState = state.determineNextStateAfterShiftingNonterminal(grammarInfo, nonterminalOrError);
		if (nextState != null) {
			getOrCreateNonterminalActionMap(state).put(nonterminalOrError, new Action.Shift(nextState));
			addStates(nextState);
		}
	}

}
