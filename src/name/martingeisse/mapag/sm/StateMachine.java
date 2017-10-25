package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public class StateMachine {

	private final ImmutableSet<State> states;
	private final ImmutableMap<State, ImmutableMap<String, Action>> terminalActions;
	private final ImmutableMap<State, ImmutableMap<String, Action.Shift>> nonterminalActions;
	private final State startState;

	public StateMachine(ImmutableSet<State> states, ImmutableMap<State, ImmutableMap<String, Action>> terminalActions, ImmutableMap<State, ImmutableMap<String, Action.Shift>> nonterminalActions, State startState) {
		this.states = ParameterUtil.ensureNotNullOrEmpty(states, "states");
		this.terminalActions = ParameterUtil.ensureNotNull(terminalActions, "terminalActions");
		this.nonterminalActions = ParameterUtil.ensureNotNull(nonterminalActions, "nonterminalActions");
		this.startState = startState;
		if (!states.contains(startState)) {
			throw new IllegalArgumentException("states set does not contain start state");
		}
	}

	public ImmutableSet<State> getStates() {
		return states;
	}

	public ImmutableMap<State, ImmutableMap<String, Action>> getTerminalActions() {
		return terminalActions;
	}

	public ImmutableMap<State, ImmutableMap<String, Action.Shift>> getNonterminalActions() {
		return nonterminalActions;
	}

	public State getStartState() {
		return startState;
	}

}
