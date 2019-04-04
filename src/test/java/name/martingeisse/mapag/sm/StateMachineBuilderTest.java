package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.GrammarBuilder;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 *
 */
public class StateMachineBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new StateMachineBuilder(null);
	}


	@Test
	public void checkClosureUnderShiftingInNumberExpressionGrammar() {
		GrammarBuilder builder = new GrammarBuilder();
		builder.setStartNonterminalName("e");
		builder.addTerminals("NUMBER");
		builder.addTerminals(1, Associativity.LEFT, "PLUS", "MINUS");
		builder.addTerminals(2, Associativity.LEFT, "TIMES");
		builder.createNonterminal("e")
			.addAlternative("NUMBER")
			.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
			.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
			.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e");
		Grammar grammar = builder.build();
		GrammarInfo grammarInfo = new GrammarInfo(grammar);
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();

		// most of the state machine building has been tested in other unit tests, so we only check some basics here:

		// check that an action map is available for each state
		for (State state : stateMachine.getStates()) {
			Assert.assertNotNull(stateMachine.getTerminalOrEofActions().get(state));
			Assert.assertNotNull(stateMachine.getNonterminalActions().get(state));
		}

		// check that all resulting actions from shifting terminals are part of the state machine
		for (Map.Entry<State, ImmutableMap<String, Action>> stateEntry : stateMachine.getTerminalOrEofActions().entrySet()) {
			State state = stateEntry.getKey();
			Assert.assertTrue(stateMachine.getStates().contains(state));
			for (Map.Entry<String, Action> actionEntry : stateEntry.getValue().entrySet()) {
				Action action = actionEntry.getValue();
				if (action instanceof Action.Shift) {
					Assert.assertTrue(stateMachine.getStates().contains(((Action.Shift) action).getNextState()));
				}
			}
		}

		// check that all resulting actions from shifting nonterminals are part of the state machine
		for (Map.Entry<State, ImmutableMap<String, Action.Shift>> stateEntry : stateMachine.getNonterminalActions().entrySet()) {
			State state = stateEntry.getKey();
			Assert.assertTrue(stateMachine.getStates().contains(state));
			for (Map.Entry<String, Action.Shift> actionEntry : stateEntry.getValue().entrySet()) {
				Action action = actionEntry.getValue();
				Assert.assertTrue(stateMachine.getStates().contains(((Action.Shift) action).getNextState()));
			}
		}

		// check properties of the start and end states
		State startState = stateMachine.getStartState();
		State endState = startState.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), grammar.getStartNonterminalName());
		Assert.assertNotNull(endState);
		Assert.assertEquals(Action.Accept.INSTANCE, stateMachine.getTerminalOrEofActions().get(endState).get(SpecialSymbols.EOF_SYMBOL_NAME));

	}

}
