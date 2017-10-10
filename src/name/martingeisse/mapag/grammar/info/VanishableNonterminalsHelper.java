package name.martingeisse.mapag.grammar.info;

import name.martingeisse.parsergen.grammar.Alternative;
import name.martingeisse.parsergen.grammar.Grammar;
import name.martingeisse.parsergen.grammar.Nonterminal;
import name.martingeisse.parsergen.grammar.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
final class VanishableNonterminalsHelper {

	private final Grammar originalGrammar;
	private boolean hasRun = false;
	private Set<Nonterminal> result;

	VanishableNonterminalsHelper(Grammar grammar) {
		this.originalGrammar = grammar;
	}

	static Set<Nonterminal> runFor(Grammar grammar) {
		VanishableNonterminalsHelper helper = new VanishableNonterminalsHelper(grammar);
		helper.run();
		return helper.getResult();
	}

	void run() {
		if (hasRun) {
			throw new IllegalStateException("This helper has already run");
		}
		this.result = determineVanishableNonterminals(originalGrammar);
		hasRun = true;
	}

	Set<Nonterminal> getResult() {
		if (!hasRun) {
			throw new IllegalStateException("This helper has not run yet");
		}
		return result;
	}

	private static Rule determineWithoutAlternativesContainingTerminals(Rule rule) {
		List<Alternative> filteredAlternatives = new ArrayList<>();
		for (Alternative alternative : rule.getAlternatives()) {
			if (!alternative.expansionContainsTerminals()) {
				filteredAlternatives.add(alternative);
			}
		}
		return (filteredAlternatives.isEmpty() ? null : new Rule(rule.getNonterminal(), filteredAlternatives));
	}

	private static Grammar determineWithoutAlternativesContainingTerminals(Grammar grammar) {
		List<Rule> filteredRules = new ArrayList<>();
		for (Rule rule : grammar.getRules()) {
			Rule filteredRule = determineWithoutAlternativesContainingTerminals(rule);
			if (filteredRule != null) {
				filteredRules.add(filteredRule);
			}
		}
		return new Grammar(grammar.getAlphabet(), grammar.getStartSymbol(), List.copyOf(filteredRules));
	}

	private static Set<Nonterminal> determineImmediatelyVanishableNonterminals(Grammar grammar) {
		Set<Nonterminal> result = new HashSet<>();
		for (Rule rule : grammar.getRules()) {
			if (rule.isImmediatelyVanishable()) {
				result.add(rule.getNonterminal());
			}
		}
		return Set.copyOf(result);
	}

	private static Set<Nonterminal> determineVanishableNonterminals(Grammar grammar) {
		grammar = determineWithoutAlternativesContainingTerminals(grammar);
		Set<Nonterminal> result = new HashSet<>();
		while (true) {
			Set<Nonterminal> immediatelyVanishableNonterminals = determineImmediatelyVanishableNonterminals(grammar);
			if (immediatelyVanishableNonterminals.isEmpty()) {
				break;
			}
			result.addAll(immediatelyVanishableNonterminals);
			for (Nonterminal nonterminal : immediatelyVanishableNonterminals) {
				grammar = grammar.vanishNonterminal(nonterminal);
			}
		}
		return Set.copyOf(result);
	}
}
