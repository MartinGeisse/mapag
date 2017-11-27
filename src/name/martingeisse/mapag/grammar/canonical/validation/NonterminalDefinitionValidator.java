package name.martingeisse.mapag.grammar.canonical.validation;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.*;
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
		for (ExpansionElement expansionElement : alternative.getExpansion().getElements()) {
			String symbol = expansionElement.getSymbol();
			if (!terminalDefinitions.containsKey(symbol) && !nonterminalDefinitions.containsKey(symbol) && !symbol.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) {
				throw new IllegalStateException("unknown symbol in nonterminal expansion: " + symbol);
			}
		}
		if (alternative.getAttributes() != null) {
			AlternativeAttributes attributes = alternative.getAttributes();
			if (attributes.getEffectivePrecedenceTerminal() != null) {
				if (!terminalDefinitions.containsKey(attributes.getEffectivePrecedenceTerminal())) {
					throw new IllegalStateException("nonterminal defines precedence using unknown terminal " + attributes.getEffectivePrecedenceTerminal());
				}
			}
			if (attributes.getTerminalToResolution() != null) {
				for (String terminal : attributes.getTerminalToResolution().keySet()) {
					if (!terminalDefinitions.containsKey(terminal)) {
						throw new IllegalStateException("nonterminal defines conflict resolution using unknown terminal " + terminal);
					}
				}
			}
		}
	}

}
