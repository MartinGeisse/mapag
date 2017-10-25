package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode()
 * are those of class Object, i.e. based on object identity. The reason is that even if this were a value object,
 * different parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't
 * really a situation where two distinct but equal instances of this class would exist. Instances are created from
 * the grammar file and anything that appears in different places in this file is not equal in any meaningful sense.
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
