package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class OptionalExpressionTest {

	private static final Expression DUMMY = new SymbolReference("foo");

	@Test(expected = IllegalArgumentException.class)
	public void testNullOperand() {
		new OptionalExpression(null);
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertSame(DUMMY, new OptionalExpression(DUMMY).getOperand());
	}

	@Test
	public void testWithName() {
		OptionalExpression expression = (OptionalExpression)(new OptionalExpression(DUMMY).withName("myName"));
		Assert.assertEquals("myName", expression.getName());
		Assert.assertSame(DUMMY, expression.getOperand());
	}

}
