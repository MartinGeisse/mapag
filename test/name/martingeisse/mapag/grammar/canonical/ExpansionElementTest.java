package name.martingeisse.mapag.grammar.canonical;

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

}
