package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;

/**
 *
 */
public class PsiClassesGenerator {

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;

	public PsiClassesGenerator(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
	}

	public void generate() throws ConfigurationException {
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {

		}
	}

}
