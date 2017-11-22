package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.Associativity;
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

	// Most of the state machine building has been tested in other unit tests. One thing that is left is to
	// check for closure of the state set under shifting.

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

		for (State state : stateMachine.getStates()) {
			Assert.assertNotNull(stateMachine.getTerminalOrEofActions().get(state));
			Assert.assertNotNull(stateMachine.getNonterminalActions().get(state));
		}

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

		for (Map.Entry<State, ImmutableMap<String, Action.Shift>> stateEntry : stateMachine.getNonterminalActions().entrySet()) {
			State state = stateEntry.getKey();
			Assert.assertTrue(stateMachine.getStates().contains(state));
			for (Map.Entry<String, Action.Shift> actionEntry : stateEntry.getValue().entrySet()) {
				Action action = actionEntry.getValue();
				Assert.assertTrue(stateMachine.getStates().contains(((Action.Shift) action).getNextState()));
			}
		}

	}

}
