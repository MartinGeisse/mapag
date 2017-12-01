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

	public void validateExpression(Expression expression, ErrorReporter.ForExpressions errorReporter) {
		if (expression.getName() != null && expression.getName().equals("name")) {
			errorReporter.reportError(expression, "The name '" + expression.getName() +
				"' is forbidden for expressions since IntelliJ PSI classes already use that name for something different.");
			return;
		}
		if (expression instanceof EmptyExpression) {
			// OK, nothing to do
		} else if (expression instanceof OneOrMoreExpression) {
			validateExpression(((OneOrMoreExpression) expression).getOperand(), errorReporter);
		} else if (expression instanceof OptionalExpression) {
			validateExpression(((OptionalExpression) expression).getOperand(), errorReporter);
		} else if (expression instanceof OrExpression) {
			OrExpression orExpression = (OrExpression) expression;
			validateExpression(orExpression.getLeftOperand(), errorReporter);
			validateExpression(orExpression.getRightOperand(), errorReporter);
		} else if (expression instanceof SequenceExpression) {
			SequenceExpression sequenceExpression = (SequenceExpression) expression;
			validateExpression(sequenceExpression.getLeft(), errorReporter);
			validateExpression(sequenceExpression.getRight(), errorReporter);
		} else if (expression instanceof SymbolReference) {
			SymbolReference symbolReference = (SymbolReference) expression;
			String symbolName = symbolReference.getSymbolName();
			if (!knownSymbols.contains(symbolName) && !symbolName.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) {
				errorReporter.reportError(expression, "unknown symbol");
			}
		} else if (expression instanceof ZeroOrMoreExpression) {
			validateExpression(((ZeroOrMoreExpression) expression).getOperand(), errorReporter);
		} else {
			throw new IllegalStateException("found unknown expression type: " + expression);
		}
	}

}
