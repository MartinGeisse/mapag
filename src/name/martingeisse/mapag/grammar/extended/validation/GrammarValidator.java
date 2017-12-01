package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;

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
	public void validate(ErrorReporter errorReporter) {

		Set<String> terminalNames = new HashSet<>();
		for (TerminalDeclaration terminalDeclaration : grammar.getTerminalDeclarations()) {
			String name = terminalDeclaration.getName();
			if (!terminalNames.add(name)) {
				errorReporter.reportError(new ErrorLocation.TerminalDeclaration(terminalDeclaration), "redeclaration of terminal " + name);
			}
		}

		Set<String> nonterminalNames = new HashSet<>();
		for (Production production : grammar.getProductions()) {
			String nonterminal = production.getLeftHandSide();
			if (terminalNames.contains(nonterminal)) {
				errorReporter.reportError(new ErrorLocation.ProductionLeftHandSide(production), "redeclaration of terminal " + nonterminal + " as nonterminal");
			} else if (!nonterminalNames.add(nonterminal)) {
				errorReporter.reportError(new ErrorLocation.ProductionLeftHandSide(production), "redeclaration of nonterminal " + nonterminal);
			}
		}

		Map<String, PrecedenceTable.Entry> precedenceTableEntriesByName = new HashMap<>();
		for (PrecedenceTable.Entry entry : grammar.getPrecedenceTable().getEntries()) {
			int symbolIndex = 0;
			for (String name : entry.getTerminalNames()) {
				if (!terminalNames.contains(name)) {
					errorReporter.reportError(new ErrorLocation.PrecedenceTableEntry(entry, symbolIndex), "unknown terminal name: " + name);
				} else if (precedenceTableEntriesByName.put(name, entry) != null) {
					errorReporter.reportError(new ErrorLocation.PrecedenceTableEntry(entry, symbolIndex), "terminal appears twice in precedence table: " + name);
				}
				symbolIndex++;
			}
		}

		if (!nonterminalNames.contains(grammar.getStartNonterminalName())) {
			errorReporter.reportError(new ErrorLocation.StartSymbol(), "start symbol was not declared as a nonterminal");
		}

		ProductionValidator productionValidator = productionValidatorFactory.createProductionValidator(
			ImmutableSet.copyOf(terminalNames),
			ImmutableSet.copyOf(nonterminalNames),
			grammar.getStartNonterminalName());
		for (Production production : grammar.getProductions()) {
			productionValidator.validateProduction(production, errorReporter);
		}
		productionValidator.finish(errorReporter);

	}

	interface ProductionValidatorFactory {
		ProductionValidator createProductionValidator(ImmutableSet<String> terminalNames,
													  ImmutableSet<String> nonterminalNames,
													  String startSymbol);
	}

}
