package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ListUtil;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class Alternative {

	private final ImmutableList<String> expansion;

	public Alternative(ImmutableList<String> expansion) {
		ParameterUtil.ensureNotNull(expansion, "expansion");
		ParameterUtil.ensureNoNullOrEmptyElement(expansion, "expansion");
		this.expansion = expansion;
	}

	public ImmutableList<String> getExpansion() {
		return expansion;
	}

	public Alternative vanishNonterminal(String nonterminalToVanish) {
		ParameterUtil.ensureNotNullOrEmpty(nonterminalToVanish, "nonterminalToVanish");
		return new Alternative(ListUtil.withElementsRemoved(expansion, symbol -> symbol.equals(nonterminalToVanish)));
	}

}
