package name.martingeisse.mapag.grammar.canonical.validation;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class GrammarValidator {

	private final Grammar grammar;

	public GrammarValidator(Grammar grammar) {
		this.grammar = grammar;
	}

	public void validate() {

		{
			Set<String> nameIntersection = new HashSet<>(grammar.getTerminalDefinitions().keySet());
			nameIntersection.retainAll(grammar.getNonterminalDefinitions().keySet());
			if (!nameIntersection.isEmpty()) {
				throw new IllegalStateException("symbol names defined both as terminals and nonterminals: " + nameIntersection);
			}
		}

		if (grammar.getNonterminalDefinitions().get(grammar.getStartNonterminalName()) == null) {
			throw new IllegalStateException("no definition found for start nonterminal: " + grammar.getStartNonterminalName());
		}

		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
				for (String symbol : alternative.getExpansion()) {
					if (!grammar.getTerminalDefinitions().containsKey(symbol) && !grammar.getNonterminalDefinitions().containsKey(symbol)) {
						throw new IllegalStateException("unknown symbol in nonterminal expansion: " + symbol);
					}
				}
			}
		}

	}

}
