package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
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

	@DataProvider
	public static Object[][] getValidAlternatives() {
		return new Object[][]{
				{new Alternative(ImmutableList.of(), null)},
				{new Alternative(ImmutableList.of(), "xyz")},
				{new Alternative(EXPANSION, null)},
				{new Alternative(EXPANSION, "xyz")},
		};
	}

	@Test(expected = IllegalArgumentException.class)
	@UseDataProvider("getValidAlternatives")
	public void testVanishSymbolNull(Alternative alternative) {
		alternative.vanishSymbol(null);
	}

	@Test(expected = IllegalArgumentException.class)
	@UseDataProvider("getValidAlternatives")
	public void testVanishSymbolEmpty(Alternative alternative) {
		alternative.vanishSymbol("");
	}

	@Test
	public void testVanishSymbolInEmptyAlternative() {
		Alternative alternative = new Alternative(ImmutableList.of(), "foo");
		Assert.assertTrue(alternative.vanishSymbol("foo").getExpansion().isEmpty());
		Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("foo"));
		Assert.assertTrue(alternative.vanishSymbol("bar").getExpansion().isEmpty());
		Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));
	}

	@Test
	public void testVanishSymbol() {
		{
			Alternative alternative = new Alternative(EXPANSION, "foo");

			Assert.assertEquals(ImmutableList.of("bar", "baz", "abc"), alternative.vanishSymbol("foo").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("foo"));

			Assert.assertEquals(ImmutableList.of("foo", "baz", "foo", "abc"), alternative.vanishSymbol("bar").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));

			Assert.assertEquals(ImmutableList.of(EXPANSION), alternative.vanishSymbol("xyz").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("xyz").getEffectivePrecedenceTerminal().equals("foo"));
		}
		{
			Alternative alternative = new Alternative(EXPANSION, "xyz");

			Assert.assertEquals(ImmutableList.of("bar", "baz", "abc"), alternative.vanishSymbol("foo").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("xyz"));

			Assert.assertEquals(ImmutableList.of("foo", "baz", "foo", "abc"), alternative.vanishSymbol("bar").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("xyz"));

			Assert.assertEquals(ImmutableList.of(EXPANSION), alternative.vanishSymbol("xyz").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("xyz").getEffectivePrecedenceTerminal().equals("xyz"));
		}
	}

	@Test
	public void testVanishSymbolToEmptyAlternative() {
		ImmutableList<String> input = ImmutableList.of("bar", "bar");
		Assert.assertEquals(0, new Alternative(input, "foo").vanishSymbol("bar").getExpansion().size());
		Assert.assertEquals(0, new Alternative(input, "foo").vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));
		Assert.assertEquals(0, new Alternative(input, "bar").vanishSymbol("bar").getExpansion().size());
		Assert.assertEquals(0, new Alternative(input, "bar").vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("bar"));
	}

}
