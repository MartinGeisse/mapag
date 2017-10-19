package name.martingeisse.mapag.grammar;

import org.junit.Test;

import static name.martingeisse.mapag.grammar.extended.TestGrammarObjects.*;

/**
 *
 */
public class GrammarCanonicalizerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCosntructorWithNull() {
		new GrammarCanonicalizer(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testCosntructorWithInvalid() {
		name.martingeisse.mapag.grammar.extended.Grammar inputGrammar =
				new name.martingeisse.mapag.grammar.extended.Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS,
						PRECEDENCE_TABLE_EMPTY, "unknownStartNonterminal", PRODUCTIONS);
		new GrammarCanonicalizer(inputGrammar).run();
	}



}
