package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public final class NonterminalDefinition extends SymbolDefinition {

	private final ImmutableList<Alternative> alternatives;

	public NonterminalDefinition(String name, ImmutableList<Alternative> alternatives) {
		super(name);
		this.alternatives = alternatives;
	}

	public ImmutableList<Alternative> getAlternatives() {
		return alternatives;
	}

}
