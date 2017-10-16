package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.Grammar;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.extended.TestGrammarObjects.*;

/**
 *
 */
public class GrammarValidatorTest {

	@Test
	public void testValid() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test
	public void test() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

}
