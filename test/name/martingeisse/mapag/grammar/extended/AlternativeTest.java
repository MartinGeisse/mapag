package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
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
		new Alternative(null, null, null, null, false, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpressionWithPrecedence() {
		new Alternative(null, null, "abc", null, false, false);
	}

	@Test
	public void testNullPrecedenceNullResolveBlock() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, null, false, false);
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPrecedence() {
		new Alternative(null, SYMBOL_REFERENCE, "", null, false, false);
	}

	@Test
	public void testConstructorGetterWithoutResolution() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, null, false, false);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
		Assert.assertNull(alternative.getResolveBlock());
		Assert.assertFalse(alternative.isReduceOnError());
		Assert.assertFalse(alternative.isReduceOnEofOnly());
	}

	@Test
	public void testConstructorGetterWithPrecedence() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, "abc", null, false, false);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals("abc", alternative.getPrecedenceDefiningTerminal());
		Assert.assertNull(alternative.getResolveBlock());
		Assert.assertFalse(alternative.isReduceOnError());
		Assert.assertFalse(alternative.isReduceOnEofOnly());
	}

	@Test
	public void testConstructorGetterWithResolveBlock() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of());
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, resolveBlock, false, false);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
		Assert.assertSame(resolveBlock, alternative.getResolveBlock());
		Assert.assertFalse(alternative.isReduceOnError());
		Assert.assertFalse(alternative.isReduceOnEofOnly());
	}

	@Test
	public void testConstructorGetterWithReduceOnError() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, null, true, false);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
		Assert.assertNull(alternative.getResolveBlock());
		Assert.assertTrue(alternative.isReduceOnError());
		Assert.assertFalse(alternative.isReduceOnEofOnly());
	}

	@Test
	public void testConstructorGetterWithReduceOnEofOnly() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, null, false, true);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
		Assert.assertNull(alternative.getResolveBlock());
		Assert.assertFalse(alternative.isReduceOnError());
		Assert.assertTrue(alternative.isReduceOnEofOnly());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithPrecedenceAndResolveBlock() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of());
		new Alternative(null, SYMBOL_REFERENCE, "foo", resolveBlock, false, false);
	}

	@Test
	public void testName() {
		Alternative alternative = new Alternative("foo", SYMBOL_REFERENCE, null, null, false, false);
		Assert.assertEquals("foo", alternative.getName());
	}

}
