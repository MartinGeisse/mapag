package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeConflictResolverTest {

	@Test
	public void testCreateNopResolver() {
		AlternativeConflictResolver resolver = new AlternativeConflictResolver(null, null);
		Assert.assertNull(resolver.getEffectivePrecedenceTerminal());
		Assert.assertNull(resolver.getTerminalToResolution());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateResolverWithEmptyPrecedenceTerminalNotAllowed() {
		new AlternativeConflictResolver("", null);
	}

	@Test
	public void testCreateResolverWithPrecedenceTerminal() {
		AlternativeConflictResolver resolver = new AlternativeConflictResolver("foo", null);
		Assert.assertEquals("foo", resolver.getEffectivePrecedenceTerminal());
		Assert.assertNull(resolver.getTerminalToResolution());
	}

	@Test
	public void testCreateResolverWithResolutionMap() {
		ImmutableMap<String, ConflictResolution> terminalToResolution = ImmutableMap.of("foo", ConflictResolution.SHIFT);
		AlternativeConflictResolver resolver = new AlternativeConflictResolver(null, terminalToResolution);
		Assert.assertNull(resolver.getEffectivePrecedenceTerminal());
		Assert.assertEquals(terminalToResolution, resolver.getTerminalToResolution());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateResolverWithBothPrecedenceTerminalAndResolutionMapNotAllowed() {
		new AlternativeConflictResolver("foo", ImmutableMap.of("bar", ConflictResolution.SHIFT));
	}

	@Test
	public void testEqualsHashCodeForNopResolver() {
		AlternativeConflictResolver resolver1 = new AlternativeConflictResolver(null, null);
		AlternativeConflictResolver resolver2 = new AlternativeConflictResolver(null, null);
		Assert.assertEquals(resolver1, resolver2);
		Assert.assertEquals(resolver1.hashCode(), resolver2.hashCode());
	}

	@Test
	public void testEqualsHashCodeForPrecedenceBasedResolver() {

		AlternativeConflictResolver a1 = new AlternativeConflictResolver("foo", null);
		AlternativeConflictResolver a2 = new AlternativeConflictResolver("foo", null);
		AlternativeConflictResolver b1 = new AlternativeConflictResolver("bar", null);
		AlternativeConflictResolver b2 = new AlternativeConflictResolver("bar", null);

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
	public void testEqualsHashCodeForMapBasedResolver() {

		AlternativeConflictResolver a1 = new AlternativeConflictResolver(null, ImmutableMap.of("foo", ConflictResolution.SHIFT));
		AlternativeConflictResolver a2 = new AlternativeConflictResolver(null, ImmutableMap.of("foo", ConflictResolution.SHIFT));
		AlternativeConflictResolver b1 = new AlternativeConflictResolver(null, ImmutableMap.of("foo", ConflictResolution.REDUCE));
		AlternativeConflictResolver b2 = new AlternativeConflictResolver(null, ImmutableMap.of("foo", ConflictResolution.REDUCE));
		AlternativeConflictResolver c1 = new AlternativeConflictResolver(null, ImmutableMap.of("bar", ConflictResolution.SHIFT));
		AlternativeConflictResolver c2 = new AlternativeConflictResolver(null, ImmutableMap.of("bar", ConflictResolution.SHIFT));

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
	public void testEqualsHashCodeForDifferentlyTypedResolver() {
		AlternativeConflictResolver a = new AlternativeConflictResolver(null, null);
		AlternativeConflictResolver b = new AlternativeConflictResolver("foo", null);
		AlternativeConflictResolver c = new AlternativeConflictResolver(null, ImmutableMap.of("foo", ConflictResolution.SHIFT));
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a, c);
		Assert.assertNotEquals(b, c);
	}

}
