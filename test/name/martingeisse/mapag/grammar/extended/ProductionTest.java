package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.expression.OrExpression;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ProductionTest {

	private static final String DUMMY_LEFT = "foo";
	private static final Expression DUMMY_RIGHT = new SymbolReference("bar");

	@Test(expected = IllegalArgumentException.class)
	public void testNullLeftHandSide() {
		new Production(null, DUMMY_RIGHT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyLeftHandSide() {
		new Production("", DUMMY_RIGHT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullRightHandSide() {
		new Production(DUMMY_LEFT, null);
	}

	@Test
	public void testConstructorGetter() {
		Production production = new Production(DUMMY_LEFT, DUMMY_RIGHT);
		Assert.assertSame(DUMMY_LEFT, production.getLeftHandSide());
		Assert.assertSame(DUMMY_RIGHT, production.getRightHandSide());
	}

}
