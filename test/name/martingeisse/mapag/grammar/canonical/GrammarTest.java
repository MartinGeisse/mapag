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
		Assert.assertEquals(ImmutableSet.copyOf(TERMINALS), ImmutableSet.copyOf(grammar.getTerminalDefinitions().values()));
		Assert.assertEquals(ImmutableSet.copyOf(NONTERMINALS), ImmutableSet.copyOf(grammar.getNonterminalDefinitions().values()));
		Assert.assertEquals(START_NONTERMINAL_NAME, grammar.getStartNonterminalName());
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

}
