package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.expression.Expression;

/**
 *
 */
public interface ErrorReporter {

	public void reportError(ErrorLocation location, String message);

	public static final ErrorReporter EXCEPTION_THROWER = (location, message) -> {
		throw new IllegalStateException("invalid grammar: " + message + " at " + location);
	};

	public static interface ForExpressions {

		public void reportError(Expression location, String message);

		public static final ForExpressions EXCEPTION_THROWER = (location, message) -> {
			throw new IllegalStateException("invalid grammar: " + message + " at " + location);
		};

	}

}
