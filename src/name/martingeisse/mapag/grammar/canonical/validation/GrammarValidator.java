package name.martingeisse.mapag.grammar.canonical.validation;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.*;

/**
 *
 */
public class GrammarValidator {

	private final Grammar grammar;

	public GrammarValidator(Grammar grammar) {
		this.grammar = ParameterUtil.ensureNotNull(grammar, "grammar");
	}

	public void validate() {

		{
			Set<String> nameIntersection = new HashSet<>(grammar.getTerminalDefinitions().keySet());
			nameIntersection.retainAll(grammar.getNonterminalDefinitions().keySet());
			if (!nameIntersection.isEmpty()) {
				throw new IllegalStateException("symbol names defined both as terminals and nonterminals: " + nameIntersection);
			}
		}

		validateAssociativityConsistency();

		if (grammar.getNonterminalDefinitions().get(grammar.getStartNonterminalName()) == null) {
			throw new IllegalStateException("no definition found for start nonterminal: " + grammar.getStartNonterminalName());
		}

		validateNonterminalDefinitions();

	}

	protected void validateAssociativityConsistency() {
		new AssociativityConsistencyValidator().validate(grammar.getTerminalDefinitions().values());
	}

	protected void validateNonterminalDefinitions() {
		new NonterminalDefinitionValidator(grammar.getTerminalDefinitions(), grammar.getNonterminalDefinitions()).validate();
	}

}
