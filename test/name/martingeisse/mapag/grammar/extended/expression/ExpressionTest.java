package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 * This class uses {@link EmptyExpression} as a sample implementation for {@link Expression}.
 */
public class ExpressionTest {

	@Test
	public void testWithName() {
		Expression a = new EmptyExpression();
		Expression b = a.withName("foo");
		Expression c = b.withName("bar");
		Assert.assertNull(a.getName());
		Assert.assertEquals("foo", b.getName());
		Assert.assertEquals("bar", c.getName());
	}

	@Test
	public void testWithFallbackName() {
		Expression a = new EmptyExpression();
		Expression b = a.withFallbackName("foo");
		Expression c = b.withFallbackName("bar");
		Assert.assertNull(a.getName());
		Assert.assertEquals("foo", b.getName());
		Assert.assertEquals("foo", c.getName());
	}

	// TODO test illegal arguments to withNAme() and withFallbackName()

}
