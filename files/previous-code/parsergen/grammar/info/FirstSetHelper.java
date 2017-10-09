package name.martingeisse.parsergen.grammar.info;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.parsergen.grammar.*;

import java.util.*;

/**
 *
 */
final class FirstSetHelper {

	private final Grammar grammar;
	private final ImmutableSet<Nonterminal> vanishableNonterminals;
	private boolean hasRun = false;
	private final List<Nonterminal> todoNonterminals = new ArrayList<>();
	private final Set<Nonterminal> checkedNonterminals = new HashSet<>();
	private final Set<Terminal> result = new HashSet<>();

	public FirstSetHelper(Grammar grammar, ImmutableSet<Nonterminal> vanishableNonterminals, Nonterminal targetNonterminal) {
		this.grammar = grammar;
		this.vanishableNonterminals = vanishableNonterminals;
		todoNonterminals.add(targetNonterminal);
	}

	static ImmutableMap<Nonterminal, ImmutableSet<Terminal>> runFor(Grammar grammar, ImmutableSet<Nonterminal> vanishableNonterminals) {
		Map<Nonterminal, ImmutableSet<Terminal>> result = new HashMap<>();
		for (Rule rule : grammar.getRules()) {
			result.put(rule.getNonterminal(), runFor(grammar, vanishableNonterminals, rule.getNonterminal()));
		}
		return ImmutableMap.copyOf(result);
	}

	static ImmutableSet<Terminal> runFor(Grammar grammar, ImmutableSet<Nonterminal> vanishableNonterminals, Nonterminal targetNonterminal) {
		FirstSetHelper helper = new FirstSetHelper(grammar, vanishableNonterminals, targetNonterminal);
		helper.run();
		return helper.getResult();
	}

	public void run() {
		if (hasRun) {
			throw new IllegalStateException("This helper has already run");
		}
		while (!todoNonterminals.isEmpty()) {
			processNextTodo();
		}
		hasRun = true;
	}

	void processNextTodo() {
		Nonterminal nonterminalToExpand = todoNonterminals.remove(todoNonterminals.size() - 1);
		// don't include the same nonterminal's first-set twice recursively -- this can't add more terminals in any
		// case, but would end up in an infinite loop.
		if (!checkedNonterminals.contains(nonterminalToExpand)) {
			include(grammar.getRuleFor(nonterminalToExpand));
			checkedNonterminals.add(nonterminalToExpand);
		}
	}

	void include(Rule rule) {
		for (Alternative alternative : rule.getAlternatives()) {
			include(alternative);
		}
	}

	void include(Alternative alternative) {
		for (Symbol symbol : alternative.getExpansionSymbols()) {
			if (!include(symbol)) {
				return;
			}
		}
		// If this reaches the end of the loop for the initial targetNonterminal, then it's correct to
		// stop becasue any allowed terminal would go into the follow-set, not in the first-set.
		// If this reaches the end of the loop for any other nonterminal, then that nonterminal is
		// vanishable and we already added the following symbol to the to-do list when we added
		// that nonterminal itself.
		// So, in either case, if the loop reaches the end of the alternative's expanion, it's okay
		// to just stop the same way as if the loop was stopped in the middle.
	}

	// returns true to continue with further symbols from the current alternative, false to stop and go on with the
	// next alternative.
	boolean include(Symbol symbol) {
		if (symbol instanceof Terminal) {
			result.add((Terminal)symbol);
			return false;
		} else {
			Nonterminal expansionNonterminal = (Nonterminal)symbol;
			todoNonterminals.add(expansionNonterminal);
			return vanishableNonterminals.contains(expansionNonterminal);
		}
	}

	public ImmutableSet<Terminal> getResult() {
		if (!hasRun) {
			throw new IllegalStateException("This helper has not run yet");
		}
		return ImmutableSet.copyOf(result);
	}

}
