package name.martingeisse.mapag.grammar.canonical.info;

import name.martingeisse.mapag.grammar.canonical.Grammar;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class GrammarInfo {

	private final Grammar grammar;
	private final Set<String> vanishableNonterminals;
	private final Map<String, Set<String>> firstSets;

	public GrammarInfo(Grammar grammar) {
		this.grammar = grammar;
		this.vanishableNonterminals = VanishableNonterminalsHelper.runFor(this);
		this.firstSets = FirstSetHelper.runFor(this);
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public Set<String> getVanishableNonterminals() {
		return vanishableNonterminals;
	}

	/**
	 * Checks if the specified sentence is vanishable. This is the case if every single symbol is vanishable.
	 * Especially, every symbol must be a nonterminal.
	 */
	public boolean isSentenceVanishable(List<String> sentence) {
		for (String symbol : sentence) {
			if (!getVanishableNonterminals().contains(symbol)) {
				return false;
			}
		}
		return true;
	}

	public Map<String, Set<String>> getFirstSets() {
		return firstSets;
	}

	public Set<String> determineFirstSetForSentence(List<String> sentence) {
		Set<String> result = new HashSet<>();
		for (String symbol : sentence) {
			if (grammar.isTerminal(symbol)) {
				result.add(symbol);
				break;
			} else if (grammar.isNonterminal(symbol)) {
				result.addAll(getFirstSets().get(symbol));
				if (!getVanishableNonterminals().contains(symbol)) {
					break;
				}
			} else {
				throw new IllegalArgumentException("unknown symbol: " + symbol);
			}
		}
		return result;
	}

}
