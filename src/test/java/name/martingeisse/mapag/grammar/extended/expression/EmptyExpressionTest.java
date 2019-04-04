package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class EmptyExpressionTest {

	@Test
	public void testConstructor() {
		new EmptyExpression();
	}

	@Test
	public void testWithName() {
		EmptyExpression expression = (EmptyExpression)(new EmptyExpression().withName("myName"));
		Assert.assertEquals("myName", expression.getName());
	}

}
