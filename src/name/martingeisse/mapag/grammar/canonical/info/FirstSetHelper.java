package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.*;

/**
 *
 */
final class FirstSetHelper {

	private final Grammar grammar;
	private final ImmutableSet<String> vanishableNonterminals;
	private final List<String> todoNonterminals = new ArrayList<>();
	private final Set<String> checkedNonterminals = new HashSet<>();
	private final Set<String> result = new HashSet<>();
	private boolean hasRun = false;

	public FirstSetHelper(Grammar grammar, ImmutableSet<String> vanishableNonterminals, String targetNonterminal) {
		this.grammar = ParameterUtil.ensureNotNull(grammar, "grammar");
		this.vanishableNonterminals = vanishableNonterminals;
		todoNonterminals.add(ParameterUtil.ensureNotNullOrEmpty(targetNonterminal, "targetNonterminal"));
	}

	static ImmutableMap<String, ImmutableSet<String>> runFor(Grammar grammar, ImmutableSet<String> vanishableNonterminals) {
		ParameterUtil.ensureNotNull(grammar, "grammar");
		ParameterUtil.ensureNotNull(vanishableNonterminals, "vanishableNonterminals");
		Map<String, ImmutableSet<String>> result = new HashMap<>();
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			result.put(nonterminalDefinition.getName(), runFor(grammar, vanishableNonterminals, nonterminalDefinition.getName()));
		}
		return ImmutableMap.copyOf(result);
	}

	static ImmutableSet<String> runFor(Grammar grammar, ImmutableSet<String> vanishableNonterminals, String targetNonterminal) {
		ParameterUtil.ensureNotNull(grammar, "grammar");
		ParameterUtil.ensureNotNull(vanishableNonterminals, "vanishableNonterminals");
		ParameterUtil.ensureNotNull(targetNonterminal, "targetNonterminal");
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
		String nonterminalToExpand = todoNonterminals.remove(todoNonterminals.size() - 1);
		// don't include the same nonterminal's first-set twice recursively -- this can't add more terminals in any
		// case, but would end up in an infinite loop.
		if (!checkedNonterminals.contains(nonterminalToExpand)) {
			include(grammar.getNonterminalDefinitions().get(nonterminalToExpand));
			checkedNonterminals.add(nonterminalToExpand);
		}
	}

	void include(NonterminalDefinition nonterminalDefinition) {
		for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
			include(alternative);
		}
	}

	void include(Alternative alternative) {
		for (String symbol : alternative.getExpansion()) {
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
	boolean include(String symbol) {
		if (grammar.isTerminal((symbol))) {
			result.add(symbol);
			return false;
		} else if (grammar.isNonterminal(symbol)) {
			todoNonterminals.add(symbol);
			return vanishableNonterminals.contains(symbol);
		} else {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
	}

	public ImmutableSet<String> getResult() {
		if (!hasRun) {
			throw new IllegalStateException("This helper has not run yet");
		}
		return ImmutableSet.copyOf(result);
	}

}
