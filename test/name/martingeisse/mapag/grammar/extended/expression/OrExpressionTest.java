package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class OrExpressionTest {

	private static final Expression DUMMY_LEFT = new SymbolReference("foo");
	private static final Expression DUMMY_RIGHT = new SymbolReference("bar");

	@Test(expected = IllegalArgumentException.class)
	public void testNullLeftOperand() {
		new OrExpression(null, DUMMY_RIGHT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullRightOperand() {
		new OrExpression(DUMMY_LEFT, null);
	}

	@Test
	public void testConstructorGetter() {
		OrExpression orExpression = new OrExpression(DUMMY_LEFT, DUMMY_RIGHT);
		Assert.assertSame(DUMMY_LEFT, orExpression.getLeftOperand());
		Assert.assertSame(DUMMY_RIGHT, orExpression.getRightOperand());
	}

	@Test
	public void testWithName() {
		OrExpression expression = (OrExpression) (new OrExpression(DUMMY_LEFT, DUMMY_RIGHT).withName("myName"));
		Assert.assertEquals("myName", expression.getName());
		Assert.assertSame(DUMMY_LEFT, expression.getLeftOperand());
		Assert.assertSame(DUMMY_RIGHT, expression.getRightOperand());
	}

}
