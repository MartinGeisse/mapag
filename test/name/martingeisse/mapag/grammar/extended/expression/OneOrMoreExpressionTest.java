package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class OneOrMoreExpressionTest {

	private static final Expression DUMMY = new SymbolReference("foo");

	@Test(expected = IllegalArgumentException.class)
	public void testNullOperand() {
		new OneOrMoreExpression(null);
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertSame(DUMMY, new OneOrMoreExpression(DUMMY).getOperand());
	}

	@Test
	public void testWithName() {
		OneOrMoreExpression expression = (OneOrMoreExpression) (new OneOrMoreExpression(DUMMY).withName("myName"));
		Assert.assertEquals("myName", expression.getName());
		Assert.assertSame(DUMMY, expression.getOperand());
	}

}
