package name.martingeisse.mapag.grammar.canonical;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final Expansion EXPANSION = TestUtil.expansion("foo", "bar", "baz", "foo", "abc");
	private static final Expansion EMPTY_EXPANSION = TestUtil.expansion();
	private static final AlternativeConflictResolver CONFLICT_RESOLVER = new AlternativeConflictResolver("foo", null);

	//
	// constructor calls
	//

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNullConflictResolver() {
		new Alternative("a", null, null, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNonNullConflictResolver() {
		new Alternative("a", null, CONFLICT_RESOLVER, false);
	}

	@Test
	public void testEmptyExpansionNullConflictResolver() {
		Alternative alternative = new Alternative("a", EMPTY_EXPANSION, null, false);
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(EMPTY_EXPANSION, alternative.getExpansion());
		Assert.assertNull(alternative.getConflictResolver());
	}

	@Test
	public void testEmptyExpansionNonNullConflictResolver() {
		Alternative alternative = new Alternative("a", EMPTY_EXPANSION, CONFLICT_RESOLVER, false);
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(EMPTY_EXPANSION, alternative.getExpansion());
		Assert.assertEquals(CONFLICT_RESOLVER, alternative.getConflictResolver());
	}

	@Test
	public void testNonemptyExpansionNullConflictResolver() {
		Alternative alternative = new Alternative("a", EXPANSION, null, false);
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertNull(alternative.getConflictResolver());
	}

	@Test
	public void testNonemptyExpansionNonNullConflictResolver() {
		Alternative alternative = new Alternative("a", EXPANSION, CONFLICT_RESOLVER, false);
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertEquals(CONFLICT_RESOLVER, alternative.getConflictResolver());
	}

	@Test
	public void testVanishSymbol() {
		Expansion expectedExpansion = TestUtil.expansion("bar", "baz", "abc");
		Alternative alternative = new Alternative("a", EXPANSION, CONFLICT_RESOLVER, false).vanishSymbol("foo");
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(expectedExpansion, alternative.getExpansion());
		Assert.assertEquals(CONFLICT_RESOLVER, alternative.getConflictResolver());
	}

}
