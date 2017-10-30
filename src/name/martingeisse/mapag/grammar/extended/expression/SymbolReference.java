package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class SymbolReference extends Expression {

	private final String symbolName;

	public SymbolReference(String symbolName) {
		ParameterUtil.ensureNotNullOrEmpty(symbolName, "symbolName");
		if (symbolName.startsWith("%") && !symbolName.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) { // TODO test this
			throw new IllegalArgumentException("invalid symbol reference: " + symbolName);
		}
		this.symbolName = symbolName;
	}

	public String getSymbolName() {
		return symbolName;
	}

	@Override
	public String toString() {
		return symbolName;
	}

}
