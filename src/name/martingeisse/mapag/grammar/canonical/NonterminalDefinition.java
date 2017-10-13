package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

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

}
