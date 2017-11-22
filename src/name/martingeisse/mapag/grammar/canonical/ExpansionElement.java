package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class ExpansionElement {

	private final String symbol;
	private final String expressionName;

	public ExpansionElement(String symbol, String expressionName) {
		this.symbol = ParameterUtil.ensureNotNullOrEmpty(symbol, "symbol");
		if (expressionName != null && expressionName.isEmpty()) {
			throw new IllegalArgumentException("expressionName cannot be empty (pass null for unnamed expressions)");
		}
		this.expressionName = expressionName;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getExpressionName() {
		return expressionName;
	}

	@Override
	public String toString() {
		if (expressionName == null) {
			return symbol;
		} else {
			return symbol + ':' + expressionName;
		}
	}

}
