package name.martingeisse.mapag.grammar.canonical.info;

import java.util.List;

/**
 *
 */
public final class AlternativeInfo {

	private final List<String> expansion;
	private NonterminalInfo nonterminalInfo;

	public AlternativeInfo(List<String> expansion) {
		this.expansion = expansion;
	}

	public List<String> getExpansion() {
		return expansion;
	}

	public NonterminalInfo getNonterminalInfo() {
		return nonterminalInfo;
	}

	void setNonterminalInfo(NonterminalInfo nonterminalInfo) {
		this.nonterminalInfo = nonterminalInfo;
	}

	public GrammarInfo getGrammarInfo() {
		return nonterminalInfo.getGrammarInfo();
	}

	public boolean expansionContainsTerminals() {
		return getGrammarInfo().sentenceContainsTerminals(expansion);
	}

	public boolean expansionContainsNonterminals() {
		return getGrammarInfo().sentenceContainsNonterminals(expansion);
	}

}
