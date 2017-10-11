package name.martingeisse.mapag.grammar;

import name.martingeisse.mapag.grammar.extended.Grammar;

/**
 *
 */
public final class GrammarCanonicalizer {

	private final name.martingeisse.mapag.grammar.extended.Grammar inputGrammar;
	private name.martingeisse.mapag.grammar.canonical.Grammar outputGrammar;

	public GrammarCanonicalizer(name.martingeisse.mapag.grammar.extended.Grammar inputGrammar) {
		this.inputGrammar = inputGrammar;
	}

	public void run() {
		// TODO
	}

	public name.martingeisse.mapag.grammar.canonical.Grammar getResult() {
		return outputGrammar;
	}

}
