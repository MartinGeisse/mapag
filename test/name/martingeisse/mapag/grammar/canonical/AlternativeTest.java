package name.martingeisse.mapag.grammar.canonical;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final Expansion EXPANSION = TestUtil.expansion("foo", "bar", "baz", "foo", "abc");
	private static final Expansion EMPTY_EXPANSION = TestUtil.expansion();

	//
	// constructor calls
	//

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNullConflictResolver() {
		new Alternative("a", null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNonNullConflictResolver() {
		new Alternative("a", null, AlternativeAttributes.EMPTY);
	}

	@Test
	public void testEmptyExpansion() {
		Alternative alternative = new Alternative("a", EMPTY_EXPANSION, AlternativeAttributes.EMPTY);
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(EMPTY_EXPANSION, alternative.getExpansion());
		Assert.assertEquals(AlternativeAttributes.EMPTY, alternative.getAttributes());
	}

	@Test
	public void testNonemptyExpansion() {
		Alternative alternative = new Alternative("a", EXPANSION, AlternativeAttributes.EMPTY);
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertEquals(AlternativeAttributes.EMPTY, alternative.getAttributes());
	}

	@Test
	public void testVanishSymbol() {
		Expansion expectedExpansion = TestUtil.expansion("bar", "baz", "abc");
		Alternative alternative = new Alternative("a", EXPANSION, AlternativeAttributes.EMPTY).vanishSymbol("foo");
		Assert.assertEquals("a", alternative.getName());
		Assert.assertEquals(expectedExpansion, alternative.getExpansion());
		Assert.assertEquals(AlternativeAttributes.EMPTY, alternative.getAttributes());
	}

}
