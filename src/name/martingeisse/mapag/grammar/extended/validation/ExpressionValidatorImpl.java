package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.expression.*;

final class ExpressionValidatorImpl implements ExpressionValidator {

	private final ImmutableSet<String> knownSymbols;

	ExpressionValidatorImpl(ImmutableSet<String> knownSymbols) {
		this.knownSymbols = knownSymbols;
	}

	public void validateExpression(Expression expression) {
		if (expression instanceof OneOrMoreExpression) {
			validateExpression(((OneOrMoreExpression) expression).getOperand());
		} else if (expression instanceof OptionalExpression) {
			validateExpression(((OptionalExpression) expression).getOperand());
		} else if (expression instanceof OrExpression) {
			OrExpression orExpression = (OrExpression) expression;
			validateExpression(orExpression.getLeftOperand());
			validateExpression(orExpression.getRightOperand());
		} else if (expression instanceof SequenceExpression) {
			SequenceExpression sequenceExpression = (SequenceExpression) expression;
			validateExpression(sequenceExpression.getLeft());
			validateExpression(sequenceExpression.getRight());
		} else if (expression instanceof SymbolReference) {
			SymbolReference symbolReference = (SymbolReference) expression;
			String symbolName = symbolReference.getSymbolName();
			if (!knownSymbols.contains(symbolName)) {
				throw new IllegalStateException("unknown symbol used on the right-hand side of a production: " + symbolReference.getSymbolName());
			}
		} else if (expression instanceof ZeroOrMoreExpression) {
			validateExpression(((ZeroOrMoreExpression) expression).getOperand());
		} else {
			throw new IllegalStateException("found unknown expression type: " + expression);
		}
	}

}
