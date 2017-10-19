package name.martingeisse.mapag.grammar.canonical.validation;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;

import java.util.Map;

/**
 *
 */
class NonterminalDefinitionValidator {

	private final Map<String, TerminalDefinition> terminalDefinitions;
	private final Map<String, NonterminalDefinition> nonterminalDefinitions;

	NonterminalDefinitionValidator(Map<String, TerminalDefinition> terminalDefinitions, Map<String, NonterminalDefinition> nonterminalDefinitions) {
		this.terminalDefinitions = terminalDefinitions;
		this.nonterminalDefinitions = nonterminalDefinitions;
	}

	void validate() {
		for (NonterminalDefinition nonterminalDefinition : nonterminalDefinitions.values()) {
			validate(nonterminalDefinition);
		}
	}

	void validate(NonterminalDefinition nonterminalDefinition) {
		for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
			validate(alternative);
		}
	}

	void validate(Alternative alternative) {
		for (String symbol : alternative.getExpansion()) {
			if (!terminalDefinitions.containsKey(symbol) && !nonterminalDefinitions.containsKey(symbol)) {
				throw new IllegalStateException("unknown symbol in nonterminal expansion: " + symbol);
			}
		}
		if (alternative.getEffectivePrecedenceTerminal() != null) {
			if (!terminalDefinitions.containsKey(alternative.getEffectivePrecedenceTerminal())) {
				throw new IllegalStateException("nonterminal defines precedence using unknown terminal " + alternative.getEffectivePrecedenceTerminal());
			}
		}
	}

}
