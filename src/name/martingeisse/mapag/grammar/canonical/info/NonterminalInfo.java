package name.martingeisse.mapag.grammar.canonical.info;

import java.util.List;

/**
 *
 */
public final class NonterminalInfo {

	private final String nonterminal;
	private final List<AlternativeInfo> alternatives;
	private GrammarInfo grammarInfo;

	public NonterminalInfo(String nonterminal, List<AlternativeInfo> alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = alternatives;
	}

	public String getNonterminal() {
		return nonterminal;
	}

	public List<AlternativeInfo> getAlternatives() {
		return alternatives;
	}

	void setGrammarInfo(GrammarInfo grammarInfo) {
		this.grammarInfo = grammarInfo;
	}

	public GrammarInfo getGrammarInfo() {
		return grammarInfo;
	}

}
