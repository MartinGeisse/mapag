package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.expression.Expression;

/**
 * This object can be re-used for all expressions based on the same set of known symbols -- usually all expressions used
 * in the same grammar.
 */
interface ExpressionValidator {

	void validateExpression(Expression expression, ErrorReporter.ForExpressions errorReporter);

}
