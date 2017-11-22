package name.martingeisse.mapag.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class ListComparatorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullElementComparator() {
		new ListComparator<>(null);
	}

	// this is a test for the getDigitSum() test helper
	@Test
	public void testDigitSum() {
		List<Integer> list = Arrays.asList(5, 11, 99, 3, 8, 10);
		List<Integer> expected = Arrays.asList(10, 11, 3, 5, 8, 99);
		Collections.sort(list, Comparator.comparing(this::getDigitSum));
		Assert.assertEquals(expected, list);
	}

	@Test
	public void testListSorting() {
		List<List<Integer>> list = Arrays.asList(
			Arrays.asList(5, 71, 99),
			Arrays.asList(2, 72, 99),
			Arrays.asList(5, 72, 99),
			Arrays.asList(5, 72, 1),
			Arrays.asList(9, 33, 99),
			Arrays.asList(5, 33, 99)
		);
		List<List<Integer>> expected = Arrays.asList(
			Arrays.asList(2, 72, 99),
			Arrays.asList(5, 33, 99),
			Arrays.asList(5, 71, 99),
			Arrays.asList(5, 72, 1),
			Arrays.asList(5, 72, 99),
			Arrays.asList(9, 33, 99)
		);
		Collections.sort(list, new ListComparator<>(Comparator.comparing(this::getDigitSum)));
		Assert.assertEquals(expected, list);
	}

	private int getDigitSum(int x) {
		int result = 0;
		while (x != 0) {
			result += x % 10;
			x /= 10;
		}
		return result;
	}

}
