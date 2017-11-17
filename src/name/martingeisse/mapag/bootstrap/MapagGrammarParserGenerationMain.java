package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.codegen.ParserClassGenerator;
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
 * TODO handle alternative names and corresponding new syntax -- see grammar-mapag for details
 */
public class MapagGrammarParserGenerationMain {

	public static void main(String[] args) throws Exception {

		Properties codeGenerationProperties = new Properties();
		codeGenerationProperties.setProperty("parser.package", "name.martingeisse.mapag.input");
		codeGenerationProperties.setProperty("parser.class", "MapagGeneratedMapagParser");
		codeGenerationProperties.setProperty("parser.fileElementType", "MapagParserDefinition.FILE_ELEMENT_TYPE");
		codeGenerationProperties.setProperty("symbolHolder.generate", "true");
		codeGenerationProperties.setProperty("symbolHolder.package", "name.martingeisse.mapag.input");
		codeGenerationProperties.setProperty("symbolHolder.class", "Symbols");
		codeGenerationProperties.setProperty("symbol.elementType.class", "MapagElementType");
		codeGenerationProperties.setProperty("psi.generate", "true");
		codeGenerationProperties.setProperty("psi.package", "name.martingeisse.mapag.input.psi");
		Configuration configuration = new Configuration(codeGenerationProperties);

		ImmutableList terminalDeclarations = ImmutableList.of(
			new TerminalDeclaration("KW_TERMINALS"),
			new TerminalDeclaration("KW_NONTERMINALS"),
			new TerminalDeclaration("KW_PRECEDENCE"),
			new TerminalDeclaration("KW_LEFT"),
			new TerminalDeclaration("KW_RIGHT"),
			new TerminalDeclaration("KW_NONASSOC"),
			new TerminalDeclaration("KW_START"),
			new TerminalDeclaration("OPENING_CURLY_BRACE"),
			new TerminalDeclaration("CLOSING_CURLY_BRACE"),
			new TerminalDeclaration("OPENING_PARENTHESIS"),
			new TerminalDeclaration("CLOSING_PARENTHESIS"),
			new TerminalDeclaration("DOT"),
			new TerminalDeclaration("COMMA"),
			new TerminalDeclaration("SEMICOLON"),
			new TerminalDeclaration("COLON"),
			new TerminalDeclaration("EXPANDS_TO"),
			new TerminalDeclaration("QUESTION_MARK"),
			new TerminalDeclaration("ASTERISK"),
			new TerminalDeclaration("PLUS"),
			new TerminalDeclaration("BAR"),
			new TerminalDeclaration("IDENTIFIER"),
			new TerminalDeclaration("COMMENT") // never passed to the parser
		);

		ImmutableList nonterminalDeclarations = ImmutableList.of(
			new NonterminalDeclaration("grammar"),
			new NonterminalDeclaration("precedenceDeclaration"),
			new NonterminalDeclaration("production"),
			new NonterminalDeclaration("alternative"),
			new NonterminalDeclaration("toplevelExpression"),
			new NonterminalDeclaration("nestedExpression"),
			new NonterminalDeclaration("nonemptyIdentifierList")
		);

		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry(ImmutableSet.of("BAR"), Associativity.LEFT),
			new PrecedenceTable.Entry(ImmutableSet.of("ASTERISK", "PLUS", "QUESTION_MARK"), Associativity.NONASSOC)
		));

		String startNonterminalName = "grammar";

		ImmutableList<Production> productions = ImmutableList.of(
			new Production("grammar", ImmutableList.of(
				new Alternative(null, sequence(
					symbol("KW_TERMINALS"),
					symbol("OPENING_CURLY_BRACE"),
					symbol("nonemptyIdentifierList"),
					symbol("CLOSING_CURLY_BRACE"),
					symbol("KW_NONTERMINALS"),
					symbol("OPENING_CURLY_BRACE"),
					symbol("nonemptyIdentifierList"),
					symbol("CLOSING_CURLY_BRACE"),
					new OptionalExpression(sequence(
						symbol("KW_PRECEDENCE"),
						symbol("OPENING_CURLY_BRACE"),
						new ZeroOrMoreExpression(symbol("precedenceDeclaration")),
						symbol("CLOSING_CURLY_BRACE")
					)),
					symbol("KW_START"),
					symbol("IDENTIFIER"),
					symbol("SEMICOLON"),
					new OneOrMoreExpression(symbol("production"))
				), null, null)
			)),
			new Production("precedenceDeclaration", ImmutableList.of(
				new Alternative(null, sequence(
					or(symbol("KW_LEFT"), symbol("KW_RIGHT"), symbol("KW_NONASSOC")),
					symbol("nonemptyIdentifierList"),
					symbol("SEMICOLON")
				), null, null)
			)),
			new Production("production", ImmutableList.of(
				new Alternative(null, sequence(
					symbol("IDENTIFIER").withName("nonterminal"),
					symbol("EXPANDS_TO"),
					symbol("alternative"),
					new ZeroOrMoreExpression(sequence(symbol("BAR"), symbol("alternative"))),
					symbol("SEMICOLON")
				), null, null)
			)),
			new Production("alternative", ImmutableList.of(
				new Alternative(null, sequence(
					new OneOrMoreExpression(symbol("toplevelExpression")),
					new OptionalExpression(sequence(symbol("KW_PRECEDENCE"), symbol("IDENTIFIER")))
				), null, null)
			)),
			new Production("toplevelExpression", ImmutableList.of(
				new Alternative(null, sequence(
					symbol("IDENTIFIER"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null, null),
				new Alternative(null, sequence(symbol("toplevelExpression"), symbol("ASTERISK")), "ASTERISK", null),
				new Alternative(null, sequence(symbol("toplevelExpression"), symbol("PLUS")), "PLUS", null),
				new Alternative(null, sequence(symbol("toplevelExpression"), symbol("QUESTION_MARK")), "QUESTION_MARK", null),
				new Alternative(null, sequence(
					symbol("OPENING_PARENTHESIS"),
					new OneOrMoreExpression(symbol("nestedExpression")),
					symbol("CLOSING_PARENTHESIS"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null, null)
			)),
			new Production("nestedExpression", ImmutableList.of(
				new Alternative(null, sequence(
					symbol("IDENTIFIER"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null, null),
				new Alternative(null, sequence(
					symbol("nestedExpression"),
					symbol("BAR"),
					symbol("toplevelExpression")
				), "BAR", null),
				new Alternative(null, sequence(symbol("nestedExpression"), symbol("ASTERISK")), "ASTERISK", null),
				new Alternative(null, sequence(symbol("nestedExpression"), symbol("PLUS")), "PLUS", null),
				new Alternative(null, sequence(symbol("nestedExpression"), symbol("QUESTION_MARK")), "QUESTION_MARK", null),
				new Alternative(null, sequence(
					symbol("OPENING_PARENTHESIS"),
					new OneOrMoreExpression(symbol("nestedExpression")),
					symbol("CLOSING_PARENTHESIS"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null, null)
			)),
			new Production("nonemptyIdentifierList", ImmutableList.of(
				new Alternative(null,
					sequence(symbol("IDENTIFIER"), new ZeroOrMoreExpression(sequence(symbol("COMMA"), symbol("IDENTIFIER"))))
					, null, null)
			))
		);

		Grammar grammar = new Grammar(terminalDeclarations, nonterminalDeclarations, precedenceTable, startNonterminalName, productions);
		GrammarInfo grammarInfo = new GrammarInfo(new GrammarCanonicalizer(grammar).run().getResult());
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();
		OutputFileFactory outputFileFactory = (packageName, className) -> {
			File baseFolder = new File("grammar_gen_out");
			if (!baseFolder.isDirectory()) {
				baseFolder.mkdir();
			}
			File packageFolder = new File(baseFolder, packageName);
			if (!packageFolder.isDirectory()) {
				packageFolder.mkdir();
			}
			return new FileOutputStream(new File(packageFolder, className + ".java"));
		};
		new ParserClassGenerator(grammarInfo, stateMachine, configuration, outputFileFactory).generate();

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
