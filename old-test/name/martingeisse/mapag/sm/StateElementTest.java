package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.AlternativeAnnotation;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StateElementTest {

	private static final Alternative ALTERNATIVE_1 = new Alternative(ImmutableList.of("abc", "def", "ghi"), "zzz", new AlternativeAnnotation("a1", null));

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullLeftSide() {
		new StateElement(null, ALTERNATIVE_1, 1, "bar");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullAlternative() {
		new StateElement("nt1", null, 1, "bar");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNegativePosition() {
		new StateElement("nt1", ALTERNATIVE_1, -1, "bar");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorPositionBeyondEnd() {
		new StateElement("nt1", ALTERNATIVE_1, 4, "bar");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullFollowTerminal() {
		new StateElement("nt1", ALTERNATIVE_1, 1, null);
	}

	@Test
	public void testConstructorGetter() {
		StateElement stateElement = new StateElement("nt1", ALTERNATIVE_1, 1, "bar");
		Assert.assertEquals("nt1", stateElement.getLeftSide());
		Assert.assertSame(ALTERNATIVE_1, stateElement.getAlternative());
		Assert.assertEquals(1, stateElement.getPosition());
		Assert.assertEquals("bar", stateElement.getFollowTerminal());
	}

	@Test
	public void testIsAtEnd() {
		Assert.assertFalse(new StateElement("nt1", ALTERNATIVE_1, 0, "bar").isAtEnd());
		Assert.assertFalse(new StateElement("nt1", ALTERNATIVE_1, 1, "bar").isAtEnd());
		Assert.assertFalse(new StateElement("nt1", ALTERNATIVE_1, 2, "bar").isAtEnd());
		Assert.assertTrue(new StateElement("nt1", ALTERNATIVE_1, 3, "bar").isAtEnd());
	}

	@Test
	public void testGetNextSymbol() {
		Assert.assertEquals("abc", new StateElement("nt1", ALTERNATIVE_1, 0, "bar").getNextSymbol());
		Assert.assertEquals("def", new StateElement("nt1", ALTERNATIVE_1, 1, "bar").getNextSymbol());
		Assert.assertEquals("ghi", new StateElement("nt1", ALTERNATIVE_1, 2, "bar").getNextSymbol());
		Assert.assertEquals("bar", new StateElement("nt1", ALTERNATIVE_1, 3, "bar").getNextSymbol());
	}

	@Test
	public void testEqualsAndHashCode() {

		// compare two equal states
		{
			Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
			StateElement se1 = new StateElement("lll", alternative, 0, "foo");
			StateElement se2 = new StateElement("lll", alternative, 0, "foo");
			Assert.assertEquals(se1, se2);
			Assert.assertEquals(se1.hashCode(), se2.hashCode());
		}

		// state elements with "equal" but distinct alternatives are different
		{
			Alternative a1 = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
			StateElement se1 = new StateElement("lll", a1, 0, "foo");
			Alternative a2 = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a2", null));
			StateElement se2 = new StateElement("lll", a2, 0, "foo");
			Assert.assertNotEquals(se1, se2);
		}

		// other cases
		{
			Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
			StateElement[] stateElements = {
				new StateElement("lll", alternative, 0, "foo"),
				new StateElement("aaa", alternative, 0, "foo"),
				new StateElement("lll", alternative, 1, "foo"),
				new StateElement("lll", alternative, 0, "bar")
			};
			for (int i=0; i<stateElements.length; i++) {
				for (int j=0; j<stateElements.length; j++) {
					if (i == j) {
						Assert.assertEquals(stateElements[i], stateElements[j]);
						Assert.assertEquals(stateElements[i].hashCode(), stateElements[j].hashCode());
					} else {
						Assert.assertNotEquals(stateElements[i], stateElements[j]);
					}
				}
			}
		}

	}

	@Test
	public void testToString() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement1 = new StateElement("lll", alternative, 0, "foo");
		Assert.assertEquals("lll ::= . r1 r2 r3    [foo]", stateElement1.toString());
		StateElement stateElement2 = new StateElement("lll", alternative, 1, "foo");
		Assert.assertEquals("lll ::= r1 . r2 r3    [foo]", stateElement2.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDetermineActionTypeForTerminalNull() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement = new StateElement("lll", alternative, 0, "foo");
		stateElement.determineActionTypeForTerminal(null);
	}

	@Test
	public void testDetermineActionTypeForTerminal() {

		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement = new StateElement("lll", alternative, 0, "foo");
		Assert.assertEquals(StateElement.ActionType.SHIFT, stateElement.determineActionTypeForTerminal("r1"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r2"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r3"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("prec1"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("foo"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("bar"));

		stateElement = stateElement.getShifted();
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r1"));
		Assert.assertEquals(StateElement.ActionType.SHIFT, stateElement.determineActionTypeForTerminal("r2"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r3"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("prec1"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("foo"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("bar"));

		stateElement = stateElement.getShifted();
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r1"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r2"));
		Assert.assertEquals(StateElement.ActionType.SHIFT, stateElement.determineActionTypeForTerminal("r3"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("prec1"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("foo"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("bar"));

		stateElement = stateElement.getShifted();
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r1"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r2"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("r3"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("prec1"));
		Assert.assertEquals(StateElement.ActionType.REDUCE, stateElement.determineActionTypeForTerminal("foo"));
		Assert.assertEquals(StateElement.ActionType.DROP_ELEMENT, stateElement.determineActionTypeForTerminal("bar"));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testDetermineNextRootElementForNonterminalNull() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement = new StateElement("lll", alternative, 0, "foo");
		stateElement.determineNextRootElementForNonterminal(null);
	}

	@Test
	public void testDetermineNextRootElementForNonterminal() {

		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement0 = new StateElement("lll", alternative, 0, "foo");
		StateElement stateElement1 = new StateElement("lll", alternative, 1, "foo");
		StateElement stateElement2 = new StateElement("lll", alternative, 2, "foo");
		StateElement stateElement3 = new StateElement("lll", alternative, 3, "foo");

		Assert.assertEquals(stateElement1, stateElement0.determineNextRootElementForNonterminal("r1"));
		Assert.assertNull(stateElement0.determineNextRootElementForNonterminal("r2"));
		Assert.assertNull(stateElement0.determineNextRootElementForNonterminal("r3"));
		Assert.assertNull(stateElement0.determineNextRootElementForNonterminal("prec1"));
		Assert.assertNull(stateElement0.determineNextRootElementForNonterminal("foo"));
		Assert.assertNull(stateElement0.determineNextRootElementForNonterminal("bar"));

		Assert.assertNull(stateElement1.determineNextRootElementForNonterminal("r1"));
		Assert.assertEquals(stateElement2, stateElement1.determineNextRootElementForNonterminal("r2"));
		Assert.assertNull(stateElement1.determineNextRootElementForNonterminal("r3"));
		Assert.assertNull(stateElement1.determineNextRootElementForNonterminal("prec1"));
		Assert.assertNull(stateElement1.determineNextRootElementForNonterminal("foo"));
		Assert.assertNull(stateElement1.determineNextRootElementForNonterminal("bar"));

		Assert.assertNull(stateElement2.determineNextRootElementForNonterminal("r1"));
		Assert.assertNull(stateElement2.determineNextRootElementForNonterminal("r2"));
		Assert.assertEquals(stateElement3, stateElement2.determineNextRootElementForNonterminal("r3"));
		Assert.assertNull(stateElement2.determineNextRootElementForNonterminal("prec1"));
		Assert.assertNull(stateElement2.determineNextRootElementForNonterminal("foo"));
		Assert.assertNull(stateElement2.determineNextRootElementForNonterminal("bar"));

		Assert.assertNull(stateElement3.determineNextRootElementForNonterminal("r1"));
		Assert.assertNull(stateElement3.determineNextRootElementForNonterminal("r2"));
		Assert.assertNull(stateElement3.determineNextRootElementForNonterminal("r3"));
		Assert.assertNull(stateElement3.determineNextRootElementForNonterminal("prec1"));
		Assert.assertNull(stateElement3.determineNextRootElementForNonterminal("foo"));
		Assert.assertNull(stateElement3.determineNextRootElementForNonterminal("bar"));

	}

	@Test
	public void testGetShifted() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement0 = new StateElement("lll", alternative, 0, "foo");
		StateElement stateElement1 = stateElement0.getShifted();
		Assert.assertEquals(new StateElement("lll", alternative, 1, "foo"), stateElement1);
		StateElement stateElement2 = stateElement1.getShifted();
		Assert.assertEquals(new StateElement("lll", alternative, 2, "foo"), stateElement2);
		StateElement stateElement3 = stateElement2.getShifted();
		Assert.assertEquals(new StateElement("lll", alternative, 3, "foo"), stateElement3);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetShiftedAtEnd() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1", new AlternativeAnnotation("a1", null));
		StateElement stateElement = new StateElement("lll", alternative, 3, "foo");
		stateElement.getShifted();
	}

}
