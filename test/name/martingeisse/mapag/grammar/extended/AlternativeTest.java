package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final SymbolReference SYMBOL_REFERENCE = new SymbolReference("foo");

	@Test(expected = IllegalArgumentException.class)
	public void testNullForSingleParameterConstructor() {
		new Alternative(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpressionForThreeParametersConstructor() {
		new Alternative(null, Alternative.PrecedenceSpecificationType.EXPLICIT, "abcdef");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPrecedenceSpecificationTypeWithoutSpecification() {
		new Alternative(SYMBOL_REFERENCE, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPrecedenceSpecificationTypeWithSpecification() {
		new Alternative(SYMBOL_REFERENCE, null, "abcdef");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPrecedenceSpecificationForTypeImplicit() {
		new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.IMPLICIT, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonemptyPrecedenceSpecificationForTypeImplicit() {
		new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.IMPLICIT, "abcdef");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPrecedenceSpecificationForTypeExplicit() {
		new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.EXPLICIT, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPrecedenceSpecificationForTypeExplicit() {
		new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.EXPLICIT, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyPrecedenceSpecificationForTypeUndefined() {
		new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.UNDEFINED, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonemptyPrecedenceSpecificationForTypeUndefined() {
		new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.UNDEFINED, "abcdef");
	}

	@Test
	public void testSingleParameterConstructorGetter() {
		Alternative alternative = new Alternative(SYMBOL_REFERENCE);
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals(Alternative.PrecedenceSpecificationType.IMPLICIT, alternative.getPrecedenceSpecificationType());
		Assert.assertNull(alternative.getPrecedenceSpecification());
	}

	@Test
	public void testThreeParametersConstructorGetterWithTypeImplicit() {
		Alternative alternative = new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.IMPLICIT, null);
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals(Alternative.PrecedenceSpecificationType.IMPLICIT, alternative.getPrecedenceSpecificationType());
		Assert.assertNull(alternative.getPrecedenceSpecification());
	}

	@Test
	public void testThreeParametersConstructorGetterWithTypeExplicit() {
		Alternative alternative = new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.EXPLICIT, "abcdef");
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals(Alternative.PrecedenceSpecificationType.EXPLICIT, alternative.getPrecedenceSpecificationType());
		Assert.assertEquals("abcdef", alternative.getPrecedenceSpecification());
	}

	@Test
	public void testThreeParametersConstructorGetterWithTypeUndefined() {
		Alternative alternative = new Alternative(SYMBOL_REFERENCE, Alternative.PrecedenceSpecificationType.UNDEFINED, null);
		Assert.assertSame(SYMBOL_REFERENCE, alternative.getExpression());
		Assert.assertEquals(Alternative.PrecedenceSpecificationType.UNDEFINED, alternative.getPrecedenceSpecificationType());
		Assert.assertNull(alternative.getPrecedenceSpecification());
	}

}
