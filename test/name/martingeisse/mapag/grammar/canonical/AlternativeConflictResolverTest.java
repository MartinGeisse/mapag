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

}
