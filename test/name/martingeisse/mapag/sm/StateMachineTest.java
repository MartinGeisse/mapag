package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.AlternativeAnnotation;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StateMachineTest {

	private static Alternative alternative1 = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
	private static StateElement stateElement1 = new StateElement("aaa", alternative1, 0, "foo");

	private static Alternative alternative2 = new Alternative(ImmutableList.of("foo", "bar"), null, new AlternativeAnnotation("a2", null));
	private static StateElement stateElement2 = new StateElement("bbb", alternative2, 1, "baz");

	private static State state1 = new State(ImmutableSet.of(stateElement1));
	private static State state2 = new State(ImmutableSet.of(stateElement2));
	private static State state3 = new State(ImmutableSet.of(stateElement1, stateElement2));
	private static ImmutableSet<State> states = ImmutableSet.of(state1, state2, state3);

	private static ImmutableMap<State, ImmutableMap<String, Action>> terminalActions = ImmutableMap.of(
		state1, ImmutableMap.of("abc", new Action.Shift(state2)),
		state2, ImmutableMap.of("abc", new Action.Shift(state3)),
		state3, ImmutableMap.of("abc", new Action.Shift(state1), "def", new Action.Reduce("zzz", alternative1))
	);
	private static ImmutableMap<State, ImmutableMap<String, Action.Shift>> nonterminalActions = ImmutableMap.of(
		state1, ImmutableMap.of("nt1", new Action.Shift(state1))
	);

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullStates() {
		StateMachine stateMachine = new StateMachine(null, terminalActions, nonterminalActions, state3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmptyStates() {
		StateMachine stateMachine = new StateMachine(ImmutableSet.of(), terminalActions, nonterminalActions, state3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullTerminalActions() {
		StateMachine stateMachine = new StateMachine(states, null, nonterminalActions, state3);
	}

	@Test
	public void testConstructorEmptyTerminalActionsAllowed() {
		StateMachine stateMachine = new StateMachine(states, ImmutableMap.of(), nonterminalActions, state3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullNonterminalActions() {
		StateMachine stateMachine = new StateMachine(states, terminalActions, null, state3);
	}

	@Test
	public void testConstructorEmptyNonterminalActionsAllowed() {
		StateMachine stateMachine = new StateMachine(states, terminalActions, ImmutableMap.of(), state3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullStartState() {
		StateMachine stateMachine = new StateMachine(states, terminalActions, nonterminalActions, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorUnknownStartState() {
		Alternative alternativeX = new Alternative(ImmutableList.of("foo", "bar"), null, new AlternativeAnnotation("a", null));
		StateElement stateElementX = new StateElement("bbb", alternativeX, 1, "baz");
		State stateX = new State(ImmutableSet.of(stateElementX));
		new StateMachine(states, terminalActions, nonterminalActions, stateX);
	}

	@Test
	public void testConstructorGetter() {
		StateMachine stateMachine = new StateMachine(states, terminalActions, nonterminalActions, state3);
		Assert.assertEquals(states, stateMachine.getStates());
		Assert.assertEquals(terminalActions, stateMachine.getTerminalOrEofActions());
		Assert.assertEquals(nonterminalActions, stateMachine.getNonterminalActions());
		Assert.assertEquals(state3, stateMachine.getStartState());
	}

}
