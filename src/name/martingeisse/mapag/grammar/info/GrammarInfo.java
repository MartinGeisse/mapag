package name.martingeisse.mapag.grammar.info;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.parsergen.grammar.*;

import java.util.*;

/**
 *
 */
public final class GrammarInfo {

	private final Grammar grammar;
	private final ImmutableSet<Nonterminal> vanishableNonterminals;
	private final ImmutableMap<Nonterminal, ImmutableSet<Terminal>> firstSets;

	public GrammarInfo(Grammar grammar) {
		this.grammar = grammar;
		this.vanishableNonterminals = VanishableNonterminalsHelper.runFor(grammar);
		this.firstSets = FirstSetHelper.runFor(grammar, vanishableNonterminals);
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public ImmutableSet<Nonterminal> getVanishableNonterminals() {
		return vanishableNonterminals;
	}

	/**
	 * Checks if the specified sentence is vanishable. This is the case if every single symbol is vanishable.
	 * Especially, every symbol must be a nonterminal.
	 */
	public boolean isSentenceVanishable(List<Symbol> sentence) {
		for (Symbol symbol : sentence) {
			if (!getVanishableNonterminals().contains(symbol)) {
				return false;
			}
		}
		return true;
	}

	public ImmutableMap<Nonterminal, ImmutableSet<Terminal>> getFirstSets() {
		return firstSets;
	}

	public ImmutableSet<Terminal> determineFirstSetForSentence(List<Symbol> sentence) {
		Set<Terminal> result = new HashSet<>();
		for (Symbol symbol : sentence) {
			if (symbol instanceof Terminal) {
				result.add((Terminal) symbol);
				break;
			} else {
				result.addAll(getFirstSets().get((Nonterminal) symbol));
				if (!getVanishableNonterminals().contains(symbol)) {
					break;
				}
			}
		}
		return ImmutableSet.copyOf(result);
	}

}
