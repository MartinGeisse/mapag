package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.NonterminalDeclaration;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.extended.TestGrammarObjects.*;

/**
 *
 */
public class GrammarCanonicalizerTest {

	private static final String PACKAGE_NAME = "foo.bar";
	private static final String CLASS_NAME = "MyClass";
	private static final String START_NONTERMINAL_NAME = "start";

	private static final TerminalDeclaration TERMINAL_1 = new TerminalDeclaration("a");
	private static final TerminalDeclaration TERMINAL_2 = new TerminalDeclaration("b");
	private static final TerminalDeclaration TERMINAL_3 = new TerminalDeclaration("c");
	private static final TerminalDeclaration TERMINAL_4 = new TerminalDeclaration("d");
	private static final TerminalDeclaration TERMINAL_5 = new TerminalDeclaration("e");
	private static final TerminalDeclaration TERMINAL_6 = new TerminalDeclaration("f");
	private static final TerminalDeclaration TERMINAL_7 = new TerminalDeclaration("g");
	private static final ImmutableList<TerminalDeclaration> TERMINALS = ImmutableList.of(
		TERMINAL_1, TERMINAL_2, TERMINAL_3, TERMINAL_4, TERMINAL_5, TERMINAL_6, TERMINAL_7
	);

	private static final NonterminalDeclaration NONTERMINAL_1 = new NonterminalDeclaration("p");
	private static final NonterminalDeclaration NONTERMINAL_2 = new NonterminalDeclaration("q");
	private static final NonterminalDeclaration NONTERMINAL_3 = new NonterminalDeclaration("r");
	private static final ImmutableList<NonterminalDeclaration> NONTERMINALS = ImmutableList.of(
		NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3
	);

	private static final PrecedenceTable PRECEDENCE_TABLE = new PrecedenceTable(ImmutableList.of(
		new PrecedenceTable.Entry(ImmutableSet.of("e"), Associativity.LEFT),
		new PrecedenceTable.Entry(ImmutableSet.of("f", "a"), Associativity.RIGHT),
		new PrecedenceTable.Entry(ImmutableSet.of("c"), Associativity.NONASSOC)
	));


	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNull() {
		new GrammarCanonicalizer(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructorWithInvalid() {
		name.martingeisse.mapag.grammar.extended.Grammar inputGrammar =
				new name.martingeisse.mapag.grammar.extended.Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS,
						PRECEDENCE_TABLE_EMPTY, "unknownStartNonterminal", PRODUCTIONS);
		new GrammarCanonicalizer(inputGrammar).run();
	}

	@Test
	public void testCanonicalization() {
		name.martingeisse.mapag.grammar.extended.Grammar grammar = new name.martingeisse.mapag.grammar.extended.Grammar(
			PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE, START_NONTERMINAL_NAME, PRODUCTIONS);
		// TODO
	}

	// TODO
	// especially that the precedence table works the way I expect, especially that higher up = lower precedence

}
