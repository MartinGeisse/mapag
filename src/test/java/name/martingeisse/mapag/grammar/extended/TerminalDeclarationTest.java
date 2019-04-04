package name.martingeisse.mapag.grammar.extended;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TerminalDeclarationTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new TerminalDeclaration(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new TerminalDeclaration("");
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertEquals("foo", new TerminalDeclaration("foo").getName());
	}

}
