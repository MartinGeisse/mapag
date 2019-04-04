package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;

/**
 *
 */
public class TestGrammarObjects {

	public static final TerminalDeclaration TERMINAL_1 = new TerminalDeclaration("foo");
	public static final TerminalDeclaration TERMINAL_2 = new TerminalDeclaration("bar");
	public static final TerminalDeclaration TERMINAL_3 = new TerminalDeclaration("baz");
	public static final ImmutableList<TerminalDeclaration> TERMINALS = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3);
	public static final PrecedenceTable PRECEDENCE_TABLE_EMPTY = new PrecedenceTable(ImmutableList.of());
	public static final PrecedenceTable PRECEDENCE_TABLE_NONEMPTY = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry(ImmutableList.of(TERMINAL_2.getName()), Associativity.LEFT)
	));
	public static final String START_NONTERMINAL_NAME = "dummyStart";
	public static final Alternative ALTERNATIVE_1 = new Alternative(null, new SymbolReference(TERMINAL_1.getName()), null, null, false, false);
	public static final Alternative ALTERNATIVE_2 = new Alternative(null, new SymbolReference("nt2"), TERMINAL_3.getName(), null, false, false);
	public static final Alternative ALTERNATIVE_3 = new Alternative(null, new SymbolReference("nt1"), null, null, false, false);
	public static final Production PRODUCTION_1 = new Production(START_NONTERMINAL_NAME, ImmutableList.of(ALTERNATIVE_1, ALTERNATIVE_2));
	public static final Production PRODUCTION_2 = new Production("nt1", ImmutableList.of(ALTERNATIVE_3));
	public static final Production PRODUCTION_3 = new Production("nt2", ImmutableList.of(ALTERNATIVE_3));
	public static final ImmutableList<Production> PRODUCTIONS = ImmutableList.of(PRODUCTION_1, PRODUCTION_2, PRODUCTION_3);

	// prevent instantiation
	private TestGrammarObjects() {
	}

}
