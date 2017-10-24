package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public final class NonterminalDefinition extends SymbolDefinition {

	private final ImmutableList<Alternative> alternatives;

	public NonterminalDefinition(String name, ImmutableList<Alternative> alternatives) {
		super(name);
		this.alternatives = ParameterUtil.ensureNotNullOrEmpty(alternatives, "alternatives");
		ParameterUtil.ensureNoNullElement(alternatives, "alternatives");
	}

	public ImmutableList<Alternative> getAlternatives() {
		return alternatives;
	}

	@Override
	public String toString() {
		return getName() + " ::= " + StringUtils.join(alternatives, " | ") + ';';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NonterminalDefinition) {
			NonterminalDefinition other = (NonterminalDefinition)obj;
			return new EqualsBuilder().append(getName(), other.getName())
				.append(alternatives, other.alternatives)
				.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(alternatives).toHashCode();
	}

}
