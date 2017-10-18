package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final ImmutableList<String> EXPANSION = ImmutableList.of("foo", "bar", "baz", "foo", "abc");
	private static final ImmutableList<String> EXPANSION_WITH_EMPTY = ImmutableList.of("foo", "", "bar");

	//
	// constructor calls
	//

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNullPrecedence() {
		new Alternative(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNonNullPrecedence() {
		new Alternative(null, "xyz");
	}

	@Test
	public void testEmptyExpansionNullPrecedence() {
		Alternative alternative = new Alternative(ImmutableList.of(), null);
		Assert.assertEquals(ImmutableList.of(), alternative.getExpansion());
		Assert.assertNull(alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyExpansionEmptyPrecedence() {
		new Alternative(ImmutableList.of(), "");
	}

	@Test
	public void testEmptyExpansionNonNullPrecedence() {
		Alternative alternative = new Alternative(ImmutableList.of(), "xyz");
		Assert.assertEquals(ImmutableList.of(), alternative.getExpansion());
		Assert.assertEquals("xyz", alternative.getEffectivePrecedenceTerminal());
	}

	@Test
	public void testNonemptyExpansionNullPrecedence() {
		Alternative alternative = new Alternative(EXPANSION, null);
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertNull(alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonemptyExpansionEmptyPrecedence() {
		new Alternative(EXPANSION, "");
	}

	@Test
	public void testNonemptyExpansionNonNullPrecedence() {
		Alternative alternative = new Alternative(EXPANSION, "xyz");
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertEquals("xyz", alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExpansionContainsEmptyWithNullPrecedence() {
		new Alternative(EXPANSION_WITH_EMPTY, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExpansionContainsEmptyWithNonNullPrecedence() {
		new Alternative(EXPANSION_WITH_EMPTY, "xyz");
	}


	//
	// methods
	//

	// TODO

//	@DataProvider
//	public static Object[][] getValidAlternatives() {
//		return new Object[][]{
//				{new Alternative(ImmutableList.of(), null)},
//				{new Alternative(ImmutableList.of(), "xyz")},
//				{new Alternative(EXPANSION, null)},
//				{new Alternative(EXPANSION, "xyz")},
//		};
//	}
//
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testVanishSymbolNullWithNullPrecedence() {
//		new Alternative(EXPANSION, null).vanishSymbol(null);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testVanishSymbolNullWithNonNullPrecedence() {
//		new Alternative(EXPANSION, "xyz").vanishSymbol(null);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testVanishSymbolNullInEmptyAlternativeWithNullPrecedence() {
//		new Alternative(ImmutableList.of(), null).vanishSymbol(null);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testVanishSymbolNullInEmptyAlternativeWithNonNullPrecedence() {
//		new Alternative(ImmutableList.of(), "xyz").vanishSymbol(null);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testVanishSymbolEmptyWithNullPrecedence() {
//		new Alternative(EXPANSION).vanishSymbol("");
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testVanishSymbolEmptyInEmptyAlternative() {
//		new Alternative(ImmutableList.of()).vanishSymbol("");
//	}
//
//	@Test
//	public void testVanishSymbolInEmptyAlternative() {
//		Assert.assertTrue(new Alternative(ImmutableList.of()).vanishSymbol("foo").getExpansion().isEmpty());
//	}
//
//	@Test
//	public void testVanishSymbol() {
//		ImmutableList<String> expected = ImmutableList.of("bar", "baz", "abc");
//		Assert.assertEquals(expected, new Alternative(EXPANSION).vanishSymbol("foo").getExpansion());
//	}
//
//	@Test
//	public void testVanishSymbolToEmptyAlternative() {
//		ImmutableList<String> input = ImmutableList.of("bar", "bar");
//		Assert.assertEquals(0, new Alternative(input).vanishSymbol("bar").getExpansion().size());
//	}

}
