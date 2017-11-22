package name.martingeisse.mapag.util;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * TODO use this class where it makes sense
 */
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

}
