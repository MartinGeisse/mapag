package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode() are
 * those of class Object, i.e. based on object identity. The reason is that even if this were a value object, different
 * parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't really a
 * situation where two distinct but equal instances of this class would exist. Instances are created from the grammar
 * file and anything that appears in different places in this file is not equal in any meaningful sense.
 */
public final class Alternative {

	private final String name;
	private final Expansion expansion;
	private final AlternativeAttributes attributes;

	public Alternative(String name, Expansion expansion, AlternativeAttributes attributes) {
		this.name = ParameterUtil.ensureNotNullOrEmpty(name, "name");
		this.expansion = ParameterUtil.ensureNotNull(expansion, "expansion");
		this.attributes = ParameterUtil.ensureNotNull(attributes, "attributes");
	}

	public String getName() {
		return name;
	}

	public Expansion getExpansion() {
		return expansion;
	}

	public AlternativeAttributes getAttributes() {
		return attributes;
	}

	public Alternative vanishSymbol(String symbol) {
		ParameterUtil.ensureNotNullOrEmpty(symbol, "symbol");
		return new Alternative(name, expansion.vanishSymbol(symbol), attributes);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (name != null) {
			builder.append(name);
		} else {
			builder.append('?');
		}
		builder.append(" ::= ");
		builder.append(expansion);
		builder.append(' ');
		builder.append(attributes);
		return builder.toString();
	}

}
