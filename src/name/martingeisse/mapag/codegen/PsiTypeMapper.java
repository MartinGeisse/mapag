package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;

/**
 *
 */
public class PsiTypeMapper {

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;

	public PsiTypeMapper(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
	}

	public String getEffectiveTypeForSymbol(String symbol) {
		if (grammar.getTerminalDefinitions().get(symbol) != null) {
			return "LeafPsiElement";
		} else if (grammar.getNonterminalDefinitions().get(symbol) != null) {
			return getEffectiveTypeForNonterminal(symbol);
		} else {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
	}

	public String getEffectiveTypeForNonterminal(String nonterminal) {
		return getGeneratedTypeForNonterminal(nonterminal);
	}

	public String getGeneratedTypeForNonterminal(String nonterminal) {
		return IdentifierUtil.toIdentifier(nonterminal, true);
	}

}
