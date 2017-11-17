package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final SymbolReference SYMBOL_REFERENCE = new SymbolReference("foo");

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpressionWithoutPrecedence() {
		new Alternative(null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpressionWithPrecedence() {
		new Alternative(null, null, "abc");
	}

	@Test
	public void testNullPrecedence() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null);
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPrecedence() {
		new Alternative(null, SYMBOL_REFERENCE, "");
	}

	@Test
	public void testSingleParameterConstructorGetter() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, "abc");
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals("abc", alternative.getPrecedenceDefiningTerminal());
	}

}
