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
			new TerminalDeclaration("KW_RESOLVE"),
			new TerminalDeclaration("KW_SHIFT"),
			new TerminalDeclaration("KW_REDUCE"),
			new TerminalDeclaration("KW_EOF"),
			new TerminalDeclaration("KW_ERROR"),
			new TerminalDeclaration("OPENING_CURLY_BRACE"),
			new TerminalDeclaration("CLOSING_CURLY_BRACE"),
			new TerminalDeclaration("OPENING_PARENTHESIS"),
			new TerminalDeclaration("CLOSING_PARENTHESIS"),
			new TerminalDeclaration("COMMA"),
			new TerminalDeclaration("SEMICOLON"),
			new TerminalDeclaration("COLON"),
			new TerminalDeclaration("EXPANDS_TO"),
			new TerminalDeclaration("QUESTION_MARK"),
			new TerminalDeclaration("ASTERISK"),
			new TerminalDeclaration("PLUS"),
			new TerminalDeclaration("BAR"),
			new TerminalDeclaration("IDENTIFIER"),
			new TerminalDeclaration("BLOCK_COMMENT"), // never passed to the parser
			new TerminalDeclaration("LINE_COMMENT") // never passed to the parser
		);

		ImmutableList nonterminalDeclarations = ImmutableList.of(
			new NonterminalDeclaration("grammar"),
			new NonterminalDeclaration("precedenceDeclaration"),
			new NonterminalDeclaration("production"),

			new NonterminalDeclaration("rightHandSide"),
			new NonterminalDeclaration("expression"),
			new NonterminalDeclaration("resolveDeclaration"),
			new NonterminalDeclaration("resolveDeclarationSymbol"),
			new NonterminalDeclaration("nonemptyIdentifierList")
		);

		PrecedenceTable precedenceTable = null;

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
					optional(
						symbol("KW_PRECEDENCE"),
						symbol("OPENING_CURLY_BRACE"),
						zeroOrMore(symbol("precedenceDeclaration")),
						symbol("CLOSING_CURLY_BRACE")
					),
					symbol("KW_START"),
					symbol("IDENTIFIER").withName("startSymbolName"),
					symbol("SEMICOLON"),
					oneOrMore(symbol("production"))
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
				new Alternative("single", sequence(
					symbol("IDENTIFIER"),
					optional(symbol("COLON"), symbol("IDENTIFIER")),
					symbol("EXPANDS_TO"),
					symbol("rightHandSide"),
					symbol("SEMICOLON")
				), null, null),
				new Alternative("multi", sequence(
					symbol("IDENTIFIER"),
					symbol("EXPANDS_TO"),
					symbol("OPENING_CURLY_BRACE"),
					zeroOrMore(
						optional(symbol("IDENTIFIER"), symbol("EXPANDS_TO")),
						symbol("rightHandSide"),
						symbol("SEMICOLON")
					),
					symbol("CLOSING_CURLY_BRACE")
				), null, null),
				new Alternative("error", sequence(
					symbol("error"),
					symbol("SEMICOLON")
				), null, null)
			)),
			new Production("rightHandSide", ImmutableList.of(
				new Alternative("withoutResolver", symbol("expression"), null, null),
				new Alternative("withPrecedenceResolver", sequence(
					symbol("expression"),
					symbol("KW_PRECEDENCE"),
					symbol("IDENTIFIER")
				), null, null),
				new Alternative("withExplicitResolver", sequence(
					symbol("expression"),
					symbol("KW_RESOLVE"),
					symbol("OPENING_CURLY_BRACE"),
					zeroOrMore(symbol("resolveDeclaration")),
					symbol("CLOSING_CURLY_BRACE")
				), null, null)
			)),
			new Production("resolveDeclaration", ImmutableList.of(
				new Alternative(null, sequence(
					or(symbol("KW_SHIFT"), symbol("KW_REDUCE")),
					symbol("resolveDeclarationSymbol"),
					zeroOrMore(symbol("COMMA"), symbol("resolveDeclarationSymbol")),
					symbol("SEMICOLON")
				), null, null)
			)),
			new Production("resolveDeclaration", ImmutableList.of(
				new Alternative("identifier", symbol("IDENTIFIER"), null, null),
				new Alternative("eof", symbol("KW_EOF"), null, null),
			)),
			new Production("expression", ImmutableList.of(
				new Alternative("identifier", symbol("IDENTIFIER"), null, null),
				new Alternative("sequence", sequence(
					symbol("expression"),
					symbol("expression")
				), null, null),
				new Alternative("or", sequence(
					symbol("expression"),
					symbol("BAR"),
					symbol("expression")
				), null, null),
				new Alternative("zeroOrMore", sequence(
					symbol("expression"),
					symbol("ASTERISK")
				), null, null),
				new Alternative("oneOrMore", sequence(
					symbol("expression"),
					symbol("PLUS")
				), null, null),
				new Alternative("optional", sequence(
					symbol("expression"),
					symbol("QUESTION_MARK")
				), null, null),
				new Alternative("parenthesized", sequence(
					symbol("OPENING_PARENTHESIS"),
					oneOrMore(symbol("expression")),
					symbol("CLOSING_PARENTHESIS")
				), null, null),
				new Alternative("named", sequence(
					symbol("expression"),
					symbol("COLON"),
					symbol("IDENTIFIER")
				), null, null)
			)),
			new Production("nonemptyIdentifierList", ImmutableList.of(
				new Alternative(null,
					sequence(symbol("IDENTIFIER"), zeroOrMore(symbol("COMMA"), symbol("IDENTIFIER")))
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
		if (expressions.length == 0) {
			return new EmptyExpression();
		} else if (expressions.length == 1) {
			return expressions[0];
		} else {
			return sequence(0, expressions);
		}
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

	private static OptionalExpression optional(Expression... expressions) {
		return new OptionalExpression(sequence(expressions));
	}

	private static ZeroOrMoreExpression zeroOrMore(Expression... expressions) {
		return new ZeroOrMoreExpression(sequence(expressions));
	}

	private static OneOrMoreExpression oneOrMore(Expression... expressions) {
		return new OneOrMoreExpression(sequence(expressions));
	}

}
