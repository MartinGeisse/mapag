package name.martingeisse.mapag.grammar;

/**
 *
 */
public abstract class SymbolDefinition {

	private final String name;

	public SymbolDefinition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
