package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public abstract class SymbolDefinition {

	private final String name;

	public SymbolDefinition(String name) {
		ParameterUtil.ensureNotNullOrEmpty(name, "name");
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
