package name.martingeisse.mapag.codegen.encoder;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateElement;
import name.martingeisse.mapag.util.ListComparator;
import name.martingeisse.mapag.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
final class EncoderComparators {

	static final Comparator<Alternative> alternativeComparator = Comparator.comparing(a -> a.getExpansion(),
			ListComparator.forComparableElements());

	static final Comparator<Pair<String, Alternative>> reductionComparator = (r1, r2) -> {
		int leftResult = r1.getLeft().compareTo(r2.getLeft());
		if (leftResult != 0) {
			return leftResult;
		} else {
			return alternativeComparator.compare(r1.getRight(), r2.getRight());
		}
	};

	static final Comparator<StateElement> stateElementComparator = (e1, e2) -> {
		int result = e1.getLeftSide().compareTo(e2.getLeftSide());
		if (result == 0) {
			result = alternativeComparator.compare(e1.getAlternative(), e2.getAlternative());
		}
		if (result == 0) {
			result = e1.getPosition() - e2.getPosition();
		}
		if (result == 0) {
			result = e1.getFollowTerminal().compareTo(e2.getFollowTerminal());
		}
		return result;
	};

	static final Comparator<List<StateElement>> stateElementListComparator = new ListComparator<>(stateElementComparator);

	static final Comparator<State> stateComparator = (s1, s2) -> {
		List<StateElement> elements1 = new ArrayList<>(s1.getElements());
		Collections.sort(elements1, stateElementComparator);
		List<StateElement> elements2 = new ArrayList<>(s2.getElements());
		Collections.sort(elements2, stateElementComparator);
		return stateElementListComparator.compare(elements1, elements2);
	};

}
