package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StateElementTest {

	private static final Alternative ALTERNATIVE_1 = new Alternative(ImmutableList.of("abc", "def", "ghi"), "zzz");

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
		// TODO
	}

	@Test
	public void testToString() {
		// TODO
	}

	@Test
	public void testDetermineActionTypeForTerminalNull() {
		// TODO
	}

	@Test
	public void testDetermineActionTypeForTerminal() {
		// TODO
	}

	@Test
	public void testDetermineNextRootElementForNonterminalNull() {
		// TODO
	}

	@Test
	public void testDetermineNextRootElementForNonterminal() {
		// TODO
	}

	// TODO

}
