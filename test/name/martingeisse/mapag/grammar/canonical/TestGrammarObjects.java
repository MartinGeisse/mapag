package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;

/**
 * Shared singleton objects for test classes.
 */
public class TestGrammarObjects {

	public static final String PACKAGE_NAME = "foo.bar";
	public static final String CLASS_NAME = "MyClass";
	public static final String START_NONTERMINAL_NAME = "dummyStart";

	public static final TerminalDefinition TERMINAL_1 = new TerminalDefinition("foo", null, Associativity.NONASSOC);
	public static final TerminalDefinition TERMINAL_2 = new TerminalDefinition("bar", null, Associativity.NONASSOC);
	public static final TerminalDefinition TERMINAL_3 = new TerminalDefinition("baz", 3, Associativity.RIGHT);
	public static final ImmutableList<TerminalDefinition> TERMINALS = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3);

	// TODO

//	public static final NonterminalDefinition NONTERMINAL_1 = new NonterminalDefinition("nt1", ImmutableList.of(
//		new Alternative(ImmutableList.of()),
//		new Alternative(ImmutableList.of("foo", "bar")),
//		new Alternative(ImmutableList.of("foo", "baz"))
//	));
//	public static final NonterminalDefinition NONTERMINAL_2 = new NonterminalDefinition("nt2", ImmutableList.of(
//		new Alternative(ImmutableList.of("nt3", "nt3"))
//	));
//	public static final NonterminalDefinition NONTERMINAL_3 = new NonterminalDefinition("dummyStart", ImmutableList.of(
//		new Alternative(ImmutableList.of("nt1", "nt2", "baz"))
//	));
//	public static final ImmutableList<NonterminalDefinition> NONTERMINALS = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3);

	// prevent instantiation
	private TestGrammarObjects() {
	}

}
