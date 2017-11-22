package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.testutil.ExAssert;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ExpansionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullNotAllowed() {
		new Expansion(null);
	}

	@Test
	public void testConstructorGettersEmpty() {
		Expansion expansion = new Expansion(ImmutableList.of());
		Assert.assertEquals(ImmutableList.of(), expansion.getElements());
		Assert.assertEquals(ImmutableList.of(), expansion.getSymbols());
		Assert.assertTrue(expansion.isEmpty());
	}

	@Test
	public void testConstructorGettersNonemptyUnnamed() {
		ImmutableList<ExpansionElement> elements = ImmutableList.of(
			new ExpansionElement("foo", null),
			new ExpansionElement("bar", null)
		);
		Expansion expansion = new Expansion(elements);
		Assert.assertEquals(elements, expansion.getElements());
		Assert.assertEquals(ImmutableList.of("foo", "bar"), expansion.getSymbols());
		Assert.assertFalse(expansion.isEmpty());
	}

	@Test
	public void testConstructorGettersNonemptyPartiallyNamed() {
		ImmutableList<ExpansionElement> elements = ImmutableList.of(
			new ExpansionElement("foo", null),
			new ExpansionElement("bar", "baz")
		);
		Expansion expansion = new Expansion(elements);
		Assert.assertEquals(elements, expansion.getElements());
		Assert.assertEquals(ImmutableList.of("foo", "bar"), expansion.getSymbols());
		Assert.assertFalse(expansion.isEmpty());
	}

	public void testVanishSymbolNullAndEmpty() {
		Expansion emptyExpansion = new Expansion(ImmutableList.of());
		Expansion nonemptyExpansion = new Expansion(ImmutableList.of(
			new ExpansionElement("foo", null),
			new ExpansionElement("bar", "baz")
		));
		ExAssert.assertThrows(IllegalArgumentException.class, () -> emptyExpansion.vanishSymbol(null));
		ExAssert.assertThrows(IllegalArgumentException.class, () -> nonemptyExpansion.vanishSymbol(null));
		ExAssert.assertThrows(IllegalArgumentException.class, () -> emptyExpansion.vanishSymbol(""));
		ExAssert.assertThrows(IllegalArgumentException.class, () -> nonemptyExpansion.vanishSymbol(""));
	}

	@Test
	public void testVanishSymbolInEmptyExpansion() {
		Expansion emptyExpansion = new Expansion(ImmutableList.of());
		Assert.assertTrue(emptyExpansion.vanishSymbol("foo").getElements().isEmpty());
	}

	@Test
	public void testVanishSymbol() {
		Expansion expansion = new Expansion(ImmutableList.of(
			new ExpansionElement("foo", "a"),
			new ExpansionElement("bar", null),
			new ExpansionElement("baz", "b"),
			new ExpansionElement("foo", null),
			new ExpansionElement("abc", "c")
		));

		Assert.assertEquals(ImmutableList.of(
			new ExpansionElement("bar", null),
			new ExpansionElement("baz", "b"),
			new ExpansionElement("abc", "c")
		), expansion.vanishSymbol("foo").getElements());

		Assert.assertEquals(ImmutableList.of(
			new ExpansionElement("foo", "a"),
			new ExpansionElement("baz", "b"),
			new ExpansionElement("foo", null),
			new ExpansionElement("abc", "c")
		), expansion.vanishSymbol("bar").getElements());

		Assert.assertEquals(ImmutableList.of(
			new ExpansionElement("foo", "a"),
			new ExpansionElement("bar", null),
			new ExpansionElement("baz", "b"),
			new ExpansionElement("foo", null),
			new ExpansionElement("abc", "c")
		), expansion.vanishSymbol("xyz").getElements());

	}

	@Test
	public void testVanishSymbolToEmptyAlternative() {
		Expansion expansion = new Expansion(ImmutableList.of(
			new ExpansionElement("foo", "a"),
			new ExpansionElement("foo", "b"),
			new ExpansionElement("foo", null)
		));
		Assert.assertEquals(ImmutableList.of(), expansion.vanishSymbol("foo").getElements());
	}

}
