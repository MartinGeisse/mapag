package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ZeroOrMoreExpressionTest {

	private static final Expression DUMMY = new SymbolReference("foo");

	@Test(expected = IllegalArgumentException.class)
	public void testNullOperand() {
		new ZeroOrMoreExpression(null);
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertSame(DUMMY, new ZeroOrMoreExpression(DUMMY).getOperand());
	}

	@Test
	public void testWithName() {
		ZeroOrMoreExpression expression = (ZeroOrMoreExpression)(new ZeroOrMoreExpression(DUMMY).withName("myName"));
		Assert.assertEquals("myName", expression.getName());
		Assert.assertSame(DUMMY, expression.getOperand());
	}

}
