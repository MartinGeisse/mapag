package name.martingeisse.mapag.testutil;

import org.junit.Assert;

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

}
