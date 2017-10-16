package name.martingeisse.mapag.grammar.canonical.validation;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.*;

/**
 *
 */
public class GrammarValidatorTest {

	@Test(expected = IllegalStateException.class)
	public void testTerminalNonterminalNameCollision() {
		TerminalDefinition conflictingTerminal = new TerminalDefinition("nt2", 2, Associativity.RIGHT);
		ImmutableList<TerminalDefinition> terminals = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3, conflictingTerminal);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, terminals, NONTERMINALS, START_NONTERMINAL_NAME);
		new GrammarValidator(grammar).validate();
	}

	// TODO further tests

}
