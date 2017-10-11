package name.martingeisse.mapag.grammar.extended.expression;

/**
 *
 */
public final class SymbolReference extends Expression {

	private final String symbolName;

	public SymbolReference(String symbolName) {
		this.symbolName = symbolName;
	}

	public String getSymbolName() {
		return symbolName;
	}

}
