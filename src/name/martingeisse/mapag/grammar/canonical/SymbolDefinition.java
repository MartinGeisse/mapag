package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public abstract class SymbolDefinition {

	private final String name;

	public SymbolDefinition(String name) {
		ParameterUtil.ensureNotNullOrEmpty(name, "name");
		if (name.indexOf('%') != -1) {
			throw new IllegalArgumentException("user-defined symbol names must not contain the '%' character");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
