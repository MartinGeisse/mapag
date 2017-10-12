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

}
