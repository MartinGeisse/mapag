package name.martingeisse.mapag.grammar.extended;

/**
 *
 */
public abstract class SymbolDeclaration {

	private final String name;

	public SymbolDeclaration(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
