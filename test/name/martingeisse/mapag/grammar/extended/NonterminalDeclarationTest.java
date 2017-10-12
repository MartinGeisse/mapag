package name.martingeisse.mapag.grammar.extended;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class NonterminalDeclarationTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new NonterminalDeclaration(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new NonterminalDeclaration("");
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertEquals("foo", new NonterminalDeclaration("foo").getName());
	}

}
