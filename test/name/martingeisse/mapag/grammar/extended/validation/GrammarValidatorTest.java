package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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

	@DataProvider
	public static Object[][] getTestUnknownTerminalInPrecedenceTableData() {
		return new Object[][]{
				{
						ImmutableList.of(
								new PrecedenceTable.Entry(ImmutableSet.of("foo", "bar"), Associativity.LEFT),
								new PrecedenceTable.Entry(ImmutableSet.of("baz", "xyz"), Associativity.LEFT)
						)
				}
		};
	}

	@Test(expected = IllegalStateException.class)
	@UseDataProvider("getTestUnknownTerminalInPrecedenceTableData")
	public void testUnknownTerminalInPrecedenceTable(ImmutableList<PrecedenceTable.Entry> entries) {
		PrecedenceTable precedenceTable = new PrecedenceTable(entries);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@DataProvider
	public static Object[][] getTestDuplicateTerminalInPrecedenceTableData() {
		return new Object[][]{
				{
						ImmutableList.of(
								new PrecedenceTable.Entry(ImmutableSet.of("foo"), Associativity.LEFT),
								new PrecedenceTable.Entry(ImmutableSet.of("bar"), Associativity.LEFT),
								new PrecedenceTable.Entry(ImmutableSet.of("baz", "foo"), Associativity.LEFT)
						)
				}
		};
	}

	@Test(expected = IllegalStateException.class)
	@UseDataProvider("getTestDuplicateTerminalInPrecedenceTableData")
	public void testDuplicateTerminalInPrecedenceTable(ImmutableList<PrecedenceTable.Entry> entries) {
		PrecedenceTable precedenceTable = new PrecedenceTable(entries);
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
		Alternative alternative = new Alternative(new SymbolReference("foo"));
		ImmutableList<Production> productions = ImmutableList.of(
				new Production("unknown", ImmutableList.of(alternative))
		);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
		new GrammarValidator(grammar).validate();
	}

//	@DataProvider
//	public static Object[][] getTestUnknownSymbolReferenceInSecondProductionData() {
//		Alternative unknownFirst = new Alternative(new SequenceExpression(new SymbolReference("unknown"), new SymbolReference("bar")));
//		Alternative unknownSecond = new Alternative(new SequenceExpression(new SymbolReference("bar"), new SymbolReference("unknown")));
//		Alternative unknownPrecedence = new Alternative(new SymbolReference("bar"), Alternative.PrecedenceSpecificationType.EXPLICIT, "xyz");
//
//		return new Object[][]{
//				{
//						ImmutableList.of(
//								new Production("nt2", ImmutableList.of(new Alternative())),
//								PRODUCTION_1,
//								)
//				},
//				{
//						ImmutableList.of(
//								new Production("nt2", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("unknown"))),
//								PRODUCTION_1,
//								)
//				},
//				{
//						ImmutableList.of(
//								PRODUCTION_1,
//								new Production("nt2", new SequenceExpression(new SymbolReference("unknown"), new SymbolReference("bar")))
//						)
//				},
//				{
//						ImmutableList.of(
//								PRODUCTION_1,
//								new Production("nt2", new SequenceExpression(new SymbolReference("foo"), new SymbolReference("unknown")))
//						)
//				}
//		};
//	}
//
//	@Test(expected = IllegalStateException.class)
//	@UseDataProvider("getTestUnknownSymbolReferenceInSecondProductionData")
//	public void testUnknownSymbolReference(ImmutableList<Production> productions) {
//		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
//		new GrammarValidator(grammar).validate();
//	}
//
//
//	@DataProvider
//	public static Object[][] getTestUnknownSymbolReferenceInSecondProductionData() {
//
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testValidateExpression() {
//		Alternative alternative = new Alternative(new SymbolReference("foo"));
//		ImmutableList<Production> productions = ImmutableList.of(
//				new Production("unknown", ImmutableList.of(alternative))
//		);
//		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
//		new GrammarValidator(grammar).validate();
//	}

}
