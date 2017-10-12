package name.martingeisse.mapag.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PairTest {

	@Test
	public void testConstructorGetter() {
		Object a = new Object();
		Object b = new Object();
		Pair<Object, Object> pair = new Pair<>(a, b);
		Assert.assertSame(a, pair.getLeft());
		Assert.assertSame(b, pair.getRight());
	}

	@Test
	public void testEqualsHashCode() {
		String a1 = "foo";
		String a2 = new String(a1);
		String b1 = "bar";
		String b2 = new String(b1);
		Assert.assertEquals(new Pair<>(a1, b1), new Pair<>(a2, b2));
		Assert.assertEquals(new Pair<>(a1, b1).hashCode(), new Pair<>(a2, b2).hashCode());
		Assert.assertNotEquals(new Pair<>(a1, "xxx"), new Pair<>(a2, b2));
		Assert.assertNotEquals(new Pair<>("xxx", b1), new Pair<>(a2, b2));
	}

	@Test
	public void testToString() {
		String a = "foo";
		String b = "bar";
		String s = new Pair<>(a, b).toString();
		Assert.assertTrue(s.contains(a));
		Assert.assertTrue(s.contains(b));
	}

}
