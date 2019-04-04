package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeAttributesTest {

	@Test
	public void testCreateNopAttributes() {
		AlternativeAttributes attributes = new AlternativeAttributes(null, null, false, false);
		Assert.assertNull(attributes.getEffectivePrecedenceTerminal());
		Assert.assertNull(attributes.getTerminalToResolution());
		Assert.assertFalse(attributes.isReduceOnError());
		Assert.assertFalse(attributes.isReduceOnEofOnly());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAttributesWithEmptyPrecedenceTerminalNotAllowed() {
		new AlternativeAttributes("", null, false, false);
	}

	@Test
	public void testCreateAttributesWithPrecedenceTerminal() {
		AlternativeAttributes attributes = new AlternativeAttributes("foo", null, false, false);
		Assert.assertEquals("foo", attributes.getEffectivePrecedenceTerminal());
		Assert.assertNull(attributes.getTerminalToResolution());
		Assert.assertFalse(attributes.isReduceOnError());
		Assert.assertFalse(attributes.isReduceOnEofOnly());
	}

	@Test
	public void testCreateAttributesWithResolutionMap() {
		ImmutableMap<String, ConflictResolution> terminalToResolution = ImmutableMap.of("foo", ConflictResolution.SHIFT);
		AlternativeAttributes attributes = new AlternativeAttributes(null, terminalToResolution, false, false);
		Assert.assertNull(attributes.getEffectivePrecedenceTerminal());
		Assert.assertEquals(terminalToResolution, attributes.getTerminalToResolution());
		Assert.assertFalse(attributes.isReduceOnError());
		Assert.assertFalse(attributes.isReduceOnEofOnly());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAttributesWithBothPrecedenceTerminalAndResolutionMapNotAllowed() {
		new AlternativeAttributes("foo", ImmutableMap.of("bar", ConflictResolution.SHIFT), false, false);
	}

	@Test
	public void testCreateAttributesWithReduceOnError() {
		AlternativeAttributes attributes = new AlternativeAttributes(null, null, true, false);
		Assert.assertNull(attributes.getEffectivePrecedenceTerminal());
		Assert.assertNull(attributes.getTerminalToResolution());
		Assert.assertTrue(attributes.isReduceOnError());
		Assert.assertFalse(attributes.isReduceOnEofOnly());
	}

	@Test
	public void testCreateAttributesWithReduceOnEofOnly() {
		AlternativeAttributes attributes = new AlternativeAttributes(null, null, false, true);
		Assert.assertNull(attributes.getEffectivePrecedenceTerminal());
		Assert.assertNull(attributes.getTerminalToResolution());
		Assert.assertFalse(attributes.isReduceOnError());
		Assert.assertTrue(attributes.isReduceOnEofOnly());
	}

	@Test
	public void testEqualsHashCodeForNopAttributes() {
		AlternativeAttributes attributes1 = new AlternativeAttributes(null, null, false, false);
		AlternativeAttributes attributes2 = new AlternativeAttributes(null, null, false, false);
		Assert.assertEquals(attributes1, attributes2);
		Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
	}

	@Test
	public void testEqualsHashCodeForPrecedence() {

		AlternativeAttributes a1 = new AlternativeAttributes("foo", null, false, false);
		AlternativeAttributes a2 = new AlternativeAttributes("foo", null, false, false);
		AlternativeAttributes b1 = new AlternativeAttributes("bar", null, false, false);
		AlternativeAttributes b2 = new AlternativeAttributes("bar", null, false, false);

		Assert.assertEquals(a1, a2);
		Assert.assertEquals(b1, b2);
		Assert.assertNotEquals(a1, b1);
		Assert.assertNotEquals(a1, b2);
		Assert.assertNotEquals(a2, b1);
		Assert.assertNotEquals(a2, b2);

		Assert.assertEquals(a1.hashCode(), a2.hashCode());
		Assert.assertEquals(b1.hashCode(), b2.hashCode());

	}

	@Test
	public void testEqualsHashCodeForConflictResolutionMap() {

		AlternativeAttributes a1 = new AlternativeAttributes(null, ImmutableMap.of("foo", ConflictResolution.SHIFT), false, false);
		AlternativeAttributes a2 = new AlternativeAttributes(null, ImmutableMap.of("foo", ConflictResolution.SHIFT), false, false);
		AlternativeAttributes b1 = new AlternativeAttributes(null, ImmutableMap.of("foo", ConflictResolution.REDUCE), false, false);
		AlternativeAttributes b2 = new AlternativeAttributes(null, ImmutableMap.of("foo", ConflictResolution.REDUCE), false, false);
		AlternativeAttributes c1 = new AlternativeAttributes(null, ImmutableMap.of("bar", ConflictResolution.SHIFT), false, false);
		AlternativeAttributes c2 = new AlternativeAttributes(null, ImmutableMap.of("bar", ConflictResolution.SHIFT), false, false);

		Assert.assertEquals(a1, a2);
		Assert.assertEquals(b1, b2);
		Assert.assertEquals(c1, c2);

		Assert.assertNotEquals(a1, b1);
		Assert.assertNotEquals(a1, b2);
		Assert.assertNotEquals(a1, c1);
		Assert.assertNotEquals(a1, c2);

		Assert.assertNotEquals(b1, a1);
		Assert.assertNotEquals(b1, a2);
		Assert.assertNotEquals(b1, c1);
		Assert.assertNotEquals(b1, c2);

		Assert.assertNotEquals(c1, a1);
		Assert.assertNotEquals(c1, a2);
		Assert.assertNotEquals(c1, b1);
		Assert.assertNotEquals(c1, b2);

		Assert.assertEquals(a1.hashCode(), a2.hashCode());
		Assert.assertEquals(b1.hashCode(), b2.hashCode());
		Assert.assertEquals(c1.hashCode(), c2.hashCode());

	}

	@Test
	public void testEqualsHashCodeForPrecedenceAgainstConflictResolutionMap() {
		AlternativeAttributes a = new AlternativeAttributes(null, null, false, false);
		AlternativeAttributes b = new AlternativeAttributes("foo", null, false, false);
		AlternativeAttributes c = new AlternativeAttributes(null, ImmutableMap.of("foo", ConflictResolution.SHIFT), false, false);
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a, c);
		Assert.assertNotEquals(b, c);
	}

	@Test
	public void testEqualsHashCodeForReduceOnErrorAndReduceOnEof() {

		AlternativeAttributes a = new AlternativeAttributes(null, null, false, false);
		AlternativeAttributes b1 = new AlternativeAttributes(null, null, true, false);
		AlternativeAttributes b2 = new AlternativeAttributes(null, null, true, false);
		AlternativeAttributes c1 = new AlternativeAttributes(null, null, false, true);
		AlternativeAttributes c2 = new AlternativeAttributes(null, null, false, true);

		Assert.assertNotEquals(a, b1);
		Assert.assertNotEquals(a, b2);
		Assert.assertNotEquals(a, c1);
		Assert.assertNotEquals(a, c2);
		Assert.assertNotEquals(b1, c1);

		Assert.assertEquals(b1, b2);
		Assert.assertEquals(b1.hashCode(), b2.hashCode());

		Assert.assertEquals(c1, c2);
		Assert.assertEquals(c1.hashCode(), c2.hashCode());

	}


}
