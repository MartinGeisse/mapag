package name.martingeisse.mapag.util;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public final class ListUtil {

	// prevent instantiation
	private ListUtil() {
	}

	public static <T> ImmutableList<T> withElementsRetained(List<T> original, Predicate<T> retentionPredicate) {
		return withElementsRemoved(original, retentionPredicate.negate());
	}

	public static <T> ImmutableList<T> withElementsRemoved(List<T> original, Predicate<T> removalPredicate) {
		List<T> result = new ArrayList<>(original);
		result.removeIf(removalPredicate);
		return ImmutableList.copyOf(result);
	}

}
