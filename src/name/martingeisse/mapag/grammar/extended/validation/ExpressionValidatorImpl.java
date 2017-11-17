package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.util.ParameterUtil;

final class ExpressionValidatorImpl implements ExpressionValidator {

	private final ImmutableSet<String> knownSymbols;

	ExpressionValidatorImpl(ImmutableSet<String> knownSymbols) {
		this.knownSymbols = ParameterUtil.ensureNotNullOrEmpty(knownSymbols, "knownSymbols");
		ParameterUtil.ensureNoNullOrEmptyElement(knownSymbols, "knownSymbols");
	}

	public void validateExpression(Expression expression) {
		if (expression.getName() != null && expression.getName().equals("name")) {
			throw new IllegalStateException("The name 'name' is forbidden for expressions since IntelliJ PSI classes already use that name for something different.");
		}
		if (expression instanceof EmptyExpression) {
			// OK, nothing to do
		} else if (expression instanceof OneOrMoreExpression) {
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
			if (!knownSymbols.contains(symbolName) && !symbolName.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) {
				throw new IllegalStateException("unknown symbol used on the right-hand side of a production: " + symbolReference.getSymbolName());
			}
		} else if (expression instanceof ZeroOrMoreExpression) {
			validateExpression(((ZeroOrMoreExpression) expression).getOperand());
		} else {
			throw new IllegalStateException("found unknown expression type: " + expression);
		}
	}

}
