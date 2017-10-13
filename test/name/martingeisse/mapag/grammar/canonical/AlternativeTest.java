package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class AlternativeTest {

	private static final ImmutableList<String> EXPANSION = ImmutableList.of("foo", "bar", "baz");

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpansion() {
		new Alternative(null);
	}

	@Test
	public void testEmptyExpansion() {
		new Alternative(ImmutableList.of());
	}

	@Test
	public void testConstructorGetter() {
		Assert.assertEquals(EXPANSION, new Alternative(EXPANSION).getExpansion());
	}

}
