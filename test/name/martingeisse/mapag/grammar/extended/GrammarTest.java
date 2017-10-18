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
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		Assert.assertEquals(PACKAGE_NAME, grammar.getPackageName());
		Assert.assertEquals(CLASS_NAME, grammar.getClassName());
		Assert.assertEquals(TERMINALS, grammar.getTerminalDeclarations());
		Assert.assertEquals(NONTERMINALS, grammar.getNonterminalDeclarations());
		Assert.assertEquals(PRECEDENCE_TABLE_EMPTY, grammar.getPrecedenceTable());
		Assert.assertEquals(START_NONTERMINAL_NAME, grammar.getStartNonterminalName());
		Assert.assertEquals(PRODUCTIONS, grammar.getProductions());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPackageName() {
		new Grammar(null, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testEmptyPackageName() {
		new Grammar("", CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullClassName() {
		new Grammar(PACKAGE_NAME, null, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyClassName() {
		new Grammar(PACKAGE_NAME, "", TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminalDeclarations() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, null, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testEmptyTerminalDeclarations() {
		// allowed by the constructor, but caught during validation
		new Grammar(PACKAGE_NAME, CLASS_NAME, ImmutableList.of(), NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullNonterminalDeclarations() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, null, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testEmptyNonterminalDeclarations() {
		// allowed by the constructor, but caught during validation
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, ImmutableList.of(), PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testNullPrecedenceTable() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, null, START_NONTERMINAL_NAME, PRODUCTIONS);
		Assert.assertEquals(0, grammar.getPrecedenceTable().getEntries().size());
	}

	@Test
	public void testNonemptyPrecedenceTable() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_NONEMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		Assert.assertEquals(PRECEDENCE_TABLE_NONEMPTY, grammar.getPrecedenceTable());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStartNonterminalName() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, null, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStartNonterminalName() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, "", PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullProductions() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, null);
	}

	@Test
	public void testEmptyProductions() {
		// allowed by the constructor, but caught during validation
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, ImmutableList.of());
	}

}
