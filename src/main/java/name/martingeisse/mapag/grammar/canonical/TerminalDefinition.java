package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode()
 * are those of class Object, i.e. based on object identity. The reason is that even if this were a value object,
 * different parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't
 * really a situation where two distinct but equal instances of this class would exist. Instances are created from
 * the grammar file and anything that appears in different places in this file is not equal in any meaningful sense.
 *
 * Higher precedenceIndex means higher precedence.
 */
public final class TerminalDefinition extends SymbolDefinition {

	private final Integer precedenceIndex;
	private final Associativity associativity;

	public TerminalDefinition(String name, Integer precedenceIndex, Associativity associativity) {
		super(name);
		this.precedenceIndex = precedenceIndex;
		this.associativity = ParameterUtil.ensureNotNull(associativity, "associativity");
		if (precedenceIndex == null && associativity != Associativity.NONASSOC) {
			throw new IllegalArgumentException("terminal without precedence index cannot have associativity " + associativity + " since it cannot have any effect");
		}
	}

	public Integer getPrecedenceIndex() {
		return precedenceIndex;
	}

	public Associativity getAssociativity() {
		return associativity;
	}

}
