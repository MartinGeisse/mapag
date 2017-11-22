package name.martingeisse.mapag.util;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;

/**
 *
 */
public class ListUtilTest {

	private static final ImmutableList<String> TEST_LIST = ImmutableList.of("foo", "foofoofoofoofoofoo", "ba", "", "barb");
	private static final Predicate<String> TEST_PREDICATE = s -> s.length() > 3;

	@Test(expected = IllegalArgumentException.class)
	public void testWithElementsRetainedNullList() {
		ListUtil.withElementsRetained(null, TEST_PREDICATE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithElementsRetainedNullPredicate() {
		ListUtil.withElementsRetained(TEST_LIST, null);
	}

	@Test
	public void testWithElementsRetained() {
		Assert.assertEquals(ImmutableList.of("foofoofoofoofoofoo", "barb"), ListUtil.withElementsRetained(TEST_LIST, TEST_PREDICATE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithElementsRemovedNullList() {
		ListUtil.withElementsRemoved(null, TEST_PREDICATE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithElementsRemovedNullPredicate() {
		ListUtil.withElementsRemoved(TEST_LIST, null);
	}

	@Test
	public void testWithElementsRemoved() {
		Assert.assertEquals(ImmutableList.of("foo", "ba", ""), ListUtil.withElementsRemoved(TEST_LIST, TEST_PREDICATE));
	}

}
