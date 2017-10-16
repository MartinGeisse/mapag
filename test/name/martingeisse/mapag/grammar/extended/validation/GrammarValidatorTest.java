package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.SequenceExpression;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Test;
import org.junit.runner.RunWith;

import static name.martingeisse.mapag.grammar.extended.TestGrammarObjects.*;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class GrammarValidatorTest {

	@Test
	public void testValid() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateTerminalName() {
		TerminalDeclaration conflictingTerminalDeclaration = new TerminalDeclaration("foo");
		ImmutableList<TerminalDeclaration> terminals = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3, conflictingTerminalDeclaration);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, terminals, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateNonterminalName() {
		NonterminalDeclaration conflictingNonterminalDeclaration = new NonterminalDeclaration("nt2");
		ImmutableList<NonterminalDeclaration> nonterminals = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3, conflictingNonterminalDeclaration);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, nonterminals, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testSameNameForTerminalAndNonterminal() {
		NonterminalDeclaration conflictingNonterminalDeclaration = new NonterminalDeclaration("foo");
		ImmutableList<NonterminalDeclaration> nonterminals = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3, conflictingNonterminalDeclaration);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, nonterminals, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownTerminalInPrecedenceTable() {
		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry("foo", Associativity.LEFT),
			new PrecedenceTable.Entry("bar", Associativity.LEFT),
			new PrecedenceTable.Entry("xyz", Associativity.LEFT)
		));
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateTerminalInPrecedenceTable() {
		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry("foo", Associativity.LEFT),
			new PrecedenceTable.Entry("bar", Associativity.LEFT),
			new PrecedenceTable.Entry("foo", Associativity.LEFT)
		));
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownStartNonterminal() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, "unknown", PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownNonterminalInProduction() {
		ImmutableList<Production> productions = ImmutableList.of(
			new Production("unknown", new SymbolReference("foo"))
		);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
		new GrammarValidator(grammar).validate();
	}

	@DataProvider
	public static Object[][] getTestUnknownSymbolReferenceInSecondProductionData() {
		return new Object[][] {
			{
				ImmutableList.of(
					PRODUCTION_1,
					new Production("nt2", new SequenceExpression(new SymbolReference("unknown"), new SymbolReference("bar"))),
					new Production("nt1", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")))
				)
			},
			{
				ImmutableList.of(
					PRODUCTION_1,
					new Production("nt2", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("unknown"))),
					new Production("nt1", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")))
				)
			},
			{
				ImmutableList.of(
					PRODUCTION_1,
					new Production("nt1", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar"))),
					new Production("nt2", new SequenceExpression(new SymbolReference("unknown"), new SymbolReference("bar")))
				)
			},
			{
				ImmutableList.of(
					PRODUCTION_1,
					new Production("nt1", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar"))),
					new Production("nt2", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("unknown")))
				)
			}
		};
	}

	@Test(expected = IllegalStateException.class)
	@UseDataProvider("getTestUnknownSymbolReferenceInSecondProductionData")
	public void testUnknownSymbolReference(ImmutableList<Production> productions) {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
		new GrammarValidator(grammar).validate();
	}

}
