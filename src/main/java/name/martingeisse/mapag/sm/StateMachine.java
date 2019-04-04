package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public class StateMachine {

	private final ImmutableSet<State> states;
	private final ImmutableMap<State, ImmutableMap<String, Action>> terminalOrEofActions;
	private final ImmutableMap<State, ImmutableMap<String, Action.Shift>> nonterminalActions;
	private final State startState;

	public StateMachine(ImmutableSet<State> states, ImmutableMap<State, ImmutableMap<String, Action>> terminalOrEofActions, ImmutableMap<State, ImmutableMap<String, Action.Shift>> nonterminalActions, State startState) {
		this.states = ParameterUtil.ensureNotNullOrEmpty(states, "states");
		this.terminalOrEofActions = ParameterUtil.ensureNotNull(terminalOrEofActions, "terminalOrEofActions");
		this.nonterminalActions = ParameterUtil.ensureNotNull(nonterminalActions, "nonterminalActions");
		this.startState = startState;
		if (!states.contains(startState)) {
			throw new IllegalArgumentException("states set does not contain start state");
		}
	}

	public ImmutableSet<State> getStates() {
		return states;
	}

	public ImmutableMap<State, ImmutableMap<String, Action>> getTerminalOrEofActions() {
		return terminalOrEofActions;
	}

	public ImmutableMap<State, ImmutableMap<String, Action.Shift>> getNonterminalActions() {
		return nonterminalActions;
	}

	public State getStartState() {
		return startState;
	}

}
