package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.Production;

class ProductionValidatorImpl implements ProductionValidator {

	private final ImmutableSet<String> terminalNames;
	private final ImmutableSet<String> nonterminalNames;
	private final String startSymbol;
	private final ExpressionValidator expressionValidator;
	private boolean foundProductionForStartSymbol = false;

	ProductionValidatorImpl(ImmutableSet<String> terminalNames, ImmutableSet<String> nonterminalNames, String startSymbol, ExpressionValidator expressionValidator) {
		this.terminalNames = terminalNames;
		this.nonterminalNames = nonterminalNames;
		this.expressionValidator = expressionValidator;
		this.startSymbol = startSymbol;
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
			if (alternative.getPrecedenceSpecificationType() == Alternative.PrecedenceSpecificationType.EXPLICIT) {
				if (!terminalNames.contains(alternative.getPrecedenceSpecification())) {
					throw new IllegalStateException("unknown terminal name '" +
							alternative.getPrecedenceSpecification() + " in rule precedence specification for nonterminal " + leftHandSide);
				}
			}
		}
	}

	public void finish() {
		if (!foundProductionForStartSymbol) {
			throw new IllegalStateException("no production found for start symbol");
		}
	}

}
