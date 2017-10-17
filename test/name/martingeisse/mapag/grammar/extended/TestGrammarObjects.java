package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;

/**
 *
 */
public class TestGrammarObjects {

	public static final String PACKAGE_NAME = "foo.bar";
	public static final String CLASS_NAME = "MyClass";
	public static final TerminalDeclaration TERMINAL_1 = new TerminalDeclaration("foo");
	public static final TerminalDeclaration TERMINAL_2 = new TerminalDeclaration("bar");
	public static final TerminalDeclaration TERMINAL_3 = new TerminalDeclaration("baz");
	public static final ImmutableList<TerminalDeclaration> TERMINALS = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3);
	public static final NonterminalDeclaration NONTERMINAL_1 = new NonterminalDeclaration("nt1");
	public static final NonterminalDeclaration NONTERMINAL_2 = new NonterminalDeclaration("nt2");
	public static final NonterminalDeclaration NONTERMINAL_3 = new NonterminalDeclaration("dummyStart");
	public static final ImmutableList<NonterminalDeclaration> NONTERMINALS = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3);
	public static final PrecedenceTable PRECEDENCE_TABLE_EMPTY = new PrecedenceTable(ImmutableList.of());
	public static final PrecedenceTable PRECEDENCE_TABLE_NONEMPTY = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry(ImmutableSet.of(TERMINAL_2.getName()), Associativity.LEFT)
	));
	public static final String START_NONTERMINAL_NAME = "dummyStart";
	public static final Alternative ALTERNATIVE_1 = new Alternative(new SymbolReference(TERMINAL_1.getName()));
	public static final Alternative ALTERNATIVE_2 = new Alternative(new SymbolReference(TERMINAL_2.getName()), Alternative.PrecedenceSpecificationType.EXPLICIT, "abc");
	public static final Alternative ALTERNATIVE_3 = new Alternative(new SymbolReference(TERMINAL_3.getName()));
	public static final Production PRODUCTION_1 = new Production(START_NONTERMINAL_NAME, ImmutableList.of(ALTERNATIVE_1, ALTERNATIVE_2));
	public static final Production PRODUCTION_2 = new Production(NONTERMINAL_1.getName(), ImmutableList.of(ALTERNATIVE_3));
	public static final ImmutableList<Production> PRODUCTIONS = ImmutableList.of(PRODUCTION_1, PRODUCTION_2);

	// prevent instantiation
	private TestGrammarObjects() {
	}

}
