package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.codegen.MapagParserClassGenerator;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;

import java.util.Properties;

/**
 * This is the bootstrapper for the calculator grammar (test project). This should ultimately be defined in a mapag
 * specification file, but for now, we don't actually have a parser for these specification files.
 */
public class CalculatorParserGenerationMain {

	public static void main(String[] args) {

		Properties codeGenerationProperties = new Properties();
		codeGenerationProperties.setProperty("parser.package", "name.martingeisse.calculator");
		codeGenerationProperties.setProperty("parser.class", "MapagGeneratedCalculationParser");

		ImmutableList terminalDeclarations = ImmutableList.of(
			new TerminalDeclaration("PLUS"),
			new TerminalDeclaration("MINUS"),
			new TerminalDeclaration("TIMES"),
			new TerminalDeclaration("DIVIDED_BY"),
			new TerminalDeclaration("OPENING_PARENTHESIS"),
			new TerminalDeclaration("CLOSING_PARENTHESIS"),
			new TerminalDeclaration("SEMICOLON"),
			new TerminalDeclaration("IDENTIFIER"),
			new TerminalDeclaration("NUMBER")
		);

		ImmutableList nonterminalDeclarations = ImmutableList.of(
			new NonterminalDeclaration("calculation"),
			new NonterminalDeclaration("statement"),
			new NonterminalDeclaration("expression")
		);

		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry(ImmutableSet.of("PLUS", "MINUS"), Associativity.LEFT),
			new PrecedenceTable.Entry(ImmutableSet.of("TIMES", "DIVIDED_BY"), Associativity.LEFT)
		));

		String startNonterminalName = "calculation";

		ImmutableList<Production> productions = ImmutableList.of(
			new Production("calculation", ImmutableList.of(
				new Alternative(new ZeroOrMoreExpression(symbol("statement")), null)
			)),
			new Production("statement", ImmutableList.of(
				new Alternative(sequence(symbol("expression"), symbol("SEMICOLON")), null)
			)),
			new Production("expression", ImmutableList.of(
				new Alternative(symbol("NUMBER"), null),
				new Alternative(symbol("IDENTIFIER"), null),
				new Alternative(sequence(symbol("expression"), or(symbol("PLUS"), symbol("MINUS")), symbol("expression")), "PLUS"),
				new Alternative(sequence(symbol("expression"), or(symbol("TIMES"), symbol("DIVIDED_BY")), symbol("expression")), "TIMES"),
				new Alternative(sequence(symbol("OPENING_PARENTHESIS"), symbol("expression"), symbol("CLOSING_PARENTHESIS")), null)
			))
		);

		Grammar grammar = new Grammar(terminalDeclarations, nonterminalDeclarations, precedenceTable, startNonterminalName, productions);
		GrammarInfo grammarInfo = new GrammarInfo(new GrammarCanonicalizer(grammar).run().getResult());
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();

		MapagParserClassGenerator mapagParserClassGenerator = new MapagParserClassGenerator(grammarInfo, stateMachine, codeGenerationProperties);
		mapagParserClassGenerator.generate();
	}

	private static SymbolReference symbol(String name) {
		return new SymbolReference(name);
	}

	private static Expression sequence(Expression... expressions) {
		return sequence(0, expressions);
	}

	private static Expression sequence(int i, Expression... expressions) {
		if (i == expressions.length) {
			return new EmptyExpression();
		} else if (i == expressions.length - 1) {
			return expressions[i];
		} else {
			return new SequenceExpression(expressions[i], sequence(i + 1, expressions));
		}
	}

	private static Expression or(Expression... expressions) {
		return or(0, expressions);
	}

	private static Expression or(int i, Expression... expressions) {
		if (i == expressions.length) {
			return new EmptyExpression();
		} else if (i == expressions.length - 1) {
			return expressions[i];
		} else {
			return new OrExpression(expressions[i], or(i + 1, expressions));
		}
	}

}
