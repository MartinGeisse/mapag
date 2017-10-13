package name.martingeisse.mapag.util;

import java.util.Comparator;
import java.util.List;

/**
 * Implements lexicographic ordering on lists.
 */
public final class ListComparator<T, L extends List<? extends T>> implements Comparator<L> {

	private final Comparator<T> elementComparator;

	public ListComparator(Comparator<T> elementComparator) {
		this.elementComparator = ParameterUtil.ensureNotNull(elementComparator, "elementComparator");;
	}

	public static <T extends Comparable<T>, L extends List<? extends T>> ListComparator<T, L> forComparableElements() {
		return new ListComparator<>((x, y) -> x.compareTo(y));
	}

	@Override
	public int compare(L list1, L list2) {
		int i = 0;
		while (true) {
			if (i == list1.size()) {
				if (i == list2.size()) {
					return 0;
				} else {
					return -1;
				}
			} else {
				if (i == list2.size()) {
					return 1;
				} else {
					int elementResult = elementComparator.compare(list1.get(i), list2.get(i));
					if (elementResult != 0) {
						return elementResult;
					} else {
						i++;
					}
				}
			}
		}
	}

}
