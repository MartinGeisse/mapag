package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.Production;
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

	public void validateProduction(Production production) {
		String leftHandSide = production.getLeftHandSide();
		if (!nonterminalNames.contains(leftHandSide)) {
			throw new IllegalStateException("left-hand symbol in production was not declared as a nonterminal: " + leftHandSide);
		}
		if (leftHandSide.equals(startSymbol)) {
			foundProductionForStartSymbol = true;
		}
		for (Alternative alternative : production.getAlternatives()) {
			expressionValidator.validateExpression(alternative.getExpression());
			if (alternative.getPrecedenceDefiningTerminal() != null) {
				if (!terminalNames.contains(alternative.getPrecedenceDefiningTerminal())) {
					throw new IllegalStateException("unknown terminal name '" +
						alternative.getPrecedenceDefiningTerminal() + " in rule precedence specification for nonterminal " + leftHandSide);
				}
			}
			// TODO validate resolve blocks -- only known terminals; no duplicate terminals
		}
	}

	public void finish() {
		if (!foundProductionForStartSymbol) {
			throw new IllegalStateException("no production found for start symbol");
		}
	}

}
