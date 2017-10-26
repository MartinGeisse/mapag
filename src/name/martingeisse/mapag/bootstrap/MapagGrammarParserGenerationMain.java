package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.codegen.MapagParserClassGenerator;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;

/**
 *
 */
public class MapagGrammarParserGenerationMain {

	public static void main(String[] args) {

		String packageName = "name.martingeisse.mapag.input";
		String className = "MapagGeneratedMapagParser";

		ImmutableList terminalDeclarations = ImmutableList.of(
			new TerminalDeclaration("KW_PACKAGE"),
			new TerminalDeclaration("KW_CLASS"),
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
			new TerminalDeclaration("IDENTIFIER")
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
		));

		String startNonterminalName = "grammar";

		ImmutableList<Production> productions = ImmutableList.of(
			new Production("grammar", ImmutableList.of(
				new Alternative(sequence(
					symbol("KW_PACKAGE"),
					new ZeroOrMoreExpression(new SequenceExpression(symbol("IDENTIFIER"), symbol("DOT"))),
					symbol("IDENTIFIER"),
					symbol("SEMICOLON"),
					symbol("KW_CLASS"),
					symbol("IDENTIFIER"),
					symbol("SEMICOLON"),
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
				), null)
			)),
			new Production("precedenceDeclaration", ImmutableList.of(
				new Alternative(sequence(
					or(symbol("KW_LEFT"), symbol("KW_RIGHT"), symbol("KW_NONASSOC")),
					symbol("nonemptyIdentifierList"),
					symbol("SEMICOLON")
				), null)
			)),
			new Production("production", ImmutableList.of(
				new Alternative(sequence(
					symbol("IDENTIFIER"),
					symbol("COLON_COLON_EQUALS"),
					symbol("alternative"),
					new ZeroOrMoreExpression(sequence(symbol("BAR"), symbol("alternative"))),
					symbol("SEMICOLON")
				), null)
			)),
			new Production("alternative", ImmutableList.of(
				new Alternative(sequence(
					new OneOrMoreExpression(symbol("toplevelExpression")),
					new OptionalExpression(sequence(symbol("KW_PRECEDENCE"), symbol("IDENTIFIER")))
				), null)
			)),
			new Production("toplevelExpression", ImmutableList.of(
				new Alternative(sequence(
					symbol("IDENTIFIER"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null),
				new Alternative(sequence(symbol("toplevelExpression"), symbol("ASTERISK")), null),
				new Alternative(sequence(symbol("toplevelExpression"), symbol("PLUS")), null),
				new Alternative(sequence(symbol("toplevelExpression"), symbol("QUESTION_MARK")), null),
				new Alternative(sequence(
					symbol("OPENING_PARENTHESIS"),
					new OneOrMoreExpression(symbol("nestedExpression")),
					symbol("OPENING_PARENTHESIS"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null)
			)),
			new Production("nestedExpression", ImmutableList.of(
				new Alternative(sequence(
					symbol("IDENTIFIER"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null),
				new Alternative(sequence(
					symbol("nestedExpression"),
					symbol("BAR"),
					symbol("toplevelExpression")
				), null),
				new Alternative(sequence(symbol("nestedExpression"), symbol("ASTERISK")), null),
				new Alternative(sequence(symbol("nestedExpression"), symbol("PLUS")), null),
				new Alternative(sequence(symbol("nestedExpression"), symbol("QUESTION_MARK")), null),
				new Alternative(sequence(
					symbol("OPENING_PARENTHESIS"),
					new OneOrMoreExpression(symbol("nestedExpression")),
					symbol("OPENING_PARENTHESIS"),
					new OptionalExpression(sequence(symbol("COLON"), symbol("IDENTIFIER")))
				), null)
			)),
			new Production("nonemptyIdentifierList", ImmutableList.of(
				new Alternative(
					sequence(symbol("IDENTIFIER"), new ZeroOrMoreExpression(sequence(symbol("COMMA"), symbol("IDENTIFIER"))))
					, null)
			))
		);

		Grammar grammar = new Grammar(packageName, className, terminalDeclarations, nonterminalDeclarations, precedenceTable, startNonterminalName, productions);
		GrammarInfo grammarInfo = new GrammarInfo(new GrammarCanonicalizer(grammar).getResult());
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();

		MapagParserClassGenerator mapagParserClassGenerator = new MapagParserClassGenerator(grammarInfo, stateMachine);
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
