package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.validation.GrammarValidator;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public final class GrammarInfo {

	private final Grammar grammar;
	private final ImmutableSet<String> vanishableNonterminals;
	private final ImmutableMap<String, ImmutableSet<String>> firstSets;

	public GrammarInfo(Grammar grammar) {
		ParameterUtil.ensureNotNull(grammar, "grammar");
		new GrammarValidator(grammar).validate();
		this.grammar = grammar;
		this.vanishableNonterminals = VanishableNonterminalsHelper.runFor(grammar);
		this.firstSets = FirstSetHelper.runFor(grammar, vanishableNonterminals);
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public ImmutableSet<String> getVanishableNonterminals() {
		return vanishableNonterminals;
	}

	/**
	 * Checks if the specified sentence is vanishable. This is the case if every single symbol is vanishable.
	 * Especially, every symbol must be a nonterminal.
	 */
	public boolean isSentenceVanishable(List<String> sentence) {
		ParameterUtil.ensureNotNull(sentence, "sentence");
		for (String symbol : sentence) {
			ParameterUtil.ensureNotNull(symbol, "sentence element");
			if (!getVanishableNonterminals().contains(symbol)) {
				return false;
			}
		}
		return true;
	}

	public ImmutableMap<String, ImmutableSet<String>> getFirstSets() {
		return firstSets;
	}

	public ImmutableSet<String> determineFirstSetForSentence(List<String> sentence) {
		ParameterUtil.ensureNotNull(sentence, "sentence");
		Set<String> result = new HashSet<>();
		for (String symbol : sentence) {
			ParameterUtil.ensureNotNull(symbol, "sentence element");
			if (grammar.isTerminal(symbol)) {
				result.add(symbol);
				break;
			} else if (grammar.isNonterminal(symbol)) {
				result.addAll(getFirstSets().get(symbol));
				if (!getVanishableNonterminals().contains(symbol)) {
					break;
				}
			} else if (symbol.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) {
				// The first-set of the error symbol is empty, which makes it useless to put the error symbol after
				// a nonterminal on a right-hand side of a production -- that nonterminal can never be reduced
				// since no possible follow-terminal, and thus no state elements, exist for it.
				break;
			} else {
				throw new IllegalArgumentException("unknown symbol name in sentence: " + symbol);
			}
		}
		return ImmutableSet.copyOf(result);
	}

}
