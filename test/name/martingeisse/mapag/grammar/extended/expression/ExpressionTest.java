package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.testutil.ExAssert;
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
	public void testWithNameNull() {
		Expression a = new EmptyExpression();
		Expression b = a.withName("foo");
		Expression c = b.withName(null);
		Expression d = c.withName("bar");
		Assert.assertNull(a.getName());
		Assert.assertEquals("foo", b.getName());
		Assert.assertNull(c.getName());
		Assert.assertEquals("bar", d.getName());
	}

	@Test
	public void testWithNameEmpty() {
		Expression a = new EmptyExpression();
		ExAssert.assertThrows(IllegalArgumentException.class, () -> a.withName(""));
		Expression b = a.withName("foo");
		ExAssert.assertThrows(IllegalArgumentException.class, () -> b.withName(""));
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

	@Test
	public void testWithFallbackNameNull() {
		Expression a = new EmptyExpression();
		Assert.assertNull(a.withFallbackName(null).getName());
		Expression b = a.withName("foo");
		Assert.assertEquals("foo", b.withFallbackName(null).getName());
	}

	@Test
	public void testWithFallbackNameEmpty() {
		Expression a = new EmptyExpression();
		ExAssert.assertThrows(IllegalArgumentException.class, () -> a.withFallbackName(""));
		Expression b = a.withName("foo");
		ExAssert.assertThrows(IllegalArgumentException.class, () -> b.withFallbackName(""));
	}

}
