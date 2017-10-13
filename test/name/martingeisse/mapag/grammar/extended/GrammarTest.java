package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class GrammarTest {

	private static final String PACKAGE_NAME = "foo.bar";
	private static final String CLASS_NAME = "MyClass";
	private static final TerminalDeclaration TERMINAL_1 = new TerminalDeclaration("ttt");
	private static final TerminalDeclaration TERMINAL_2 = new TerminalDeclaration("uuu");
	private static final TerminalDeclaration TERMINAL_3 = new TerminalDeclaration("vvv");
	private static final ImmutableList<TerminalDeclaration> TERMINALS = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3);
	private static final NonterminalDeclaration NONTERMINAL_1 = new NonterminalDeclaration("ttt");
	private static final NonterminalDeclaration NONTERMINAL_2 = new NonterminalDeclaration("uuu");
	private static final NonterminalDeclaration NONTERMINAL_3 = new NonterminalDeclaration("vvv");
	private static final ImmutableList<NonterminalDeclaration> NONTERMINALS = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3);
	private static final PrecedenceTable PRECEDENCE_TABLE_EMPTY = new PrecedenceTable(ImmutableList.of());
	private static final PrecedenceTable PRECEDENCE_TABLE_NONEMPTY = new PrecedenceTable(ImmutableList.of(new PrecedenceTable.Entry(TERMINAL_2.getName(), Associativity.LEFT)));
	private static final String START_NONTERMINAL_NAME = "dummyStart";
	private static final Production PRODUCTION_1 = new Production(NONTERMINAL_1.getName(), new SymbolReference(TERMINAL_1.getName()));
	private static final ImmutableList<Production> PRODUCTIONS = ImmutableList.of(PRODUCTION_1);

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
		new Grammar(PACKAGE_NAME, CLASS_NAME, ImmutableList.of(), NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullNonterminalDeclarations() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, null, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
	}

	@Test
	public void testEmptyNonterminalDeclarations() {
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
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, ImmutableList.of());
	}

}
