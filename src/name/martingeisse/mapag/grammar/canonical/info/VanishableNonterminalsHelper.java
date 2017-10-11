package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.*;

/**
 *
 */
final class VanishableNonterminalsHelper {

	private final Grammar grammar;
	private boolean hasRun = false;
	private Map<String, List<List<String>>> remainingGrammar;
	private Set<String> result;

	VanishableNonterminalsHelper(Grammar grammar) {
		ParameterUtil.ensureNotNull(grammar, "grammar");
		this.grammar = grammar;
	}

	static ImmutableSet<String> runFor(Grammar grammar) {
		VanishableNonterminalsHelper helper = new VanishableNonterminalsHelper(grammar);
		helper.run();
		return helper.getResult();
	}

	void run() {
		if (hasRun) {
			throw new IllegalStateException("This helper has already run");
		}
		initializeGrammar();
		this.result = new HashSet<>();
		work();
		hasRun = true;
	}

	ImmutableSet<String> getResult() {
		if (!hasRun) {
			throw new IllegalStateException("This helper has not run yet");
		}
		return ImmutableSet.copyOf(result);
	}

	private void initializeGrammar() {
		remainingGrammar = new HashMap<>();
		for (Map.Entry<String, NonterminalDefinition> nonterminalDefinitionEntry : grammar.getNonterminalDefinitions().entrySet()) {
			List<List<String>> alternatives = new ArrayList<>();
			for (Alternative alternative : nonterminalDefinitionEntry.getValue().getAlternatives()) {
				List<String> expansion = alternative.getExpansion();
				if (!grammar.sentenceContainsTerminals(expansion)) {
					alternatives.add(new ArrayList<>(expansion));
				}
			}
			if (!alternatives.isEmpty()) {
				remainingGrammar.put(nonterminalDefinitionEntry.getKey(), alternatives);
			}
		}
	}

	private void work() {
		while (true) {
			Set<String> immediatelyVanishableNonterminals = determineImmediatelyVanishableNonterminals();
			if (immediatelyVanishableNonterminals.isEmpty()) {
				break;
			}
			result.addAll(immediatelyVanishableNonterminals);
			for (String nonterminal : immediatelyVanishableNonterminals) {
				vanishNonterminal(nonterminal);
			}
		}

	}

	private Set<String> determineImmediatelyVanishableNonterminals() {
		Set<String> result = new HashSet<>();
		nonterminalLoop: for (Map.Entry<String, List<List<String>>> nonterminalEntry : remainingGrammar.entrySet()) {
			for (List<String> alternative : nonterminalEntry.getValue()) {
				if (alternative.isEmpty()) {
					result.add(nonterminalEntry.getKey());
					continue nonterminalLoop;
				}
			}
		}
		return result;
	}

	private void vanishNonterminal(String nonterminalToVanish) {
		remainingGrammar.remove(nonterminalToVanish);
		remainingGrammar.values().forEach(alternatives -> {
			alternatives.forEach(alternative -> {
				alternative.removeIf(symbol -> symbol.equals(nonterminalToVanish));
			});
		});
	}

}
