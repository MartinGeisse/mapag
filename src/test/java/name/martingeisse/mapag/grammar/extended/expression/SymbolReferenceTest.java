package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.grammar.SpecialSymbols;
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

	@Test
	public void testWithErrorSymbol() {
		Assert.assertEquals(SpecialSymbols.ERROR_SYMBOL_NAME, new SymbolReference(SpecialSymbols.ERROR_SYMBOL_NAME).getSymbolName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEofSymbolNotAllowed() {
		new SymbolReference(SpecialSymbols.EOF_SYMBOL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRootSymbolNotAllowed() {
		new SymbolReference(SpecialSymbols.ROOT_SYMBOL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBadCharacterSymbolNotAllowed() {
		new SymbolReference(SpecialSymbols.BAD_CHARACTER_SYMBOL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOtherSpecialSymbolNameNotAllowed() {
		new SymbolReference("%whatever");
	}

	@Test
	public void testWithName() {
		SymbolReference expression = (SymbolReference) (new SymbolReference("foo").withName("myName"));
		Assert.assertEquals("myName", expression.getName());
		Assert.assertSame("foo", expression.getSymbolName());
	}

}
