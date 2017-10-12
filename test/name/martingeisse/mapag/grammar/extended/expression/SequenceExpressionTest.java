package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SequenceExpressionTest {

	private static final Expression DUMMY_LEFT = new SymbolReference("foo");
	private static final Expression DUMMY_RIGHT = new SymbolReference("bar");

	@Test(expected = IllegalArgumentException.class)
	public void testNullLeftOperand() {
		new SequenceExpression(null, DUMMY_RIGHT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullRightOperand() {
		new SequenceExpression(DUMMY_LEFT, null);
	}

	@Test
	public void testConstructorGetter() {
		SequenceExpression sequenceExpression = new SequenceExpression(DUMMY_LEFT, DUMMY_RIGHT);
		Assert.assertSame(DUMMY_LEFT, sequenceExpression.getLeft());
		Assert.assertSame(DUMMY_RIGHT, sequenceExpression.getRight());
	}

}
