package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.*;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.testutil.ExAssert;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Consumer;

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
		Alternative alternative = new Alternative("a1", TestUtil.expansion("r1", "r2", "r3"), AlternativeAttributes.EMPTY);
		StateElement stateElement = new StateElement("lll", alternative, 0, "foo");
		State state = new State(ImmutableSet.of(stateElement));
		Assert.assertEquals(ImmutableSet.of(stateElement), state.getElements());
	}

	@Test
	public void testEqualsAndHashCode() {

		Alternative alternative1 = new Alternative("a1", TestUtil.expansion("r1", "r2", "r3"), AlternativeAttributes.EMPTY);
		StateElement stateElement1 = new StateElement("aaa", alternative1, 0, "foo");

		Alternative alternative2 = new Alternative("a2", TestUtil.expansion("foo", "bar"), AlternativeAttributes.EMPTY);
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
	public void testActionsWithoutResolver() {

		GrammarBuilder builder = new GrammarBuilder();
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
			Alternative startAlternative = new Alternative("a1", TestUtil.expansion("s"), AlternativeAttributes.EMPTY);
			StateElement startStateElement = new StateElement(SpecialSymbols.ROOT_SYMBOL_NAME, startAlternative, 0, SpecialSymbols.EOF_SYMBOL_NAME);
			State state0 = new StateBuilder(grammarInfo, new StateMachineBuildingCache()).addElementClosure(startStateElement).build();
			Assert.assertEquals(new State(ImmutableSet.of(
				startStateElement,
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 0, SpecialSymbols.EOF_SYMBOL_NAME),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1), 0, SpecialSymbols.EOF_SYMBOL_NAME),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2), 0, SpecialSymbols.EOF_SYMBOL_NAME),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3), 0, SpecialSymbols.EOF_SYMBOL_NAME),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 0, "e"),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 0, "c"),
				new StateElement("q", grammar.getNonterminalDefinitions().get("q").getAlternatives().get(0), 0, "c")
			)), state0);
			// You might expect this, but it's circumvented by implicit %reduceOnError: expectSyntaxError(grammarInfo, state0, "b", "d");
			expectReduceOnTerminal(grammarInfo, state0, "c", "q", grammar.getNonterminalDefinitions().get("q").getAlternatives().get(0));

			//
			// check the path through (s ::= a b c)
			//

			State state_a = expectShiftTerminal(grammarInfo, state0, "a");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 1, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_a);
			expectSyntaxError(grammarInfo, state_a, "a", "c", "d", "e", SpecialSymbols.EOF_SYMBOL_NAME, "p", "q");

			State state_a_b = expectShiftTerminal(grammarInfo, state_a, "b");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 2, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_a_b);
			expectSyntaxError(grammarInfo, state_a_b, "a", "b", "d", "e", SpecialSymbols.EOF_SYMBOL_NAME, "p", "q");

			State state_a_b_c = expectShiftTerminal(grammarInfo, state_a_b, "c");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0), 3, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_a_b_c);
			expectSyntaxError(grammarInfo, state_a_b_c, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_a_b_c, SpecialSymbols.EOF_SYMBOL_NAME, "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(0));

			//
			// check the path through (s ::= p p, s ::= p c, p ::= e)
			//

			State state_e = expectShiftTerminal(grammarInfo, state0, "e");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 1, "e"),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 1, "c")
			)), state_e);
			expectSyntaxError(grammarInfo, state_e, "a", "b", "d", SpecialSymbols.EOF_SYMBOL_NAME, "p", "q");
			expectReduceOnTerminal(grammarInfo, state_e, "e", "p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0));
			expectReduceOnTerminal(grammarInfo, state_e, "c", "p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0));

			State state_p = state0.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), "p");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1), 1, SpecialSymbols.EOF_SYMBOL_NAME),
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2), 1, SpecialSymbols.EOF_SYMBOL_NAME),
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 0, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_p);
			expectSyntaxError(grammarInfo, state_p, "a", "b", "d", SpecialSymbols.EOF_SYMBOL_NAME, "q");

			State state_p_c = expectShiftTerminal(grammarInfo, state_p, "c");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2), 2, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_p_c);
			expectSyntaxError(grammarInfo, state_p_c, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_p_c, SpecialSymbols.EOF_SYMBOL_NAME, "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(2));

			State state_p_e = expectShiftTerminal(grammarInfo, state_p, "e");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0), 1, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_p_e);
			expectSyntaxError(grammarInfo, state_p_e, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_p_e, SpecialSymbols.EOF_SYMBOL_NAME, "p", grammar.getNonterminalDefinitions().get("p").getAlternatives().get(0));

			State state_p_p = state_p.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), "p");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1), 2, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_p_p);
			expectSyntaxError(grammarInfo, state_p_p, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_p_p, SpecialSymbols.EOF_SYMBOL_NAME, "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(1));

			//
			// check the path through (s ::= q c)
			//

			State state_q = state0.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), "q");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3), 1, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_q);
			expectSyntaxError(grammarInfo, state_q, "a", "b", "d", "e", SpecialSymbols.EOF_SYMBOL_NAME, "p", "q");

			State state_q_c = expectShiftTerminal(grammarInfo, state_q, "c");
			Assert.assertEquals(new State(ImmutableSet.of(
				new StateElement("s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3), 2, SpecialSymbols.EOF_SYMBOL_NAME)
			)), state_q_c);
			expectSyntaxError(grammarInfo, state_q_c, "a", "b", "c", "d", "e", "p", "q");
			expectReduceOnTerminal(grammarInfo, state_q_c, SpecialSymbols.EOF_SYMBOL_NAME, "s", grammar.getNonterminalDefinitions().get("s").getAlternatives().get(3));

		}

	}

	private static State expectShiftTerminal(GrammarInfo grammarInfo, State state, String terminal) {
		Action action = state.determineActionForTerminalOrEof(grammarInfo, new StateMachineBuildingCache(), terminal);
		Assert.assertTrue(action instanceof Action.Shift);
		return ((Action.Shift) action).getNextState();
	}

	// note: the 'terminal' argument may be EOF for this method
	private static void expectReduceOnTerminal(GrammarInfo grammarInfo, State state, String terminal, String expectedNonterminal, Alternative expectedAlternative) {
		Action action = state.determineActionForTerminalOrEof(grammarInfo, new StateMachineBuildingCache(), terminal);
		Assert.assertTrue(action instanceof Action.Reduce);
		Action.Reduce reduce = (Action.Reduce) action;
		Assert.assertEquals(expectedNonterminal, reduce.getNonterminal());
		Assert.assertEquals(expectedAlternative, reduce.getAlternative());
	}

	// checks terminals and nonterminals
	private static void expectSyntaxError(GrammarInfo grammarInfo, State state, String... symbols) {
		for (String symbol : symbols) {
			Assert.assertNull(state.determineActionForTerminalOrEof(grammarInfo, new StateMachineBuildingCache(), symbol));
			Assert.assertNull(state.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), symbol));
		}
	}

	@Test
	public void testShiftReduceConflict() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder -> builder.addTerminals("PLUS", "MINUS", "TIMES", "NUMBER")
				.createNonterminal("e")
				.addAlternative("NUMBER")
				.addAlternative("e", "PLUS", "e")
				.addAlternative("e", "MINUS", "e")
				.addAlternative("e", "TIMES", "e"),
			"PLUS"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		State state = helper.getRight();

		ExAssert.assertThrows(StateMachineException.ShiftReduceConflict.class, () -> state.determineActionForTerminalOrEof(grammarInfo, new StateMachineBuildingCache(), "PLUS"));
		ExAssert.assertThrows(StateMachineException.ShiftReduceConflict.class, () -> state.determineActionForTerminalOrEof(grammarInfo, new StateMachineBuildingCache(), "MINUS"));
		ExAssert.assertThrows(StateMachineException.ShiftReduceConflict.class, () -> state.determineActionForTerminalOrEof(grammarInfo, new StateMachineBuildingCache(), "TIMES"));

	}

	@Test
	public void testShiftReduceConflictResolvedViaPrecedenceAfterPlus() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.LEFT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.LEFT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"PLUS"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// now reduce on PLUS and MINUS because they're left-associative ...
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1));
		expectReduceOnTerminal(grammarInfo, state, "MINUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1));

		// ... and shift TIMES because it has higher precedence
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "TIMES");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

	}

	@Test
	public void testShiftReduceConflictResolvedViaPrecedenceAfterMinus() {

		// this test works like for PLUS since they have equal precedence, nothing new here

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.LEFT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.LEFT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"MINUS"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// now reduce on PLUS and MINUS because they're left-associative ...
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2));
		expectReduceOnTerminal(grammarInfo, state, "MINUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2));

		// ... and shift TIMES because it has higher precedence
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "TIMES");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

	}

	@Test
	public void testShiftReduceConflictResolvedViaPrecedenceAfterTimes() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.LEFT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.LEFT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"TIMES"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// TIMES has higher precedence and is left-associative, so reduce in all cases
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));
		expectReduceOnTerminal(grammarInfo, state, "MINUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));
		expectReduceOnTerminal(grammarInfo, state, "TIMES", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));

	}

	@Test
	public void testPrecedenceRightAssociativePlusAfterPlus() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.RIGHT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.LEFT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"PLUS"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// this time, shift PLUS and MINUS because they're right-associative...
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "PLUS");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "MINUS");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

		// ... and shift TIMES because it has higher precedence
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "TIMES");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

	}

	@Test
	public void testPrecedenceRightAssociativePlusAfterTimes() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.RIGHT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.LEFT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"TIMES"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// still reduce on PLUS and MINUS because they have lower precedence...
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));
		expectReduceOnTerminal(grammarInfo, state, "MINUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));

		// ... and TIMES is still left-associative
		expectReduceOnTerminal(grammarInfo, state, "TIMES", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));

	}

	@Test
	public void testPrecedenceRightAssociativeTimesAfterPlus() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.LEFT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.RIGHT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"PLUS"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// reduce on PLUS and MINUS because they're left-associative again...
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1));
		expectReduceOnTerminal(grammarInfo, state, "MINUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1));

		// ... and shift TIMES because it has higher precedence
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "TIMES");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

	}

	@Test
	public void testPrecedenceRightAssociativeTimesAfterTimes() {

		Pair<GrammarInfo, State> helper = conflictTestHelper(builder ->
				builder.addTerminals("NUMBER")
					.addTerminals(1, Associativity.LEFT, "PLUS", "MINUS")
					.addTerminals(2, Associativity.RIGHT, "TIMES")
					.createNonterminal("e")
					.addAlternative("NUMBER")
					.addAlternativeWithPrecedence("PLUS", "e", "PLUS", "e")
					.addAlternativeWithPrecedence("MINUS", "e", "MINUS", "e")
					.addAlternativeWithPrecedence("TIMES", "e", "TIMES", "e"),
			"TIMES"
		);
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// reduce on PLUS and MINUS because they have lower precedence...
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));
		expectReduceOnTerminal(grammarInfo, state, "MINUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3));

		// ... and shift TIMES because it is right-associative
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "TIMES");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "MINUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(3), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

	}

	@Test
	public void testConflictResolutionMapAfterPlus() {

		Pair<GrammarInfo, State> helper = conflictResolutionMapTestHelper("PLUS");
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// reduce on PLUS
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(1));

		// ... and shift TIMES
		{
			State actualState2 = expectShiftTerminal(grammarInfo, state, "TIMES");
			State expectedState2 = new StateBuilder(grammarInfo, new StateMachineBuildingCache())
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, SpecialSymbols.EOF_SYMBOL_NAME))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, "PLUS"))
				.addElementClosure(new StateElement("e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2), 2, "TIMES"))
				.build();
			Assert.assertEquals(expectedState2, actualState2);
		}

	}

	@Test
	public void testConflictResolutionMapAfterTimes() {

		Pair<GrammarInfo, State> helper = conflictResolutionMapTestHelper("TIMES");
		GrammarInfo grammarInfo = helper.getLeft();
		Grammar grammar = grammarInfo.getGrammar();
		State state = helper.getRight();

		// reduce on PLUS and on TIMES
		expectReduceOnTerminal(grammarInfo, state, "PLUS", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2));
		expectReduceOnTerminal(grammarInfo, state, "TIMES", "e", grammar.getNonterminalDefinitions().get("e").getAlternatives().get(2));

	}

	private static Pair<GrammarInfo, State> conflictResolutionMapTestHelper(String operatorTerminal) {
		return conflictTestHelper(builder ->
			builder.addTerminals("NUMBER", "PLUS", "TIMES")
				.createNonterminal("e")
				.addAlternative("NUMBER")
				.addAlternativeWithResolutionMap(ImmutableMap.of("PLUS", ConflictResolution.REDUCE, "TIMES", ConflictResolution.SHIFT), "e", "PLUS", "e")
				.addAlternativeWithResolutionMap(ImmutableMap.of("PLUS", ConflictResolution.REDUCE, "TIMES", ConflictResolution.REDUCE), "e", "TIMES", "e"),
			operatorTerminal
		);
	}

	private static Pair<GrammarInfo, State> conflictTestHelper(Consumer<GrammarBuilder> symbolContributor, String operatorTerminal) {
		GrammarBuilder builder = new GrammarBuilder();
		builder.setStartNonterminalName("e");
		symbolContributor.accept(builder);
		Grammar grammar = builder.build();
		GrammarInfo grammarInfo = new GrammarInfo(grammar);
		StateElement startStateElement = new StateElement(SpecialSymbols.ROOT_SYMBOL_NAME, new Alternative("a1", TestUtil.expansion("e"), AlternativeAttributes.EMPTY), 0, SpecialSymbols.EOF_SYMBOL_NAME);
		State state = new StateBuilder(grammarInfo, new StateMachineBuildingCache()).addElementClosure(startStateElement).build();
		state = state.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), "e");
		state = expectShiftTerminal(grammarInfo, state, operatorTerminal);
		state = state.determineNextStateAfterShiftingNonterminal(grammarInfo, new StateMachineBuildingCache(), "e");
		return Pair.of(grammarInfo, state);
	}
}
