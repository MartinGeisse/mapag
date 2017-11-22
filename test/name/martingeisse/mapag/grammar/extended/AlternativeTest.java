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
		new Alternative(null, null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpressionWithPrecedence() {
		new Alternative(null, null, "abc", null);
	}

	@Test
	public void testNullPrecedenceNullResolveBlock() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, null);
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPrecedence() {
		new Alternative(null, SYMBOL_REFERENCE, "", null);
	}

	@Test
	public void testConstructorGetterWithoutResolution() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, null);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
		Assert.assertNull(alternative.getResolveBlock());
	}

	@Test
	public void testConstructorGetterWithPrecedence() {
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, "abc", null);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals("abc", alternative.getPrecedenceDefiningTerminal());
		Assert.assertNull(alternative.getResolveBlock());
	}

	@Test
	public void testConstructorGetterWithResolveBlock() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of());
		Alternative alternative = new Alternative(null, SYMBOL_REFERENCE, null, resolveBlock);
		Assert.assertNull(alternative.getName());
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertNull(alternative.getPrecedenceDefiningTerminal());
		Assert.assertSame(resolveBlock, alternative.getResolveBlock());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithPrecedenceAndResolveBlock() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of());
		new Alternative(null, SYMBOL_REFERENCE, "foo", resolveBlock);
	}

	@Test
	public void testName() {
		Alternative alternative = new Alternative("foo", SYMBOL_REFERENCE, null, null);
		Assert.assertEquals("foo", alternative.getName());
	}

}
