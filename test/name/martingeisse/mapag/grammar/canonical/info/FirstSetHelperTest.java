package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.*;
import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.START_NONTERMINAL_NAME;

/**
 *
 */
public class FirstSetHelperTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullGrammar() {
		FirstSetHelper.runFor(null, ImmutableSet.of());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullVanishableNonterminals() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		FirstSetHelper.runFor(grammar, null);
	}

	// TODO

}
