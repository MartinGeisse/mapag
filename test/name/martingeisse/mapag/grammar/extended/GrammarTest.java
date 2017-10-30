package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.extended.TestGrammarObjects.*;

/**
 *
 */
public class GrammarTest {

	@Test
	public void testConstructorGetter() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		Assert.assertEquals(TERMINALS, grammar.getTerminalDeclarations());
		Assert.assertEquals(NONTERMINALS, grammar.getNonterminalDeclarations());
		Assert.assertEquals(PRECEDENCE_TABLE_EMPTY, grammar.getPrecedenceTable());
		Assert.assertEquals(START_NONTERMINAL_NAME, grammar.getStartNonterminalName());
		Assert.assertEquals(PRODUCTIONS, grammar.getProductions());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminalDeclarations() {
		new Grammar(null, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testEmptyTerminalDeclarations() {
		// allowed by the constructor, but caught during validation
		new Grammar(ImmutableList.of(), NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullNonterminalDeclarations() {
		new Grammar(TERMINALS, null, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testEmptyNonterminalDeclarations() {
		// allowed by the constructor, but caught during validation
		new Grammar(TERMINALS, ImmutableList.of(), PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testNullPrecedenceTable() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, null, START_NONTERMINAL_NAME, PRODUCTIONS);
		Assert.assertEquals(0, grammar.getPrecedenceTable().getEntries().size());
	}

	@Test
	public void testNonemptyPrecedenceTable() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_NONEMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		Assert.assertEquals(PRECEDENCE_TABLE_NONEMPTY, grammar.getPrecedenceTable());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStartNonterminalName() {
		new Grammar(TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, null, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStartNonterminalName() {
		new Grammar(TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, "", PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullProductions() {
		new Grammar(TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, null);
	}

	@Test
	public void testEmptyProductions() {
		// allowed by the constructor, but caught during validation
		new Grammar(TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, ImmutableList.of());
	}

}
