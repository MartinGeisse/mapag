package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
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
	private final Map<State, Map<String, Action>> terminalActions = new HashMap<>();
	private final Map<State, Map<String, Action.Shift>> nonterminalActions = new HashMap<>();

	public StateMachineBuilder(GrammarInfo grammarInfo) {
		this.grammarInfo = ParameterUtil.ensureNotNull(grammarInfo, "grammarInfo");
	}

	public StateMachine build() {
		String startNonterminal = grammarInfo.getGrammar().getStartNonterminalName();
		Alternative implicitAlternative = new Alternative(ImmutableList.of(startNonterminal));
		StateBuilder builder = new StateBuilder(grammarInfo);
		builder.addElementClosure(new StateElement(SpecialSymbols.IMPLICIT_START_NONTERMINAL_NAME, implicitAlternative, 0, SpecialSymbols.EOF_TOKEN_NAME));
		State startState = builder.build();
		addStates(startState);
		return new StateMachine(ImmutableSet.copyOf(states), makeImmutable(terminalActions), makeImmutable(nonterminalActions), startState);
	}

	private static <A, B, C> ImmutableMap<A, ImmutableMap<B, C>> makeImmutable(Map<A, Map<B, C>> original) {
		Map<A, ImmutableMap<B, C>> result = new HashMap<>();
		for (Map.Entry<A, Map<B, C>> outerEntry : original.entrySet()) {
			result.put(outerEntry.getKey(), ImmutableMap.copyOf(outerEntry.getValue()));
		}
		return ImmutableMap.copyOf(result);
	}

	private Map<String, Action> getOrCreateTerminalActionMap(State state) {
		Map<String, Action> result = terminalActions.get(state);
		if (result == null) {
			result = new HashMap<>();
			terminalActions.put(state, result);
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
			getOrCreateTerminalActionMap(state);
			getOrCreateNonterminalActionMap(state);

			// Determine reaction to terminals in this state.
			for (TerminalDefinition terminalDefinition : grammarInfo.getGrammar().getTerminalDefinitions().values()) {
				String terminal = terminalDefinition.getName();
				Action action = state.determineActionForTerminal(grammarInfo, terminal);
				if (action == null) {
					continue;
				}
				getOrCreateTerminalActionMap(state).put(terminal, action);
				if (action instanceof Action.Shift) {
					addStates(((Action.Shift)action).getNextState());
				}
			}

			// Determine reaction to nonterminals in this state. This just tries all nonterminals for simplicity, and
			// thus adds unnessecary states if the grammar contains alternatives that can never match. We don't care.
			for (NonterminalDefinition nonterminalDefinition : grammarInfo.getGrammar().getNonterminalDefinitions().values()) {
				String nonterminal = nonterminalDefinition.getName();
				State nextState = state.determineNextStateAfterShiftingNonterminal(grammarInfo, nonterminal);
				if (nextState != null) {
					addStates(nextState);
					getOrCreateNonterminalActionMap(state).put(nonterminal, new Action.Shift(nextState));
				}
			}

		}
	}

}
