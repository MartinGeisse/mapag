package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class SymbolReference extends Expression {

	private final String symbolName;

	public SymbolReference(String symbolName) {
		ParameterUtil.ensureNotNullOrEmpty(symbolName, "symbolName");
		this.symbolName = symbolName;
	}

	public String getSymbolName() {
		return symbolName;
	}

}
