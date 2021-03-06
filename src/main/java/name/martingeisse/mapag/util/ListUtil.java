package name.martingeisse.mapag.util;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public final class ListUtil {

	// prevent instantiation
	private ListUtil() {
	}

	public static <T> ImmutableList<T> withElementsRetained(List<T> original, Predicate<T> retentionPredicate) {
		ParameterUtil.ensureNotNull(original, "original");
		ParameterUtil.ensureNotNull(retentionPredicate, "retentionPredicate");
		return withElementsRemoved(original, retentionPredicate.negate());
	}

	public static <T> ImmutableList<T> withElementsRemoved(List<T> original, Predicate<T> removalPredicate) {
		ParameterUtil.ensureNotNull(original, "original");
		ParameterUtil.ensureNotNull(removalPredicate, "retentionPredicate");
		List<T> result = new ArrayList<>(original);
		result.removeIf(removalPredicate);
		return ImmutableList.copyOf(result);
	}

	/**
	 * Note: If the comparator is null, then the elements must implement {@link Comparable}.
	 */
	public static <T> ImmutableList<T> sorted(Collection<T> original, Comparator<T> comparator) {
		List<T> list = new ArrayList<>(original);
		list.sort(comparator);
		return ImmutableList.copyOf(list);
	}

}
