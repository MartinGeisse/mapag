package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.extended.*;
import org.junit.Assert;
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
				new PrecedenceTable.Entry(ImmutableSet.of("foo", "bar"), Associativity.LEFT),
				new PrecedenceTable.Entry(ImmutableSet.of("baz", "xyz"), Associativity.LEFT)
		)
		);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateTerminalInPrecedenceTable() {
		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
				new PrecedenceTable.Entry(ImmutableSet.of("foo"), Associativity.LEFT),
				new PrecedenceTable.Entry(ImmutableSet.of("bar"), Associativity.LEFT),
				new PrecedenceTable.Entry(ImmutableSet.of("baz", "foo"), Associativity.LEFT)
		)
		);
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownStartNonterminal() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, "unknown", PRODUCTIONS);
		new GrammarValidator(grammar).validate();
	}

	@Test
	public void testProductionValidatorGetsCalled() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		GrammarValidator.ProductionValidatorFactory productionValidatorFactory = (terminalNames, nonterminalNames, startSymbol) -> {
			Assert.assertEquals(ImmutableSet.of(TERMINAL_1.getName(), TERMINAL_2.getName(), TERMINAL_3.getName()), terminalNames);
			Assert.assertEquals(ImmutableSet.of(NONTERMINAL_1.getName(), NONTERMINAL_2.getName(), NONTERMINAL_3.getName()), nonterminalNames);
			Assert.assertEquals(START_NONTERMINAL_NAME, startSymbol);
			return new ProductionValidator() {

				int counter = 0;

				@Override
				public void validateProduction(Production production) {
					if (counter == 0) {
						Assert.assertEquals(PRODUCTION_1, production);
					} else if (counter == 1) {
						Assert.assertEquals(PRODUCTION_2, production);
					} else {
						Assert.fail("invalid counter: " + counter);
					}
					counter++;
				}

				@Override
				public void finish() {
					Assert.assertEquals(2, counter);
				}

			};
		};
		new GrammarValidator(grammar, productionValidatorFactory).validate();
	}

}
