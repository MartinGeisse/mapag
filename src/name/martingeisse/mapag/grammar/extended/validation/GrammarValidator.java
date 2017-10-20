package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class GrammarValidator {

	private final Grammar grammar;
	private final ProductionValidatorFactory productionValidatorFactory;
	public GrammarValidator(Grammar grammar) {
		this(grammar, (terminalNames, nonterminalNames, startSymbol) -> {
			ImmutableSet<String> allSymbolNames = ImmutableSet.<String>builder().addAll(terminalNames).addAll(nonterminalNames).build();
			ExpressionValidator expressionValidator = new ExpressionValidatorImpl(allSymbolNames);
			return new ProductionValidatorImpl(terminalNames, nonterminalNames, startSymbol, expressionValidator);
		});
	}

	GrammarValidator(Grammar grammar, ProductionValidatorFactory productionValidatorFactory) {
		this.grammar = grammar;
		this.productionValidatorFactory = productionValidatorFactory;
	}

	/**
	 * This method may be called only once.
	 */
	public void validate() {

		Set<String> terminalNames = new HashSet<>();
		for (TerminalDeclaration terminalDeclaration : grammar.getTerminalDeclarations()) {
			if (!terminalNames.add(terminalDeclaration.getName())) {
				throw new IllegalStateException("redeclaration of terminal: " + terminalDeclaration.getName());
			}
		}

		Set<String> nonterminalNames = new HashSet<>();
		for (NonterminalDeclaration nonterminalDeclaration : grammar.getNonterminalDeclarations()) {
			if (!nonterminalNames.add(nonterminalDeclaration.getName())) {
				throw new IllegalStateException("redeclaration of nonterminal: " + nonterminalDeclaration.getName());
			}
		}

		{
			Set<String> nameIntersection = new HashSet<>(terminalNames);
			nameIntersection.retainAll(nonterminalNames);
			if (!nameIntersection.isEmpty()) {
				throw new IllegalStateException("redeclaration of terminals as nonterminals: " + nameIntersection);
			}
		}

		Map<String, PrecedenceTable.Entry> precedenceTableEntriesByName = new HashMap<>();
		for (PrecedenceTable.Entry entry : grammar.getPrecedenceTable().getEntries()) {
			for (String name : entry.getTerminalNames()) {
				if (!terminalNames.contains(name)) {
					throw new IllegalStateException("unknown terminal name in precedence table: " + name);
				}
				if (precedenceTableEntriesByName.put(name, entry) != null) {
					throw new IllegalStateException("terminal appears twice in precedence table: " + name);
				}
			}
		}

		if (!nonterminalNames.contains(grammar.getStartNonterminalName())) {
			throw new IllegalStateException("start symbol was not declared as a nonterminal: " + grammar.getStartNonterminalName());
		}

		ProductionValidator productionValidator = productionValidatorFactory.createProductionValidator(
			ImmutableSet.copyOf(terminalNames),
			ImmutableSet.copyOf(nonterminalNames),
			grammar.getStartNonterminalName());
		for (Production production : grammar.getProductions()) {
			productionValidator.validateProduction(production);
		}
		productionValidator.finish();

	}

	interface ProductionValidatorFactory {
		ProductionValidator createProductionValidator(ImmutableSet<String> terminalNames,
													  ImmutableSet<String> nonterminalNames,
													  String startSymbol);
	}

}
