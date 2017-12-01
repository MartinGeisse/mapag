package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.expression.Expression;

/**
 *
 */
public interface ErrorReporter {

	public void reportError(ErrorLocation location, String message);

	public static interface ForExpressions {
		public void reportError(Expression location, String message);
	}

}
