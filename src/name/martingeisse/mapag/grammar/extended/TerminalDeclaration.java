package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class TerminalDeclaration {

	private final String name;

	public TerminalDeclaration(String name) {
		ParameterUtil.ensureNotNullOrEmpty(name, "name");
		if (name.startsWith("%")) {
			throw new IllegalArgumentException("cannot redefine special symbol " + name);
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
