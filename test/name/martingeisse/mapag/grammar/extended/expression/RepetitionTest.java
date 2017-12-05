package name.martingeisse.mapag.grammar.extended.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class RepetitionTest {

	private static final Expression DUMMY_ELEMENT = new SymbolReference("foo");
	private static final Expression DUMMY_SEPARATOR = new SymbolReference("bar");

	@Test(expected = IllegalArgumentException.class)
	public void testNullElementOperandWithoutEmpty() {
		new Repetition(null, DUMMY_SEPARATOR, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullElementOperandWithEmpty() {
		new Repetition(null, DUMMY_SEPARATOR, true);
	}

	@Test
	public void testWithoutSeparator() {

		Repetition repetition1 = new Repetition(DUMMY_ELEMENT, null, false);
		Assert.assertNull(repetition1.getName());
		Assert.assertEquals(DUMMY_ELEMENT, repetition1.getElementExpression());
		Assert.assertNull(repetition1.getSeparatorExpression());
		Assert.assertFalse(repetition1.isEmptyAllowed());

		Repetition repetition2 = new Repetition(DUMMY_ELEMENT, null, true);
		Assert.assertNull(repetition2.getName());
		Assert.assertEquals(DUMMY_ELEMENT, repetition2.getElementExpression());
		Assert.assertNull(repetition2.getSeparatorExpression());
		Assert.assertTrue(repetition2.isEmptyAllowed());

	}

	@Test
	public void testWithSeparator() {

		Repetition repetition1 = new Repetition(DUMMY_ELEMENT, DUMMY_SEPARATOR, false);
		Assert.assertNull(repetition1.getName());
		Assert.assertEquals(DUMMY_ELEMENT, repetition1.getElementExpression());
		Assert.assertEquals(DUMMY_SEPARATOR, repetition1.getSeparatorExpression());
		Assert.assertFalse(repetition1.isEmptyAllowed());

		Repetition repetition2 = new Repetition(DUMMY_ELEMENT, DUMMY_SEPARATOR, true);
		Assert.assertNull(repetition2.getName());
		Assert.assertEquals(DUMMY_ELEMENT, repetition2.getElementExpression());
		Assert.assertEquals(DUMMY_SEPARATOR, repetition2.getSeparatorExpression());
		Assert.assertTrue(repetition2.isEmptyAllowed());

	}

	@Test
	public void testWithName() {

		Repetition repetition1 = (Repetition)new Repetition(DUMMY_ELEMENT, DUMMY_SEPARATOR, false).withName("myName");
		Assert.assertEquals("myName", repetition1.getName());
		Assert.assertEquals(DUMMY_ELEMENT, repetition1.getElementExpression());
		Assert.assertEquals(DUMMY_SEPARATOR, repetition1.getSeparatorExpression());
		Assert.assertFalse(repetition1.isEmptyAllowed());

		Repetition repetition2 = (Repetition)new Repetition(DUMMY_ELEMENT, DUMMY_SEPARATOR, true).withName("myName");
		Assert.assertEquals("myName", repetition2.getName());
		Assert.assertEquals(DUMMY_ELEMENT, repetition2.getElementExpression());
		Assert.assertEquals(DUMMY_SEPARATOR, repetition2.getSeparatorExpression());
		Assert.assertTrue(repetition2.isEmptyAllowed());

	}

}
