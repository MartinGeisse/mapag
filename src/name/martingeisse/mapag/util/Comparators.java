package name.martingeisse.mapag.util;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateElement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public final class Comparators {

	public static final Comparator<ExpansionElement> expansionElementComparator = (e1, e2) -> {
		int result = e1.getSymbol().compareTo(e2.getSymbol());
		if (result != 0) {
			return result;
		}
		if (e1.getExpressionName() == e2.getExpressionName()) {
			return 0;
		}
		if (e1.getExpressionName() == null) {
			return -1;
		}
		if (e2.getExpressionName() == null) {
			return 1;
		}
		return (e1.getExpressionName().compareTo(e2.getExpressionName()));
	};

	public static final Comparator<Alternative> alternativeComparator = Comparator.comparing(
		a -> a.getExpansion().getElements(),
		new ListComparator<>(expansionElementComparator));

	public static final Comparator<Pair<String, Alternative>> nonterminalAlternativeComparator = (r1, r2) -> {
		int leftResult = r1.getLeft().compareTo(r2.getLeft());
		if (leftResult != 0) {
			return leftResult;
		} else {
			return alternativeComparator.compare(r1.getRight(), r2.getRight());
		}
	};

	public static final Comparator<StateElement> stateElementComparator = (e1, e2) -> {
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

	public static final Comparator<List<StateElement>> stateElementListComparator = new ListComparator<>(stateElementComparator);

	public static final Comparator<State> stateComparator = (s1, s2) -> {
		ImmutableList<StateElement> elements1 = ListUtil.sorted(s1.getElements(), stateElementComparator);
		ImmutableList<StateElement> elements2 = ListUtil.sorted(s2.getElements(), stateElementComparator);
		return stateElementListComparator.compare(elements1, elements2);
	};

}
