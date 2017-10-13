package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final ImmutableList<String> EXPANSION = ImmutableList.of("foo", "bar", "baz", "foo", "abc");

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansion() {
		new Alternative(null);
	}

	@Test
	public void testEmptyExpansion() {
		new Alternative(ImmutableList.of());
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertEquals(EXPANSION, new Alternative(EXPANSION).getExpansion());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVanishSymbolNull() {
		new Alternative(EXPANSION).vanishSymbol(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVanishSymbolNullInEmptyAlternative() {
		new Alternative(ImmutableList.of()).vanishSymbol(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVanishSymbolEmpty() {
		new Alternative(EXPANSION).vanishSymbol("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVanishSymbolEmptyInEmptyAlternative() {
		new Alternative(ImmutableList.of()).vanishSymbol("");
	}

	@Test
	public void testVanishSymbolInEmptyAlternative() {
		Assert.assertTrue(new Alternative(ImmutableList.of()).vanishSymbol("foo").getExpansion().isEmpty());
	}

	@Test
	public void testVanishSymbol() {
		ImmutableList<String> expected = ImmutableList.of("bar", "baz", "abc");
		Assert.assertEquals(expected, new Alternative(EXPANSION).vanishSymbol("foo").getExpansion());
	}

	@Test
	public void testVanishSymbolToEmptyAlternative() {
		ImmutableList<String> input = ImmutableList.of("bar", "bar");
		Assert.assertEquals(0, new Alternative(input).vanishSymbol("bar").getExpansion().size());
	}

}
