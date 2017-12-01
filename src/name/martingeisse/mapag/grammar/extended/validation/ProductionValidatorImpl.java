package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.util.ParameterUtil;

class ProductionValidatorImpl implements ProductionValidator {

	private final ImmutableSet<String> terminalNames;
	private final ImmutableSet<String> nonterminalNames;
	private final String startSymbol;
	private final ExpressionValidator expressionValidator;
	private boolean foundProductionForStartSymbol = false;

	ProductionValidatorImpl(ImmutableSet<String> terminalNames, ImmutableSet<String> nonterminalNames, String startSymbol, ExpressionValidator expressionValidator) {
		this.terminalNames = ParameterUtil.ensureNotNullOrEmpty(terminalNames, "terminalNames");
		ParameterUtil.ensureNoNullOrEmptyElement(terminalNames, "terminalNames");
		this.nonterminalNames = ParameterUtil.ensureNotNullOrEmpty(nonterminalNames, "nonterminalNames");
		ParameterUtil.ensureNoNullOrEmptyElement(nonterminalNames, "nonterminalNames");
		this.startSymbol = ParameterUtil.ensureNotNullOrEmpty(startSymbol, "startSymbol");
		this.expressionValidator = ParameterUtil.ensureNotNull(expressionValidator, "expressionValidator");
	}

	public void validateProduction(Production production, ErrorReporter errorReporter) {
		String leftHandSide = production.getLeftHandSide();
		if (!nonterminalNames.contains(leftHandSide)) {
			errorReporter.reportError(new ErrorLocation.ProductionLeftHandSide(production), leftHandSide + " was not declared as a nonterminal");
		}
		if (leftHandSide.equals(startSymbol)) {
			foundProductionForStartSymbol = true;
		}
		for (Alternative alternative : production.getAlternatives()) {
			ErrorReporter.ForExpressions subReporter = (location, message) -> errorReporter.reportError(
				new ErrorLocation.Expression(production, alternative, location), message);
			expressionValidator.validateExpression(alternative.getExpression(), subReporter);
			if (alternative.getPrecedenceDefiningTerminal() != null) {
				if (!terminalNames.contains(alternative.getPrecedenceDefiningTerminal())) {
					errorReporter.reportError(new ErrorLocation.PrecedenceDefiningTerminal(production, alternative), "unknown terminal name: " + alternative.getPrecedenceDefiningTerminal());
				}
			}
			if (alternative.getResolveBlock() != null) {
				for (ResolveDeclaration resolveDeclaration : alternative.getResolveBlock().getResolveDeclarations()) {
					int symbolIndex = 0;
					for (String symbol : resolveDeclaration.getTerminals()) {
						if (!terminalNames.contains(symbol)) {
							ErrorLocation location = new ErrorLocation.SymbolInResolveDeclaration(production, alternative, resolveDeclaration, symbolIndex);
							errorReporter.reportError(location, "unknown terminal: " + symbol);
						}
						symbolIndex++;
					}
				}
			}
		}
	}

	public void finish(ErrorReporter errorReporter) {
		if (!foundProductionForStartSymbol) {
			errorReporter.reportError(new ErrorLocation.StartSymbol(), "no production found for start symbol");
		}
	}

}
