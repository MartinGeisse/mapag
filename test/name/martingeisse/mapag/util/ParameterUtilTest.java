package name.martingeisse.mapag.util;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 */
public class ParameterUtilTest {

	private static final String NAME = "___long_dummy_name_to_look_for___";

	private static final <T> IllegalArgumentException testException(BiFunction<T, String, ?> checkMethod, T value) {
		try {
			checkMethod.apply(value, NAME);
			Assert.fail("check method failed to throw IllegalArgumentException");
			return null;
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains(NAME));
			return e;
		}
	}

	@Test
	public void testEnsureNotNullWithNull() {
		testException(ParameterUtil::ensureNotNull, null);
	}

	@Test
	public void testEnsureNotNullWithNonNull() {
		ParameterUtil.ensureNotNull("foo", NAME);
	}

	@Test
	public void testEnsureNoNullElementAcceptsNullCollection() {
		ParameterUtil.ensureNoNullElement((String[]) null, NAME);
		ParameterUtil.ensureNoNullElement((List<?>) null, NAME);
		ParameterUtil.ensureNoNullElement((Iterable<?>) null, NAME);
	}

	@Test
	public void testEnsureNoNullElementWithEmptyCollection() {
		ParameterUtil.ensureNoNullElement(new String[0], NAME);
		ParameterUtil.ensureNoNullElement(ImmutableList.of(), NAME);
		ParameterUtil.ensureNoNullElement((Iterable<?>) ImmutableList.of(), NAME);
	}

	@Test
	public void testEnsureNoNullElementWithNullElement() {
		testException(ParameterUtil::ensureNoNullElement, new String[]{"foo", null, "bar"});
		testException(ParameterUtil::ensureNoNullElement, Arrays.asList("foo", null, "bar"));
		testException(ParameterUtil::ensureNoNullElement, (Iterable<?>) Arrays.asList("foo", null, "bar"));
	}

	@Test
	public void testEnsureNoNullElementWithoutNullElement() {
		ParameterUtil.ensureNoNullElement(new String[]{"foo", "xxx", "bar"}, NAME);
		ParameterUtil.ensureNoNullElement(Arrays.asList("foo", "xxx", "bar"), NAME);
		ParameterUtil.ensureNoNullElement((Iterable<?>) Arrays.asList("foo", "xxx", "bar"), NAME);
	}

	@Test
	public void testEnsureNotNullOrEmptyStringWithNull() {
		testException(ParameterUtil::ensureNotNullOrEmpty, (String) null);
	}

	@Test
	public void testEnsureNotNullOrEmptyStringWithEmpty() {
		testException(ParameterUtil::ensureNotNullOrEmpty, "");
	}

	@Test
	public void testEnsureNotNullOrEmptyStringWithNonNull() {
		ParameterUtil.ensureNotNullOrEmpty("foo", NAME);
	}

	@Test
	public void testEnsureNoNullOrEmptyElementAcceptsNullCollection() {
		ParameterUtil.ensureNoNullOrEmptyElement((String[]) null, NAME);
		ParameterUtil.ensureNoNullOrEmptyElement((List<String>) null, NAME);
		ParameterUtil.ensureNoNullOrEmptyElement((Iterable<String>) null, NAME);
	}

	@Test
	public void testEnsureNoNullOrEmptyElementWithEmptyCollection() {
		ParameterUtil.ensureNoNullOrEmptyElement(new String[0], NAME);
		ParameterUtil.ensureNoNullOrEmptyElement(ImmutableList.of(), NAME);
		ParameterUtil.ensureNoNullOrEmptyElement((Iterable<String>) ImmutableList.<String>of(), NAME);
	}

	@Test
	public void testEnsureNoNullOrEmptyElementWithNullElement() {
		testException(ParameterUtil::ensureNoNullOrEmptyElement, new String[]{"foo", null, "bar"});
		testException(ParameterUtil::ensureNoNullOrEmptyElement, Arrays.asList("foo", null, "bar"));
		testException(ParameterUtil::ensureNoNullOrEmptyElement, (Iterable<String>) Arrays.asList("foo", null, "bar"));
	}

	@Test
	public void testEnsureNoNullOrEmptyElementWithEmptyElement() {
		testException(ParameterUtil::ensureNoNullOrEmptyElement, new String[]{"foo", "", "bar"});
		testException(ParameterUtil::ensureNoNullOrEmptyElement, Arrays.asList("foo", "", "bar"));
		testException(ParameterUtil::ensureNoNullOrEmptyElement, (Iterable<String>) Arrays.asList("foo", "", "bar"));
	}

	@Test
	public void testEnsureNoNullOrEmptyElementWithoutNullOrEmptyElement() {
		ParameterUtil.ensureNoNullOrEmptyElement(new String[]{"foo", "xxx", "bar"}, NAME);
		ParameterUtil.ensureNoNullOrEmptyElement(Arrays.asList("foo", "xxx", "bar"), NAME);
		ParameterUtil.ensureNoNullOrEmptyElement((Iterable<String>) Arrays.asList("foo", "xxx", "bar"), NAME);
	}

	@Test
	public void testEnsureNotNullOrEmptyCollectionWithNull() {
		testException(ParameterUtil::ensureNotNullOrEmpty, (Collection<?>) null);
	}

	@Test
	public void testEnsureNotNullOrEmptyCollectionWithEmpty() {
		testException(ParameterUtil::ensureNotNullOrEmpty, ImmutableList.of());
	}

	@Test
	public void testEnsureNotNullOrEmptyCollectionWithNonNull() {
		ParameterUtil.ensureNotNullOrEmpty(ImmutableList.of("foo"), NAME);
	}

}
