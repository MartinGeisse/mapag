package name.martingeisse.mapag.grammar.info;

import java.util.*;

/**
 *
 */
final class FirstSetHelper {

	private final GrammarInfo grammarInfo;
	private boolean hasRun = false;
	private final List<String> todoNonterminals = new ArrayList<>();
	private final Set<String> checkedNonterminals = new HashSet<>();
	private final Set<String> result = new HashSet<>();

	public FirstSetHelper(GrammarInfo grammarInfo, String targetNonterminal) {
		this.grammarInfo = grammarInfo;
		todoNonterminals.add(targetNonterminal);
	}

	static Map<String, Set<String>> runFor(GrammarInfo grammarInfo) {
		Map<String, Set<String>> result = new HashMap<>();
		for (NonterminalInfo nonterminalInfo : grammarInfo.getNonterminalInfos().values()) {
			result.put(nonterminalInfo.getNonterminal(), runFor(grammarInfo, nonterminalInfo.getNonterminal()));
		}
		return result;
	}

	static Set<String> runFor(GrammarInfo grammarInfo, String targetNonterminal) {
		FirstSetHelper helper = new FirstSetHelper(grammarInfo, targetNonterminal);
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
			include(grammarInfo.getNonterminalInfos().get(nonterminalToExpand));
			checkedNonterminals.add(nonterminalToExpand);
		}
	}

	void include(NonterminalInfo nonterminalInfo) {
		for (AlternativeInfo alternativeInfo : nonterminalInfo.getAlternatives()) {
			include(alternativeInfo);
		}
	}

	void include(AlternativeInfo alternativeInfo) {
		for (String symbol : alternativeInfo.getExpansion()) {
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
		if (grammarInfo.isTerminal((symbol))) {
			result.add(symbol);
			return false;
		} else if (grammarInfo.isNonterminal(symbol)) {
			todoNonterminals.add(symbol);
			return grammarInfo.getVanishableNonterminals().contains(symbol);
		} else {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
	}

	public Set<String> getResult() {
		if (!hasRun) {
			throw new IllegalStateException("This helper has not run yet");
		}
		return result;
	}

}
