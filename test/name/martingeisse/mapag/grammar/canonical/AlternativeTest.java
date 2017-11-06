package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class AlternativeTest {

	private static final ImmutableList<String> EXPANSION = ImmutableList.of("foo", "bar", "baz", "foo", "abc");
	private static final ImmutableList<String> EXPANSION_WITH_EMPTY = ImmutableList.of("foo", "", "bar");

	//
	// constructor calls
	//

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNullPrecedence() {
		new Alternative(null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNonNullPrecedence() {
		new Alternative(null, null, "xyz");
	}

	@Test
	public void testEmptyExpansionNullPrecedence() {
		Alternative alternative = new Alternative(null, ImmutableList.of(), null);
		Assert.assertEquals(ImmutableList.of(), alternative.getExpansion());
		Assert.assertNull(alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyExpansionEmptyPrecedence() {
		new Alternative(null, ImmutableList.of(), "");
	}

	@Test
	public void testEmptyExpansionNonNullPrecedence() {
		Alternative alternative = new Alternative(null, ImmutableList.of(), "xyz");
		Assert.assertEquals(ImmutableList.of(), alternative.getExpansion());
		Assert.assertEquals("xyz", alternative.getEffectivePrecedenceTerminal());
	}

	@Test
	public void testNonemptyExpansionNullPrecedence() {
		Alternative alternative = new Alternative(null, EXPANSION, null);
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertNull(alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonemptyExpansionEmptyPrecedence() {
		new Alternative(null, EXPANSION, "");
	}

	@Test
	public void testNonemptyExpansionNonNullPrecedence() {
		Alternative alternative = new Alternative(null, EXPANSION, "xyz");
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertEquals("xyz", alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExpansionContainsEmptyWithNullPrecedence() {
		new Alternative(null, EXPANSION_WITH_EMPTY, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExpansionContainsEmptyWithNonNullPrecedence() {
		new Alternative(null, EXPANSION_WITH_EMPTY, "xyz");
	}


	//
	// methods
	//

	@DataProvider
	public static Object[][] getValidAlternatives() {
		return new Object[][]{
				{new Alternative(null, ImmutableList.of(), null)},
				{new Alternative(null, ImmutableList.of(), "xyz")},
				{new Alternative(null, EXPANSION, null)},
				{new Alternative(null, EXPANSION, "xyz")},
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
		Alternative alternative = new Alternative(null, ImmutableList.of(), "foo");
		Assert.assertTrue(alternative.vanishSymbol("foo").getExpansion().isEmpty());
		Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("foo"));
		Assert.assertTrue(alternative.vanishSymbol("bar").getExpansion().isEmpty());
		Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));
	}

	@Test
	public void testVanishSymbol() {
		{
			Alternative alternative = new Alternative(null, EXPANSION, "foo");

			Assert.assertEquals(ImmutableList.of("bar", "baz", "abc"), alternative.vanishSymbol("foo").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("foo"));

			Assert.assertEquals(ImmutableList.of("foo", "baz", "foo", "abc"), alternative.vanishSymbol("bar").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));

			Assert.assertEquals(EXPANSION, alternative.vanishSymbol("xyz").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("xyz").getEffectivePrecedenceTerminal().equals("foo"));
		}
		{
			Alternative alternative = new Alternative(null, EXPANSION, "xyz");

			Assert.assertEquals(ImmutableList.of("bar", "baz", "abc"), alternative.vanishSymbol("foo").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("xyz"));

			Assert.assertEquals(ImmutableList.of("foo", "baz", "foo", "abc"), alternative.vanishSymbol("bar").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("xyz"));

			Assert.assertEquals(EXPANSION, alternative.vanishSymbol("xyz").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("xyz").getEffectivePrecedenceTerminal().equals("xyz"));
		}
	}

	@Test
	public void testVanishSymbolToEmptyAlternative() {
		ImmutableList<String> input = ImmutableList.of("bar", "bar");
		Assert.assertEquals(0, new Alternative(null, input, "foo").vanishSymbol("bar").getExpansion().size());
		Assert.assertEquals("foo", new Alternative(null, input, "foo").vanishSymbol("bar").getEffectivePrecedenceTerminal());
		Assert.assertEquals(0, new Alternative(null, input, "bar").vanishSymbol("bar").getExpansion().size());
		Assert.assertEquals("bar", new Alternative(null, input, "bar").vanishSymbol("bar").getEffectivePrecedenceTerminal());
	}

}
