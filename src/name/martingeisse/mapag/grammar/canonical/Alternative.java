package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ListUtil;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

public final class Alternative {

	private final ImmutableList<String> expansion;
	private final String effectivePrecedenceTerminal;

	public Alternative(ImmutableList<String> expansion, String effectivePrecedenceTerminal) {
		ParameterUtil.ensureNotNull(expansion, "expansion");
		ParameterUtil.ensureNoNullOrEmptyElement(expansion, "expansion");
		this.expansion = expansion;
		if (effectivePrecedenceTerminal != null && effectivePrecedenceTerminal.isEmpty()) {
			throw new IllegalArgumentException("effectivePrecedenceTerminal cannot be the empty string");
		}
		this.effectivePrecedenceTerminal = effectivePrecedenceTerminal;
	}

	public ImmutableList<String> getExpansion() {
		return expansion;
	}

	public String getEffectivePrecedenceTerminal() {
		return effectivePrecedenceTerminal;
	}

	public Alternative vanishSymbol(String nonterminalToVanish) {
		ParameterUtil.ensureNotNullOrEmpty(nonterminalToVanish, "nonterminalToVanish");
		return new Alternative(ListUtil.withElementsRemoved(expansion, symbol -> symbol.equals(nonterminalToVanish)), effectivePrecedenceTerminal);
	}

	@Override
	public String toString() {
		return StringUtils.join(expansion, ' ') + " %precedence " + (effectivePrecedenceTerminal == null ? "%undefined" : effectivePrecedenceTerminal);
	}

}
