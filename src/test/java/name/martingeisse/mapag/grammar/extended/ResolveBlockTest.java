package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.ConflictResolution;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ResolveBlockTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new ResolveBlock(null);
	}

	@Test
	public void testConstructorEmptyAllowed() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of());
		Assert.assertEquals(ImmutableList.of(), resolveBlock.getResolveDeclarations());
	}

	@Test
	public void testConstructorGetter() {
		ImmutableList<ResolveDeclaration> resolveDeclarations = ImmutableList.of(
			new ResolveDeclaration(ConflictResolution.SHIFT, ImmutableList.of("foo")),
			new ResolveDeclaration(ConflictResolution.REDUCE, ImmutableList.of("bar", "baz"))
		);
		ResolveBlock resolveBlock = new ResolveBlock(resolveDeclarations);
		Assert.assertEquals(resolveDeclarations, resolveBlock.getResolveDeclarations());
	}

}
