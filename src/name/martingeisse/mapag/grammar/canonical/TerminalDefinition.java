package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	// TODO test equals / hashCode in this whole package
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TerminalDefinition) {
			TerminalDefinition other = (TerminalDefinition)obj;
			return new EqualsBuilder().append(getName(), other.getName())
				.append(precedenceIndex, other.precedenceIndex)
				.append(associativity, other.associativity)
				.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(precedenceIndex).append(associativity).toHashCode();
	}

}
