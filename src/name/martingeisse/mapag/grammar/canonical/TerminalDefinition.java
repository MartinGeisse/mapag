package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
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
