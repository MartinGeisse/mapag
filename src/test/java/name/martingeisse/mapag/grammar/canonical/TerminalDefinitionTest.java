package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TerminalDefinitionTest {

	@Test
	public void testConstructorGetter() {
		TerminalDefinition terminalDefinition = new TerminalDefinition("foo", 5, Associativity.LEFT);
		Assert.assertEquals("foo", terminalDefinition.getName());
		Assert.assertEquals(5, terminalDefinition.getPrecedenceIndex().intValue());
		Assert.assertEquals(Associativity.LEFT, terminalDefinition.getAssociativity());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new TerminalDefinition(null, 5, Associativity.LEFT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new TerminalDefinition("", 5, Associativity.LEFT);
	}

	@Test
	public void testNullPrecedenceIndexNonassoc() {
		TerminalDefinition terminalDefinition = new TerminalDefinition("foo", null, Associativity.NONASSOC);
		Assert.assertEquals("foo", terminalDefinition.getName());
		Assert.assertNull(terminalDefinition.getPrecedenceIndex());
		Assert.assertEquals(Associativity.NONASSOC, terminalDefinition.getAssociativity());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPrecedenceIndexLeftAssoc() {
		new TerminalDefinition("foo", null, Associativity.LEFT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAssociativity() {
		new TerminalDefinition("foo", 5, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPrecedenceAndAssociativity() {
		new TerminalDefinition("foo", null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPercentSignInNameNotAllowed() {
		new TerminalDefinition("%foo", 5, Associativity.LEFT);
	}
}
