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

	private static final Expansion EXPANSION = TestUtil.expansion("foo", "bar", "baz", "foo", "abc");

	//
	// constructor calls
	//

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNullPrecedence() {
		new Alternative(null, null, new AlternativeAnnotation("a1", null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansionNonNullPrecedence() {
		new Alternative(null, "xyz", new AlternativeAnnotation("a1", null));
	}

	@Test
	public void testEmptyExpansionNullPrecedence() {
		Alternative alternative = new Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null));
		Assert.assertEquals(ImmutableList.of(), alternative.getExpansion());
		Assert.assertNull(alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyExpansionEmptyPrecedence() {
		new Alternative(ImmutableList.of(), "", new AlternativeAnnotation("a1", null));
	}

	@Test
	public void testEmptyExpansionNonNullPrecedence() {
		Alternative alternative = new Alternative(ImmutableList.of(), "xyz", new AlternativeAnnotation("a1", null));
		Assert.assertEquals(ImmutableList.of(), alternative.getExpansion());
		Assert.assertEquals("xyz", alternative.getEffectivePrecedenceTerminal());
	}

	@Test
	public void testNonemptyExpansionNullPrecedence() {
		Alternative alternative = new Alternative(EXPANSION, null, new AlternativeAnnotation("a1", null));
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertNull(alternative.getEffectivePrecedenceTerminal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonemptyExpansionEmptyPrecedence() {
		new Alternative(EXPANSION, "", new AlternativeAnnotation("a1", null));
	}

	@Test
	public void testNonemptyExpansionNonNullPrecedence() {
		Alternative alternative = new Alternative(EXPANSION, "xyz", new AlternativeAnnotation("a1", null));
		Assert.assertEquals(EXPANSION, alternative.getExpansion());
		Assert.assertEquals("xyz", alternative.getEffectivePrecedenceTerminal());
	}



	//
	// methods
	//

	@DataProvider
	public static Object[][] getValidAlternatives() {
		return new Object[][]{
				{new Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null))},
				{new Alternative(ImmutableList.of(), "xyz", new AlternativeAnnotation("a1", null))},
				{new Alternative(EXPANSION, null, new AlternativeAnnotation("a1", null))},
				{new Alternative(EXPANSION, "xyz", new AlternativeAnnotation("a1", null))},
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
		Alternative alternative = new Alternative(ImmutableList.of(), "foo", new AlternativeAnnotation("a1", null));
		Assert.assertTrue(alternative.vanishSymbol("foo").getExpansion().isEmpty());
		Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("foo"));
		Assert.assertTrue(alternative.vanishSymbol("bar").getExpansion().isEmpty());
		Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));
	}

	@Test
	public void testVanishSymbol() {
		{
			Alternative alternative = new Alternative(EXPANSION, "foo", new AlternativeAnnotation("a1", null));

			Assert.assertEquals(ImmutableList.of("bar", "baz", "abc"), alternative.vanishSymbol("foo").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("foo").getEffectivePrecedenceTerminal().equals("foo"));

			Assert.assertEquals(ImmutableList.of("foo", "baz", "foo", "abc"), alternative.vanishSymbol("bar").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("bar").getEffectivePrecedenceTerminal().equals("foo"));

			Assert.assertEquals(EXPANSION, alternative.vanishSymbol("xyz").getExpansion());
			Assert.assertTrue(alternative.vanishSymbol("xyz").getEffectivePrecedenceTerminal().equals("foo"));
		}
		{
			Alternative alternative = new Alternative(EXPANSION, "xyz", new AlternativeAnnotation("a1", null));

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
		Assert.assertEquals(0, new Alternative(input, "foo", new AlternativeAnnotation("a1", null)).vanishSymbol("bar").getExpansion().size());
		Assert.assertEquals("foo", new Alternative(input, "foo", new AlternativeAnnotation("a1", null)).vanishSymbol("bar").getEffectivePrecedenceTerminal());
		Assert.assertEquals(0, new Alternative(input, "bar", new AlternativeAnnotation("a1", null)).vanishSymbol("bar").getExpansion().size());
		Assert.assertEquals("bar", new Alternative(input, "bar", new AlternativeAnnotation("a1", null)).vanishSymbol("bar").getEffectivePrecedenceTerminal());
	}

}
