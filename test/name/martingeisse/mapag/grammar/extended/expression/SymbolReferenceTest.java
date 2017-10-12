package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SymbolReferenceTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new SymbolReference(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new SymbolReference("");
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertEquals("foo", new SymbolReference("foo").getSymbolName());
	}

}
