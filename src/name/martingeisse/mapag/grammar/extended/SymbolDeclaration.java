package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public abstract class SymbolDeclaration {

	private final String name;

	public SymbolDeclaration(String name) {
		ParameterUtil.ensureNotNullOrEmpty(name, "name");
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
