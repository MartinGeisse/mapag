package name.martingeisse.mapag.grammar.canonical.validation;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.AlternativeConflictResolver;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.Map;

/**
 *
 */
class NonterminalDefinitionValidator {

	private final Map<String, TerminalDefinition> terminalDefinitions;
	private final Map<String, NonterminalDefinition> nonterminalDefinitions;

	NonterminalDefinitionValidator(Map<String, TerminalDefinition> terminalDefinitions, Map<String, NonterminalDefinition> nonterminalDefinitions) {
		this.terminalDefinitions = ParameterUtil.ensureNotNull(terminalDefinitions, "terminalDefinitions");
		this.nonterminalDefinitions = ParameterUtil.ensureNotNull(nonterminalDefinitions, "nonterminalDefinitions");
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
			if (!terminalDefinitions.containsKey(symbol) && !nonterminalDefinitions.containsKey(symbol) && !symbol.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) {
				throw new IllegalStateException("unknown symbol in nonterminal expansion: " + symbol);
			}
		}
		if (alternative.getConflictResolver() != null) {
			AlternativeConflictResolver conflictResolver = alternative.getConflictResolver();
			if (conflictResolver.getEffectivePrecedenceTerminal() != null) {
				if (!terminalDefinitions.containsKey(conflictResolver.getEffectivePrecedenceTerminal())) {
					throw new IllegalStateException("nonterminal defines precedence using unknown terminal " + conflictResolver.getEffectivePrecedenceTerminal());
				}
			}
			if (conflictResolver.getTerminalToResolution() != null) {
				for (String terminal : conflictResolver.getTerminalToResolution().keySet()) {
					if (!terminalDefinitions.containsKey(terminal)) {
						throw new IllegalStateException("nonterminal defines conflict resolution using unknown terminal " + terminal);
					}
				}
			}
		}
	}

}
