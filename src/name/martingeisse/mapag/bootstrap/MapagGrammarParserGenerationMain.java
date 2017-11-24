package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import com.intellij.execution.ui.ConsoleViewContentType;
import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;
import name.martingeisse.mapag.sm.StateMachineException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 *
 */
public class MapagGrammarParserGenerationMain {

	public static void main(String[] args) throws Exception {
		try {
			run();
		} catch (StateMachineException.Conflict e) {
			PrintWriter printWriter = new PrintWriter(System.out);
			e.describe(printWriter);
			printWriter.flush();
		}
	}

	private static void run() throws Exception {
		Properties codeGenerationProperties = new Properties();
		codeGenerationProperties.setProperty("parser.package", "name.martingeisse.mapag.input");
		codeGenerationProperties.setProperty("parser.class", "MapagGeneratedMapagParser");
		codeGenerationProperties.setProperty("parser.fileElementType", "name.martingeisse.mapag.ide.MapagParserDefinition.FILE_ELEMENT_TYPE");

		codeGenerationProperties.setProperty("parser.error.KW_TERMINALS", "%terminals");
		codeGenerationProperties.setProperty("parser.error.KW_NONTERMINALS", "%nonterminals");
		codeGenerationProperties.setProperty("parser.error.KW_PRECEDENCE", "%precedence");
		codeGenerationProperties.setProperty("parser.error.KW_LEFT", "%left");
		codeGenerationProperties.setProperty("parser.error.KW_RIGHT", "%right");
		codeGenerationProperties.setProperty("parser.error.KW_NONASSOC", "%nonassoc");
		codeGenerationProperties.setProperty("parser.error.KW_START", "%start");
		codeGenerationProperties.setProperty("parser.error.KW_RESOLVE", "%resolve");
		codeGenerationProperties.setProperty("parser.error.KW_SHIFT", "%shift");
		codeGenerationProperties.setProperty("parser.error.KW_REDUCE", "%reduce");
		codeGenerationProperties.setProperty("parser.error.KW_EOF", "%eof");
		codeGenerationProperties.setProperty("parser.error.KW_ERROR", "%error");
		codeGenerationProperties.setProperty("parser.error.OPENING_CURLY_BRACE", "{");
		codeGenerationProperties.setProperty("parser.error.CLOSING_CURLY_BRACE", "}");
		codeGenerationProperties.setProperty("parser.error.OPENING_PARENTHESIS", "(");
		codeGenerationProperties.setProperty("parser.error.CLOSING_PARENTHESIS", ")");
		codeGenerationProperties.setProperty("parser.error.COMMA", ",");
		codeGenerationProperties.setProperty("parser.error.SEMICOLON", ";");
		codeGenerationProperties.setProperty("parser.error.COLON", ":");
		codeGenerationProperties.setProperty("parser.error.EXPANDS_TO", "::=");
		codeGenerationProperties.setProperty("parser.error.QUESTION_MARK", "?");
		codeGenerationProperties.setProperty("parser.error.ASTERISK", "*");
		codeGenerationProperties.setProperty("parser.error.PLUS", "+");
		codeGenerationProperties.setProperty("parser.error.BAR", "|");
		codeGenerationProperties.setProperty("parser.error.IDENTIFIER", "identifier");
		codeGenerationProperties.setProperty("parser.error.BLOCK_COMMENT", "/*");
		codeGenerationProperties.setProperty("parser.error.LINE_COMMENT", "//");
		codeGenerationProperties.setProperty("parser.error.precedenceDeclaration", "precedence declaration");
		codeGenerationProperties.setProperty("parser.error.production", "production");
		codeGenerationProperties.setProperty("parser.error.expression", "expression");
		codeGenerationProperties.setProperty("parser.error.resolveDeclaration", "resolve declaration");
		codeGenerationProperties.setProperty("parser.error.nonemptyIdentifierList", "identifier(s)");
		codeGenerationProperties.setProperty("symbolHolder.generate", "true");
		codeGenerationProperties.setProperty("symbolHolder.package", "name.martingeisse.mapag.input");
		codeGenerationProperties.setProperty("symbolHolder.class", "Symbols");
		codeGenerationProperties.setProperty("symbol.elementType.class", "MapagElementType");
		codeGenerationProperties.setProperty("psi.generate", "true");
		codeGenerationProperties.setProperty("psi.package", "name.martingeisse.mapag.input.psi");
		codeGenerationProperties.setProperty("context.parserDefinitionClass", "name.martingeisse.mapag.ide.MapagParserDefinition");
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
				alternative(null, sequence(
					symbol("KW_TERMINALS"),
					symbol("OPENING_CURLY_BRACE"),
					symbol("nonemptyIdentifierList").withName("terminals"),
					symbol("CLOSING_CURLY_BRACE"),
					symbol("KW_NONTERMINALS"),
					symbol("OPENING_CURLY_BRACE"),
					symbol("nonemptyIdentifierList").withName("nonterminals"),
					symbol("CLOSING_CURLY_BRACE"),
					optional(
						symbol("KW_PRECEDENCE"),
						symbol("OPENING_CURLY_BRACE"),
						zeroOrMore(symbol("precedenceDeclaration")).withName("precedenceDeclarations"),
						symbol("CLOSING_CURLY_BRACE")
					).withName("precedenceTable"),
					symbol("KW_START"),
					symbol("IDENTIFIER").withName("startSymbolName"),
					symbol("SEMICOLON"),
					oneOrMore(symbol("production")).withName("productions")
				))
			)),
			new Production("precedenceDeclaration", ImmutableList.of(
				alternative(null, sequence(
					or(symbol("KW_LEFT").withName("left"), symbol("KW_RIGHT").withName("right"), symbol("KW_NONASSOC").withName("nonassoc")).withName("associativity"),
					symbol("nonemptyIdentifierList").withName("terminals"),
					symbol("SEMICOLON")
				))
			)),
			new Production("production", ImmutableList.of(

				// single alternative. Note: an optional-expression for the name would make the grammar LR(>1)
				alternative("singleUnnamed", sequence(
					symbol("IDENTIFIER").withName("nonterminalName"),
					symbol("EXPANDS_TO"),
					symbol("rightHandSide").withName("rightHandSide"),
					symbol("SEMICOLON")
				)),
				alternative("singleNamed", sequence(
					symbol("IDENTIFIER").withName("nonterminalName"),
					symbol("COLON"),
					symbol("IDENTIFIER").withName("alternativeName"),
					symbol("EXPANDS_TO"),
					symbol("rightHandSide").withName("rightHandSide"),
					symbol("SEMICOLON")
				)),

				// multiple alternatives
				alternative("multi", sequence(
					symbol("IDENTIFIER").withName("nonterminalName"),
					symbol("EXPANDS_TO"),
					symbol("OPENING_CURLY_BRACE"),
					zeroOrMore(
						or( // again, an optional-expression would make the grammar LR(>1)
							sequence(
								symbol("rightHandSide").withName("rightHandSide"),
								symbol("SEMICOLON")
							).withName("unnamed"),
							sequence(
								symbol("IDENTIFIER").withName("alternativeName"),
								symbol("EXPANDS_TO"),
								symbol("rightHandSide").withName("rightHandSide"),
								symbol("SEMICOLON")
							).withName("named")
						)
					).withName("alternatives"),
					symbol("CLOSING_CURLY_BRACE")
				)),
				alternative("error", sequence(
					symbol("%error"),
					symbol("SEMICOLON")
				))
			)),
			new Production("rightHandSide", ImmutableList.of(
				alternative("withoutResolver", symbol("expression").withName("expression")),
				alternative("withPrecedenceResolver", sequence(
					symbol("expression").withName("expression"),
					symbol("KW_PRECEDENCE"),
					symbol("IDENTIFIER").withName("precedenceDefiningTerminal")
				)),
				alternative("withExplicitResolver", sequence(
					symbol("expression").withName("expression"),
					symbol("KW_RESOLVE"),
					symbol("OPENING_CURLY_BRACE"),
					zeroOrMore(symbol("resolveDeclaration")).withName("resolveDeclarations"),
					symbol("CLOSING_CURLY_BRACE")
				))
			)),
			new Production("resolveDeclaration", ImmutableList.of(
				alternative(null, sequence(
					or(symbol("KW_SHIFT").withName("shift"), symbol("KW_REDUCE").withName("reduce")).withName("action"),
					symbol("resolveDeclarationSymbol").withName("firstSymbol"),
					zeroOrMore(
						symbol("COMMA"),
						symbol("resolveDeclarationSymbol").withName("symbol")
					).withName("additionalSymbols"),
					symbol("SEMICOLON")
				))
			)),
			new Production("resolveDeclarationSymbol", ImmutableList.of(
				alternative("identifier", symbol("IDENTIFIER").withName("symbol")),
				alternative("eof", symbol("KW_EOF"))
			)),
			new Production("expression", ImmutableList.of(
				alternative("identifier", symbol("IDENTIFIER").withName("identifier")),
				alternative("error", symbol("KW_ERROR")),
				alternativeWithResolution("sequence", sequence(
					symbol("expression").withName("left"),
					symbol("expression").withName("right")
				), ImmutableList.of("QUESTION_MARK", "ASTERISK", "PLUS", "COLON"), ImmutableList.of("OPENING_PARENTHESIS", "IDENTIFIER", "KW_ERROR", "BAR")),
				alternativeWithResolution("or", sequence(
					symbol("expression").withName("left"),
					symbol("BAR"),
					symbol("expression").withName("right")
				), ImmutableList.of("QUESTION_MARK", "ASTERISK", "PLUS", "COLON", "OPENING_PARENTHESIS", "IDENTIFIER", "KW_ERROR"), ImmutableList.of("BAR")),
				alternative("zeroOrMore", sequence(
					symbol("expression").withName("operand"),
					symbol("ASTERISK")
				)),
				alternative("oneOrMore", sequence(
					symbol("expression").withName("operand"),
					symbol("PLUS")
				)),
				alternative("optional", sequence(
					symbol("expression").withName("operand"),
					symbol("QUESTION_MARK")
				)),
				alternative("parenthesized", sequence(
					symbol("OPENING_PARENTHESIS"),
					symbol("expression").withName("inner"),
					symbol("CLOSING_PARENTHESIS")
				)),
				alternative("named", sequence(
					symbol("expression").withName("expression"),
					symbol("COLON"),
					symbol("IDENTIFIER").withName("expressionName")
				))
			)),
			new Production("nonemptyIdentifierList", ImmutableList.of(
				alternative(null, sequence(
					symbol("IDENTIFIER").withName("firstIdentifier"),
					zeroOrMore(
						symbol("COMMA"),
						symbol("IDENTIFIER").withName("identifier")
					).withName("moreIdentifiers")
				))
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
		new CodeGenerationDriver(grammarInfo, stateMachine, configuration, outputFileFactory).generate();

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

	private static Alternative alternative(String name, Expression expression) {
		return new Alternative(name, expression, null, null);
	}

	private static Alternative alternativeWithResolution(String name, Expression expression, ImmutableList<String> shiftTerminals, ImmutableList<String> reduceTerminals) {
		ResolveDeclaration shiftDeclaration = new ResolveDeclaration(ConflictResolution.SHIFT, shiftTerminals);
		ResolveDeclaration reduceDeclaration = new ResolveDeclaration(ConflictResolution.REDUCE, reduceTerminals);
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of(shiftDeclaration, reduceDeclaration));
		return new Alternative(name, expression, null, resolveBlock);
	}

}
