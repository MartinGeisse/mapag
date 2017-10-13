package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class GrammarTest {

	private static final String PACKAGE_NAME = "foo.bar";
	private static final String CLASS_NAME = "MyClass";
	private static final String START_NONTERMINAL_NAME = "dummyStart";

	private static final TerminalDefinition TERMINAL_1 = new TerminalDefinition("foo", null, Associativity.NONASSOC);
	private static final TerminalDefinition TERMINAL_2 = new TerminalDefinition("bar", null, Associativity.LEFT);
	private static final TerminalDefinition TERMINAL_3 = new TerminalDefinition("baz", 3, Associativity.RIGHT);
	private static final ImmutableList<TerminalDefinition> TERMINALS = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3);

	private static final NonterminalDefinition NONTERMINAL_1 = new NonterminalDefinition("nt1", ImmutableList.of(
		new Alternative(ImmutableList.of()),
		new Alternative(ImmutableList.of("foo", "bar")),
		new Alternative(ImmutableList.of("foo", "baz"))
	));
	private static final NonterminalDefinition NONTERMINAL_2 = new NonterminalDefinition("nt2", ImmutableList.of(
		new Alternative(ImmutableList.of("nt3", "nt3"))
	));
	private static final NonterminalDefinition NONTERMINAL_3 = new NonterminalDefinition("dummyStart", ImmutableList.of(
		new Alternative(ImmutableList.of("nt1", "nt2", "baz"))
	));
	private static final ImmutableList<NonterminalDefinition> NONTERMINALS = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3);

	@Test
	public void testConstructorGetter() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		Assert.assertEquals(PACKAGE_NAME, grammar.getPackageName());
		Assert.assertEquals(CLASS_NAME, grammar.getClassName());
		Assert.assertEquals(START_NONTERMINAL_NAME, grammar.getStartNonterminalName());

		Assert.assertEquals(3, grammar.getTerminalDefinitions().size());
		Assert.assertEquals(TERMINAL_1, grammar.getTerminalDefinitions().get("foo"));
		Assert.assertEquals(TERMINAL_2, grammar.getTerminalDefinitions().get("bar"));
		Assert.assertEquals(TERMINAL_3, grammar.getTerminalDefinitions().get("baz"));

		Assert.assertEquals(3, grammar.getNonterminalDefinitions().size());
		Assert.assertEquals(NONTERMINAL_1, grammar.getNonterminalDefinitions().get("nt1"));
		Assert.assertEquals(NONTERMINAL_2, grammar.getNonterminalDefinitions().get("nt2"));
		Assert.assertEquals(NONTERMINAL_3, grammar.getNonterminalDefinitions().get("dummyStart"));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPackageName() {
		new Grammar(null, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test
	public void testEmptyPackageName() {
		new Grammar("", CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullClassName() {
		new Grammar(PACKAGE_NAME, null, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyClassName() {
		new Grammar(PACKAGE_NAME, "", TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminalDefinitions() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, null, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test
	public void testEmptyTerminalDefinitions() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, ImmutableList.of(), NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullNonterminalDefinitions() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, null, START_NONTERMINAL_NAME);
	}

	@Test
	public void testEmptyNonterminalDefinitions() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, ImmutableList.of(), START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStartNonterminalName() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStartNonterminalName() {
		new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, "");
	}

	@Test
	public void testTwoTerminalsNameCollision() {
		// TODO
	}

	@Test
	public void testTwoNonterminalsNameCollision() {
		// TODO
	}

	@Test
	public void testTerminalNonterminalNameCollision() {
		// TODO
	}

	@Test
	public void testIsTerminal() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);

		// terminals
		Assert.assertTrue(grammar.isTerminal("foo"));
		Assert.assertTrue(grammar.isTerminal("bar"));
		Assert.assertTrue(grammar.isTerminal("baz"));

		// undefined
		Assert.assertFalse(grammar.isTerminal("abc"));

		// nonterminals
		Assert.assertFalse(grammar.isTerminal("nt1"));
		Assert.assertFalse(grammar.isTerminal("nt2"));
		Assert.assertFalse(grammar.isTerminal("dummyStart"));

		// special
		Assert.assertFalse(grammar.isTerminal("%start"));
		Assert.assertFalse(grammar.isTerminal("%eof"));
		Assert.assertFalse(grammar.isTerminal("%error"));

		// special undefined
		Assert.assertFalse(grammar.isTerminal("%wegoiewjgioejwgoewjhgiouewhguiewhjgoiew"));

	}

	@Test
	public void testSentenceContainsTerminals() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		// Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("nt1"));
		// TODO
	}

	@Test
	public void testIsNonterminal() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);

		// terminals
		Assert.assertFalse(grammar.isTerminal("foo"));
		Assert.assertFalse(grammar.isTerminal("bar"));
		Assert.assertFalse(grammar.isTerminal("baz"));

		// nonterminals
		Assert.assertTrue(grammar.isTerminal("nt1"));
		Assert.assertTrue(grammar.isTerminal("nt2"));
		Assert.assertTrue(grammar.isTerminal("dummyStart"));

		// undefined
		Assert.assertFalse(grammar.isTerminal("abc"));

		// special
		Assert.assertFalse(grammar.isTerminal("%start"));
		Assert.assertFalse(grammar.isTerminal("%eof"));
		Assert.assertFalse(grammar.isTerminal("%error"));

		// special undefined
		Assert.assertFalse(grammar.isTerminal("%wegoiewjgioejwgoewjhgiouewhguiewhjgoiew"));

	}

}
