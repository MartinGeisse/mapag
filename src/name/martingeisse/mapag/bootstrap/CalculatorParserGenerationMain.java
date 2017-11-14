package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * This is the bootstrapper for the calculator grammar (test project). This should ultimately be defined in a mapag
 * specification file, but for now, we don't actually have a parser for these specification files.
 */
public class CalculatorParserGenerationMain {

	public static void main(String[] args) throws Exception {

		Properties codeGenerationProperties = new Properties();
		codeGenerationProperties.setProperty("parser.package", "name.martingeisse.calculator");
		codeGenerationProperties.setProperty("parser.class", "MapagGeneratedCalculationParser");
		codeGenerationProperties.setProperty("parser.fileElementType", "CalculatorParserDefinition.FILE_ELEMENT_TYPE");
		codeGenerationProperties.setProperty("symbolHolder.package", "name.martingeisse.calculator");
		codeGenerationProperties.setProperty("symbolHolder.class", "Symbols");
		codeGenerationProperties.setProperty("symbol.elementType.class", "CalculatorElementType");
		codeGenerationProperties.setProperty("psi.generate", "true");
		codeGenerationProperties.setProperty("psi.package", "name.martingeisse.calculator.psi");
		Configuration configuration = new Configuration(codeGenerationProperties);

		ImmutableList<TerminalDeclaration> terminalDeclarations = ImmutableList.of(
			new TerminalDeclaration("PLUS"),
			new TerminalDeclaration("MINUS"),
			new TerminalDeclaration("TIMES"),
			new TerminalDeclaration("DIVIDED_BY"),
			new TerminalDeclaration("OPENING_PARENTHESIS"),
			new TerminalDeclaration("CLOSING_PARENTHESIS"),
			new TerminalDeclaration("SEMICOLON"),
			new TerminalDeclaration("IDENTIFIER"),
			new TerminalDeclaration("NUMBER"),
			new TerminalDeclaration("BLOCK_COMMENT"), // never passed to the parser
			new TerminalDeclaration("LINE_COMMENT") // never passed to the parser
		);

		ImmutableList<NonterminalDeclaration> nonterminalDeclarations = ImmutableList.of(
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
				new Alternative(null, new ZeroOrMoreExpression(symbol("statement")).withName("statements"), null)
			)),
			new Production("statement", ImmutableList.of(
				new Alternative("expression", sequence(symbol("expression").withName("expression"), symbol("SEMICOLON")), null),
				new Alternative("error", sequence(symbol("%error"), symbol("SEMICOLON")), null)
			)),
			new Production("expression", ImmutableList.of(
				new Alternative("literal", symbol("NUMBER").withName("value"), null),
				new Alternative("variable", symbol("IDENTIFIER").withName("variableName"), null),
				new Alternative("additive", sequence(
					symbol("expression").withName("left"),
					or(symbol("PLUS"), symbol("MINUS")).withName("operator"),
					symbol("expression").withName("right")
				), "PLUS"),
				new Alternative("multiplicative", sequence(
					symbol("expression").withName("left"),
					or(symbol("TIMES"), symbol("DIVIDED_BY")).withName("operator"),
					symbol("expression").withName("right")
				), "TIMES"),
				new Alternative("parenthesized", sequence(symbol("OPENING_PARENTHESIS"), symbol("expression").withName("inner"), symbol("CLOSING_PARENTHESIS")), null)
			))
		);

		Grammar grammar = new Grammar(terminalDeclarations, nonterminalDeclarations, precedenceTable, startNonterminalName, productions);
		GrammarInfo grammarInfo = new GrammarInfo(new GrammarCanonicalizer(grammar).run().getResult());
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();
		OutputFileFactory outputFileFactory = (packageName, className) -> {
			File baseFolder = new File("calc_gen_out");
			if (!baseFolder.isDirectory()) {
				baseFolder.mkdir();
			}
			File packageFolder = new File(baseFolder, packageName);
			if (!packageFolder.isDirectory()) {
				packageFolder.mkdir();
			}
			return new FileOutputStream(new File(packageFolder, className + ".java"));
		};
		new CodeGenerationDriver(grammarInfo, stateMachine, configuration, outputFileFactory).generate();

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
