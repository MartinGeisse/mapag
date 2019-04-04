package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.testutil.ExAssert;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ExpansionElementTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullSymbolNotAllowed() {
		new ExpansionElement(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptySymbolNotAllowed() {
		new ExpansionElement("", null);
	}

	@Test
	public void testWithoutExpressionName() {
		ExpansionElement element = new ExpansionElement("foo", null);
		Assert.assertEquals("foo", element.getSymbol());
		Assert.assertNull(element.getExpressionName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithEmptyExpressionName() {
		new ExpansionElement("foo", "");
	}

	@Test
	public void testWithExpressionName() {
		ExpansionElement element = new ExpansionElement("foo", "bar");
		Assert.assertEquals("foo", element.getSymbol());
		Assert.assertEquals("bar", element.getExpressionName());
	}

	@Test
	public void testEqualsHashCode() {

		ExpansionElement a1 = new ExpansionElement("a", null);
		ExpansionElement a2 = new ExpansionElement("a", null);
		ExpansionElement b1 = new ExpansionElement("b", null);
		ExpansionElement b2 = new ExpansionElement("b", null);
		ExpansionElement c1 = new ExpansionElement("a", "foo");
		ExpansionElement c2 = new ExpansionElement("a", "foo");
		ExpansionElement d1 = new ExpansionElement("a", "bar");
		ExpansionElement d2 = new ExpansionElement("a", "bar");
		ExpansionElement e1 = new ExpansionElement("b", "bar");
		ExpansionElement e2 = new ExpansionElement("b", "bar");

		Assert.assertEquals(a1, a2);
		Assert.assertEquals(b1, b2);
		Assert.assertEquals(c1, c2);
		Assert.assertEquals(d1, d2);
		Assert.assertEquals(e1, e2);

		Assert.assertEquals(a1.hashCode(), a2.hashCode());
		Assert.assertEquals(b1.hashCode(), b2.hashCode());
		Assert.assertEquals(c1.hashCode(), c2.hashCode());
		Assert.assertEquals(d1.hashCode(), d2.hashCode());
		Assert.assertEquals(e1.hashCode(), e2.hashCode());

		ExAssert.assertMutuallyUnequal(a1, b1, c1, d1, e1);

	}

}
