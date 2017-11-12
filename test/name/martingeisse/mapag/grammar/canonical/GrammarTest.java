package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.*;

/**
 *
 */
public class GrammarTest {

	@Test
	public void testConstructorGetter() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
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
	public void testNullTerminalDefinitions() {
		new Grammar(null, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test
	public void testEmptyTerminalDefinitions() {
		// allowed in constructor but caught during validation
		new Grammar(ImmutableList.of(), NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullNonterminalDefinitions() {
		new Grammar(TERMINALS, null, START_NONTERMINAL_NAME);
	}

	@Test
	public void testEmptyNonterminalDefinitions() {
		// allowed in constructor but caught during validation
		new Grammar(TERMINALS, ImmutableList.of(), START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStartNonterminalName() {
		new Grammar(TERMINALS, NONTERMINALS, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStartNonterminalName() {
		new Grammar(TERMINALS, NONTERMINALS, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTwoTerminalsNameCollisionEqual() {
		TerminalDefinition conflictingTerminal = new TerminalDefinition("bar", null, Associativity.LEFT);
		ImmutableList<TerminalDefinition> terminals = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3, conflictingTerminal);
		new Grammar(terminals, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTwoTerminalsNameCollisionDifferent() {
		TerminalDefinition conflictingTerminal = new TerminalDefinition("bar", 2, Associativity.RIGHT);
		ImmutableList<TerminalDefinition> terminals = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3, conflictingTerminal);
		new Grammar(terminals, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTwoNonterminalsNameCollisionEqual() {
		NonterminalDefinition conflictingNonterminal = new NonterminalDefinition("nt2", ImmutableList.of(
			new Alternative(ImmutableList.of("nt3", "nt3"), null, AlternativeAnnotation.EMPTY)
		), NonterminalAnnotation.EMPTY);
		ImmutableList<NonterminalDefinition> nonterminals = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3, conflictingNonterminal);
		new Grammar(TERMINALS, nonterminals, START_NONTERMINAL_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTwoNonterminalsNameCollisionDifferent() {
		NonterminalDefinition conflictingNonterminal = new NonterminalDefinition("nt2", ImmutableList.of(
			new Alternative(ImmutableList.of("nt1", "nt1"), null, AlternativeAnnotation.EMPTY)
		), NonterminalAnnotation.EMPTY);
		ImmutableList<NonterminalDefinition> nonterminals = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3, conflictingNonterminal);
		new Grammar(TERMINALS, nonterminals, START_NONTERMINAL_NAME);
	}

	@Test
	public void testTerminalNonterminalNameCollision() {
		// this succeeds in building the grammar object but should fail validation (which is tested in the validator's test class)
		TerminalDefinition conflictingTerminal = new TerminalDefinition("nt2", 2, Associativity.RIGHT);
		ImmutableList<TerminalDefinition> terminals = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3, conflictingTerminal);
		new Grammar(terminals, NONTERMINALS, START_NONTERMINAL_NAME);
	}

	@Test
	public void testIsTerminal() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);

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
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);

		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of()));
		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of("undefinedSymbol")));
		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of("undefinedSymbol", "anotherUndefinedSymbol")));
		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of("nt1")));
		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of("nt2")));
		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of("nt2", "nt1")));
		Assert.assertFalse(grammar.sentenceContainsTerminals(ImmutableList.of("undefinedSymbol", "nt1")));

		Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("foo")));
		Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("foo", "bar")));
		Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("foo", "undefinedSymbol")));
		Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("foo", "nt1")));
		Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("nt1", "foo")));
		Assert.assertTrue(grammar.sentenceContainsTerminals(ImmutableList.of("nt1", "foo", "nt2")));

	}

	@Test
	public void testIsNonterminal() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);

		// terminals
		Assert.assertFalse(grammar.isNonterminal("foo"));
		Assert.assertFalse(grammar.isNonterminal("bar"));
		Assert.assertFalse(grammar.isNonterminal("baz"));

		// nonterminals
		Assert.assertTrue(grammar.isNonterminal("nt1"));
		Assert.assertTrue(grammar.isNonterminal("nt2"));
		Assert.assertTrue(grammar.isNonterminal("dummyStart"));

		// undefined
		Assert.assertFalse(grammar.isNonterminal("abc"));

		// special
		Assert.assertFalse(grammar.isNonterminal("%start"));
		Assert.assertFalse(grammar.isNonterminal("%eof"));
		Assert.assertFalse(grammar.isNonterminal("%error"));

		// special undefined
		Assert.assertFalse(grammar.isNonterminal("%wegoiewjgioejwgoewjhgiouewhguiewhjgoiew"));

	}

	@Test
	public void testSentenceContainsNonterminals() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);

		Assert.assertFalse(grammar.sentenceContainsNonterminals(ImmutableList.of()));
		Assert.assertFalse(grammar.sentenceContainsNonterminals(ImmutableList.of("undefinedSymbol")));
		Assert.assertFalse(grammar.sentenceContainsNonterminals(ImmutableList.of("undefinedSymbol", "anotherUndefinedSymbol")));
		Assert.assertFalse(grammar.sentenceContainsNonterminals(ImmutableList.of("foo")));
		Assert.assertFalse(grammar.sentenceContainsNonterminals(ImmutableList.of("foo", "bar")));
		Assert.assertFalse(grammar.sentenceContainsNonterminals(ImmutableList.of("foo", "undefinedSymbol")));

		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("nt1")));
		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("nt2")));
		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("nt2", "nt1")));
		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("undefinedSymbol", "nt1")));
		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("foo", "nt1")));
		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("nt1", "foo")));
		Assert.assertTrue(grammar.sentenceContainsNonterminals(ImmutableList.of("foo", "nt1", "bar")));

	}

}
