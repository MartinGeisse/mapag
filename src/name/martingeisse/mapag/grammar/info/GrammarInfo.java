package name.martingeisse.mapag.grammar.info;

import name.martingeisse.mapag.grammar.Grammar;
import name.martingeisse.mapag.grammar.NonterminalDefinition;
import name.martingeisse.mapag.grammar.Production;
import name.martingeisse.mapag.grammar.TerminalDefinition;

import java.util.*;
import java.util.function.Function;

/**
 *
 */
public final class GrammarInfo {

	private final Grammar grammar;
	private final Map<String, TerminalDefinition> terminalDefinitions;
	private final Map<String, NonterminalDefinition> nonterminalDefinitions;
	private final Map<String, NonterminalInfo> nonterminalInfos;
	private final Set<String> vanishableNonterminals;
	private final Map<String, Set<String>> firstSets;

	public GrammarInfo(Grammar grammar) {
		this.grammar = grammar;

		// map definitions by name for quick access
		terminalDefinitions = mapByName(grammar.getTerminalDefinitions(), t -> t.getName());
		nonterminalDefinitions = mapByName(grammar.getNonterminalDefinitions(), t -> t.getName());

		// collection (normalized) information about nonterminals and their productions
		nonterminalInfos = new HashMap<>();
		for (Production production : grammar.getProductions()) {
			String nonterminal = production.getLeftHandSide();
			NonterminalInfo nonterminalInfo = nonterminalInfos.get(nonterminal);
			if (nonterminalInfo == null) {
				nonterminalInfo = new NonterminalInfo(nonterminal, new ArrayList<>());
			}
			production.getRightHandSide().contributeAlternativesTo(nonterminalInfo.getAlternatives());
		}

		// check which nonterminal can expand to the empty sentence
		vanishableNonterminals = VanishableNonterminalsHelper.runFor(this);

		// compute the first-sets for all nonterminals
		firstSets = FirstSetHelper.runFor(this);

	}

	private static <T> Map<String, T> mapByName(Collection<T> collection, Function<T, String> nameMapper) {
		Map<String, T> result = new HashMap<>();
		for (T element : collection) {
			result.put(nameMapper.apply(element), element);
		}
		return result;
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public Map<String, TerminalDefinition> getTerminalDefinitions() {
		return terminalDefinitions;
	}

	public boolean isTerminal(String name) {
		return terminalDefinitions.containsKey(name);
	}

	public boolean sentenceContainsTerminals(List<String> sentence) {
		for (String symbol : sentence) {
			if (isTerminal(symbol)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, NonterminalDefinition> getNonterminalDefinitions() {
		return nonterminalDefinitions;
	}

	public boolean isNonterminal(String name) {
		return nonterminalDefinitions.containsKey(name);
	}

	public boolean sentenceContainsNonterminals(List<String> sentence) {
		for (String symbol : sentence) {
			if (isNonterminal(symbol)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, NonterminalInfo> getNonterminalInfos() {
		return nonterminalInfos;
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
			if (isTerminal(symbol)) {
				result.add(symbol);
				break;
			} else {
				result.addAll(getFirstSets().get(symbol));
				if (!getVanishableNonterminals().contains(symbol)) {
					break;
				}
			}
		}
		return result;
	}

}
