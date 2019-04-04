package name.martingeisse.mapag.testutil;

import org.junit.Assert;

import java.util.List;

/**
 * Extended assertion methods.
 */
public final class ExAssert {

	// prevent instantiation
	private ExAssert() {
	}

	public static void assertThrows(Class<? extends Throwable> expectedExceptionType, Runnable body) {
		try {
			body.run();
		} catch (Throwable t) {
			if (expectedExceptionType.isInstance(t)) {
				return;
			} else {
				throw t;
			}
		}
		Assert.fail("expected exception of type " + expectedExceptionType + " was not thrown");
	}

	/**
	 * Invokes {@link Assert#assertNotEquals(Object, Object)} on each pair of objects, except for comparing objects
	 * at equal indices using {@link Assert#assertEquals(Object, Object)}. If an object is in the list
	 * twice, an error gets thrown: Either the object compares equal to itself, so the objects are not mutually unequal,
	 * or the object compares unequal to itself, which is caught as a special case.
	 */
	public static void assertMutuallyUnequal(Object... objects) {
		// note: we use index counters to avoid comparing an object to itself, so we catch a case where the same
		// object is twice in the list
		for (int i = 0; i < objects.length; i++) {
			for (int j = 0; j < objects.length; j++) {
				if (i == j) {
					Assert.assertEquals(objects[i], objects[j]);
				} else {
					Assert.assertNotEquals(objects[i], objects[j]);
				}
			}
		}
	}

}
