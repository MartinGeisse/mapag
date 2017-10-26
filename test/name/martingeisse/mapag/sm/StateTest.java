package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.GrammarBuilder;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StateTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new State(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmpty() {
		new State(ImmutableSet.of());
	}

	@Test
	public void testConstructorGetter() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1");
		StateElement stateElement = new StateElement("lll", alternative, 0, "foo");
		State state = new State(ImmutableSet.of(stateElement));
		Assert.assertEquals(ImmutableSet.of(stateElement), state.getElements());
	}

	@Test
	public void testEqualsAndHashCode() {

		Alternative alternative1 = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1");
		StateElement stateElement1 = new StateElement("aaa", alternative1, 0, "foo");

		Alternative alternative2 = new Alternative(ImmutableList.of("foo", "bar"), null);
		StateElement stateElement2 = new StateElement("bbb", alternative2, 1, "baz");

		State[] states = new State[]{
			new State(ImmutableSet.of(stateElement1)),
			new State(ImmutableSet.of(stateElement2)),
			new State(ImmutableSet.of(stateElement1, stateElement2)),
			new State(ImmutableSet.of(stateElement1, stateElement2))
		};

		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states.length; j++) {
				if (i == j || (i >= 2 && j >= 2)) {
					Assert.assertEquals(states[i], states[j]);
					Assert.assertEquals(states[i].hashCode(), states[j].hashCode());
				} else {
					Assert.assertNotEquals(states[i], states[j]);
				}
			}
		}

	}

	@Test
	public void testActionsWithoutPrecedences() {

		GrammarBuilder builder = new GrammarBuilder();
		builder.setPackageName("test");
		builder.setClassName("Parser");
		builder.setStartNonterminalName("s");
		builder.addTerminals("a", "b", "c", "d", "e");
		builder.createNonterminal("s")
			.addAlternative("a", "b", "c")
			.addAlternative("p", "p")
			.addAlternative("p", "c")
			.addAlternative("q", "c");
		builder.createNonterminal("p")
			.addAlternative("e");
		builder.createNonterminal("q")
			.addAlternative();
		Grammar grammar = builder.build();
		GrammarInfo grammarInfo = new GrammarInfo(grammar);

		{
			Alternative startAlternative = new Alternative(ImmutableList.of("s"), null);
			StateElement startStateElement = new StateElement("dummy", startAlternative, 0, "%eof");
			State state0 = new StateBuilder(grammarInfo).addElementClosure(startStateElement).build();
			Assert.assertEquals(new State(ImmutableSet.of(
				startStateElement,
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 0, "%eof"),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1), 0, "%eof"),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2), 0, "%eof"),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3), 0, "%eof"),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 0, "e"),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 0, "c"),
				new StateElement("q", grammar.getNonterminalDefinitions().get("q").getAlternatives().get(0), 0, "c")
			)), state0);
			expectSyntaxError(grammarInfo, state0, "b", "d");
			expectReduceOnTerminal(grammarInfo, state0, "c", "q", grammar.getNonterminalDefinitions().get("q").getAlternatives().get(0));

			//
			// check the path through (s ::= a b c)
			//

			State state_a = expectShiftTerminal(grammarInfo, state0, "a");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 1, "%eof")
			)), state_a);
			expectSyntaxError(grammarInfo, state_a, "a", "c", "d", "e", "%eof", "p", "q");

			State state_a_b = expectShiftTerminal(grammarInfo, state_a, "b");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 2, "%eof")
			)), state_a_b);
			expectSyntaxError(grammarInfo, state_a_b, "a", "b", "d", "e", "%eof", "p", "q");

			State state_a_b_c = expectShiftTerminal(grammarInfo, state_a_b, "c");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 3, "%eof")
			)), state_a_b_c);
			expectSyntaxError(grammarInfo, state_a_b_c, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_a_b_c, "%eof", "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0));

			//
			// check the path through (s ::= p p, s ::= p c, p ::= e)
			//

			State state_e = expectShiftTerminal(grammarInfo, state0, "e");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 1, "e"),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 1, "c")
			)), state_e);
			expectSyntaxError(grammarInfo, state_e, "a", "b", "d", "%eof", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_e, "e", "p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0));
			expectReduceOnTerminal(grammarInfo, state_e, "c", "p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0));

			State state_p = state0.determineNextStateAfterShiftingNonterminal(grammarInfo, "p");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1), 1, "%eof"),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2), 1, "%eof"),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 0, "%eof")
			)), state_p);
			expectSyntaxError(grammarInfo, state_p, "a", "b", "d", "%eof", "q");

			State state_p_c = expectShiftTerminal(grammarInfo, state_p, "c");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2), 2, "%eof")
			)), state_p_c);
			expectSyntaxError(grammarInfo, state_p_c, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_p_c, "%eof", "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2));

			State state_p_e = expectShiftTerminal(grammarInfo, state_p, "e");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 1, "%eof")
			)), state_p_e);
			expectSyntaxError(grammarInfo, state_p_e, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_p_e, "%eof", "p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0));

			State state_p_p = state_p.determineNextStateAfterShiftingNonterminal(grammarInfo, "p");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1), 2, "%eof")
			)), state_p_p);
			expectSyntaxError(grammarInfo, state_p_p, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_p_p, "%eof", "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1));

			//
			// check the path through (s ::= q c)
			//

			State state_q = state0.determineNextStateAfterShiftingNonterminal(grammarInfo, "q");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3), 1, "%eof")
			)), state_q);
			expectSyntaxError(grammarInfo, state_q, "a", "b", "d", "e", "%eof", "p", "q");

			State state_q_c = expectShiftTerminal(grammarInfo, state_q, "c");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3), 2, "%eof")
			)), state_q_c);
			expectSyntaxError(grammarInfo, state_q_c, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_q_c, "%eof", "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3));

		}


	}

	// TODO with precedence

	private static State expectShiftTerminal(GrammarInfo grammarInfo, State state, String terminal) {
		Action action = state.determineActionForTerminal(grammarInfo, terminal);
		Assert.assertTrue(action instanceof Action.Shift);
		return ((Action.Shift)action).getNextState();
	}

	private static void expectReduceOnTerminal(GrammarInfo grammarInfo, State state, String terminal, String expectedNonterminal, Alternative expectedAlternative) {
		Action action = state.determineActionForTerminal(grammarInfo, terminal);
		Assert.assertTrue(action instanceof Action.Reduce);
		Action.Reduce reduce = (Action.Reduce)action;
		Assert.assertEquals(expectedNonterminal, reduce.getNonterminal());
		Assert.assertEquals(expectedAlternative, reduce.getAlternative());
	}

	// checks terminals and nonterminals
	private static void expectSyntaxError(GrammarInfo grammarInfo, State state, String... symbols) {
		for (String symbol : symbols) {
			Assert.assertNull(state.determineActionForTerminal(grammarInfo, symbol));
			Assert.assertNull(state.determineNextStateAfterShiftingNonterminal(grammarInfo, symbol));
		}
	}

}
