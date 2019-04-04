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
		Grammar grammar = new Grammar(TERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateTerminalName() {
		TerminalDeclaration conflictingTerminalDeclaration = new TerminalDeclaration("foo");
		ImmutableList<TerminalDeclaration> terminals = ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3, conflictingTerminalDeclaration);
		Grammar grammar = new Grammar(terminals, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateNonterminalName() {
		Production conflictingProduction = new Production("nt2", ImmutableList.of(ALTERNATIVE_3));
		ImmutableList<Production> productions = ImmutableList.of(PRODUCTION_1, PRODUCTION_2, PRODUCTION_3, conflictingProduction);
		Grammar grammar = new Grammar(TERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test(expected = IllegalStateException.class)
	public void testSameNameForTerminalAndNonterminal() {
		Production conflictingProduction = new Production("foo", ImmutableList.of(ALTERNATIVE_3));
		ImmutableList<Production> productions = ImmutableList.of(PRODUCTION_1, PRODUCTION_2, PRODUCTION_3, conflictingProduction);
		Grammar grammar = new Grammar(TERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, productions);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownTerminalInPrecedenceTable() {
		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
				new PrecedenceTable.Entry(ImmutableList.of("foo", "bar"), Associativity.LEFT),
				new PrecedenceTable.Entry(ImmutableList.of("baz", "xyz"), Associativity.LEFT)
		)
		);
		Grammar grammar = new Grammar(TERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateTerminalInPrecedenceTable() {
		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
				new PrecedenceTable.Entry(ImmutableList.of("foo"), Associativity.LEFT),
				new PrecedenceTable.Entry(ImmutableList.of("bar"), Associativity.LEFT),
				new PrecedenceTable.Entry(ImmutableList.of("baz", "foo"), Associativity.LEFT)
		)
		);
		Grammar grammar = new Grammar(TERMINALS, precedenceTable, START_NONTERMINAL_NAME, PRODUCTIONS);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownStartNonterminal() {
		Grammar grammar = new Grammar(TERMINALS, PRECEDENCE_TABLE_EMPTY, "unknown", PRODUCTIONS);
		new GrammarValidator(grammar).validate(ErrorReporter.EXCEPTION_THROWER);
	}

	@Test
	public void testProductionValidatorGetsCalled() {
		Grammar grammar = new Grammar(TERMINALS, PRECEDENCE_TABLE_EMPTY, START_NONTERMINAL_NAME, PRODUCTIONS);
		GrammarValidator.ProductionValidatorFactory productionValidatorFactory = (terminalNames, nonterminalNames, startSymbol) -> {
			Assert.assertEquals(ImmutableSet.of(TERMINAL_1.getName(), TERMINAL_2.getName(), TERMINAL_3.getName()), terminalNames);
			Assert.assertEquals(ImmutableSet.of("dummyStart", "nt1", "nt2"), nonterminalNames);
			Assert.assertEquals(START_NONTERMINAL_NAME, startSymbol);
			return new ProductionValidator() {

				int counter = 0;

				@Override
				public void validateProduction(Production production, ErrorReporter errorReporter) {
					if (counter == 0) {
						Assert.assertEquals(PRODUCTION_1, production);
					} else if (counter == 1) {
						Assert.assertEquals(PRODUCTION_2, production);
					} else if (counter == 2) {
						Assert.assertEquals(PRODUCTION_3, production);
					} else {
						Assert.fail("invalid counter: " + counter);
					}
					counter++;
				}

				@Override
				public void finish(ErrorReporter errorReporter) {
					Assert.assertEquals(3, counter);
				}

			};
		};
		new GrammarValidator(grammar, productionValidatorFactory).validate(ErrorReporter.EXCEPTION_THROWER);
	}

}
