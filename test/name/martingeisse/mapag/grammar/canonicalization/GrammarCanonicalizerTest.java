package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.SequenceExpression;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.extended.TestGrammarObjects.PRECEDENCE_TABLE_EMPTY;

/**
 *
 */
public class GrammarCanonicalizerTest {

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
	private static final NonterminalDeclaration NONTERMINAL_3 = new NonterminalDeclaration("start");
	private static final ImmutableList<NonterminalDeclaration> NONTERMINALS = ImmutableList.of(
		NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3
	);

	private static final PrecedenceTable PRECEDENCE_TABLE = new PrecedenceTable(ImmutableList.of(
		new PrecedenceTable.Entry(ImmutableSet.of("e"), Associativity.LEFT),
		new PrecedenceTable.Entry(ImmutableSet.of("f", "a"), Associativity.RIGHT),
		new PrecedenceTable.Entry(ImmutableSet.of("c"), Associativity.NONASSOC)
	));

	// --- start TODO

	private static final Alternative ALTERNATIVE_1 = new Alternative(new SymbolReference("a"), null);
	private static final Alternative ALTERNATIVE_2 = new Alternative(new SymbolReference("p"), "f");
	private static final Alternative ALTERNATIVE_3 = new Alternative(new SequenceExpression(new SymbolReference("f"), new SymbolReference("g")), null);
	private static final Alternative ALTERNATIVE_4 = new Alternative(new SymbolReference("e"), null);
	private static final Production PRODUCTION_1 = new Production("p", ImmutableList.of(ALTERNATIVE_1, ALTERNATIVE_2));
	private static final Production PRODUCTION_2 = new Production("q", ImmutableList.of(ALTERNATIVE_3));
	private static final Production PRODUCTION_3 = new Production("start", ImmutableList.of(ALTERNATIVE_4));
	private static final ImmutableList<Production> PRODUCTIONS = ImmutableList.of(PRODUCTION_1, PRODUCTION_2, PRODUCTION_3);

	// --- end TODO

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNull() {
		new GrammarCanonicalizer(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testConstructorWithInvalid() {
		name.martingeisse.mapag.grammar.extended.Grammar inputGrammar =
			new name.martingeisse.mapag.grammar.extended.Grammar(TERMINALS, NONTERMINALS,
				PRECEDENCE_TABLE_EMPTY, "unknownStartNonterminal", PRODUCTIONS);
		new GrammarCanonicalizer(inputGrammar).run();
	}

	@Test
	public void testCanonicalization() {

		name.martingeisse.mapag.grammar.extended.Grammar inputGrammar = new name.martingeisse.mapag.grammar.extended.Grammar(
			TERMINALS, NONTERMINALS, PRECEDENCE_TABLE, START_NONTERMINAL_NAME, PRODUCTIONS);
		name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar = new GrammarCanonicalizer(inputGrammar).run().getResult();

		//
		// terminals
		//

		String[] terminalNames = {"a", "b", "c", "d", "e", "f", "g"};
		Assert.assertEquals(ImmutableSet.copyOf(terminalNames), canonicalGrammar.getTerminalDefinitions().keySet());
		for (String terminalName : terminalNames) {
			Assert.assertEquals(terminalName, canonicalGrammar.getTerminalDefinitions().get(terminalName).getName());
		}

		Assert.assertEquals(1, canonicalGrammar.getTerminalDefinitions().get("a").getPrecedenceIndex().intValue());
		Assert.assertNull(canonicalGrammar.getTerminalDefinitions().get("b").getPrecedenceIndex());
		Assert.assertEquals(2, canonicalGrammar.getTerminalDefinitions().get("c").getPrecedenceIndex().intValue());
		Assert.assertNull(canonicalGrammar.getTerminalDefinitions().get("d").getPrecedenceIndex());
		Assert.assertEquals(0, canonicalGrammar.getTerminalDefinitions().get("e").getPrecedenceIndex().intValue());
		Assert.assertEquals(1, canonicalGrammar.getTerminalDefinitions().get("f").getPrecedenceIndex().intValue());
		Assert.assertNull(canonicalGrammar.getTerminalDefinitions().get("g").getPrecedenceIndex());

		Assert.assertEquals(Associativity.RIGHT, canonicalGrammar.getTerminalDefinitions().get("a").getAssociativity());
		Assert.assertEquals(Associativity.NONASSOC, canonicalGrammar.getTerminalDefinitions().get("b").getAssociativity());
		Assert.assertEquals(Associativity.NONASSOC, canonicalGrammar.getTerminalDefinitions().get("c").getAssociativity());
		Assert.assertEquals(Associativity.NONASSOC, canonicalGrammar.getTerminalDefinitions().get("d").getAssociativity());
		Assert.assertEquals(Associativity.LEFT, canonicalGrammar.getTerminalDefinitions().get("e").getAssociativity());
		Assert.assertEquals(Associativity.RIGHT, canonicalGrammar.getTerminalDefinitions().get("f").getAssociativity());
		Assert.assertEquals(Associativity.NONASSOC, canonicalGrammar.getTerminalDefinitions().get("g").getAssociativity());

		//
		// nonterminals
		//

		Assert.assertEquals(ImmutableSet.of("p", "q", "start"), canonicalGrammar.getNonterminalDefinitions().keySet());

		Assert.assertEquals("p", canonicalGrammar.getNonterminalDefinitions().get("p").getName());
		Assert.assertEquals(2, canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().size());
		Assert.assertNull(canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().get(0).getEffectivePrecedenceTerminal());
		Assert.assertEquals(1, canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().get(0).getExpansion().size());
		Assert.assertEquals("a", canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().get(0).getExpansion().get(0));
		Assert.assertEquals("f", canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().get(1).getEffectivePrecedenceTerminal());
		Assert.assertEquals(1, canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().get(1).getExpansion().size());
		Assert.assertEquals("p", canonicalGrammar.getNonterminalDefinitions().get("p").getAlternatives().get(1).getExpansion().get(0));

		Assert.assertEquals("q", canonicalGrammar.getNonterminalDefinitions().get("q").getName());
		Assert.assertEquals(1, canonicalGrammar.getNonterminalDefinitions().get("q").getAlternatives().size());
		Assert.assertNull(canonicalGrammar.getNonterminalDefinitions().get("q").getAlternatives().get(0).getEffectivePrecedenceTerminal());
		Assert.assertEquals(2, canonicalGrammar.getNonterminalDefinitions().get("q").getAlternatives().get(0).getExpansion().size());
		Assert.assertEquals("f", canonicalGrammar.getNonterminalDefinitions().get("q").getAlternatives().get(0).getExpansion().get(0));
		Assert.assertEquals("g", canonicalGrammar.getNonterminalDefinitions().get("q").getAlternatives().get(0).getExpansion().get(1));

		Assert.assertEquals("start", canonicalGrammar.getNonterminalDefinitions().get("start").getName());
		Assert.assertEquals(1, canonicalGrammar.getNonterminalDefinitions().get("start").getAlternatives().size());
		Assert.assertNull(canonicalGrammar.getNonterminalDefinitions().get("start").getAlternatives().get(0).getEffectivePrecedenceTerminal());
		Assert.assertEquals(1, canonicalGrammar.getNonterminalDefinitions().get("start").getAlternatives().get(0).getExpansion().size());
		Assert.assertEquals("e", canonicalGrammar.getNonterminalDefinitions().get("start").getAlternatives().get(0).getExpansion().get(0));

		//
		// other
		//

		Assert.assertEquals(START_NONTERMINAL_NAME, canonicalGrammar.getStartNonterminalName());

	}

}
