package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.ConflictResolution;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ResolveDeclarationTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullConflictResolution() {
		new ResolveDeclaration(null, ImmutableList.of("foo"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminals() {
		new ResolveDeclaration(ConflictResolution.SHIFT, null);
	}

	@Test
	public void testConstructorGetter() {
		ResolveDeclaration resolveDeclaration = new ResolveDeclaration(ConflictResolution.REDUCE, ImmutableList.of("foo", "bar"));
		Assert.assertEquals(ConflictResolution.REDUCE, resolveDeclaration.getConflictResolution());
		Assert.assertEquals(ImmutableList.of("foo", "bar"), resolveDeclaration.getTerminals());
	}

	@Test
	public void testEmptyTerminalListAllowed() {
		// pointless, but allowed
		ResolveDeclaration resolveDeclaration = new ResolveDeclaration(ConflictResolution.SHIFT, ImmutableList.of());
		Assert.assertEquals(ConflictResolution.SHIFT, resolveDeclaration.getConflictResolution());
		Assert.assertEquals(ImmutableList.of(), resolveDeclaration.getTerminals());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStringInTerminals() {
		new ResolveDeclaration(ConflictResolution.SHIFT, ImmutableList.of("foo", ""));
	}

}
