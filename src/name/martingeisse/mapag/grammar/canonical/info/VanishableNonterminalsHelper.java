package name.martingeisse.mapag.grammar.canonical.info;

import java.util.*;

/**
 *
 */
final class VanishableNonterminalsHelper {

	private final GrammarInfo grammarInfo;
	private boolean hasRun = false;
	private Map<String, List<List<String>>> remainingGrammar;
	private Set<String> result;

	VanishableNonterminalsHelper(GrammarInfo grammarInfo) {
		this.grammarInfo = grammarInfo;
	}

	static Set<String> runFor(GrammarInfo grammarInfo) {
		VanishableNonterminalsHelper helper = new VanishableNonterminalsHelper(grammarInfo);
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

	Set<String> getResult() {
		if (!hasRun) {
			throw new IllegalStateException("This helper has not run yet");
		}
		return result;
	}

	private void initializeGrammar() {
		remainingGrammar = new HashMap<>();
		for (Map.Entry<String, NonterminalInfo> nonterminalInfoEntry : grammarInfo.getNonterminalInfos().entrySet()) {
			List<List<String>> alternatives = new ArrayList<>();
			for (AlternativeInfo alternativeInfo : nonterminalInfoEntry.getValue().getAlternatives()) {
				List<String> expansion = alternativeInfo.getExpansion();
				if (!grammarInfo.sentenceContainsTerminals(expansion)) {
					alternatives.add(new ArrayList<>(expansion));
				}
			}
			if (!alternatives.isEmpty()) {
				remainingGrammar.put(nonterminalInfoEntry.getKey(), alternatives);
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


		/*
		TODO remove if the above works

		Iterator<Map.Entry<String, List<List<String>>>> nonterminalIterator = remainingGrammar.entrySet().iterator();
		while (nonterminalIterator.hasNext()) {
			Map.Entry<String, List<List<String>>> nonterminalEntry = nonterminalIterator.next();
			Iterator<List<String>> alternativeIterator = nonterminalEntry.getValue().iterator();
			while (alternativeIterator.hasNext()) {
				List<String> alternative = alternativeIterator.next();
				alternative.removeIf(symbol -> symbol.equals(nonterminalToVanish));
			}
			if (nonterminalEntry.getValue().isEmpty()) {
				nonterminalIterator.remove();
			}
		}
		*/
	}

}
