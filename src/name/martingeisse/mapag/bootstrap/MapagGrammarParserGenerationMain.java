package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.codegen.MapagParserClassGenerator;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.extended.*;
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
			new TerminalDeclaration("IDENTIFIER")
		);

		ImmutableList nonterminalDeclarations = ImmutableList.of(
			new NonterminalDeclaration("grammar"),
			new NonterminalDeclaration("symbolDefinitions"),
			new NonterminalDeclaration("symbolDefinition"),
			new NonterminalDeclaration("precedenceList"),
			new NonterminalDeclaration("precedenceDeclaration"),
			new NonterminalDeclaration("productionList"),
			new NonterminalDeclaration("production"),
			new NonterminalDeclaration("expression"),
			new NonterminalDeclaration("hierarchicalIdentifier"),
			new NonterminalDeclaration("nonemptyIdentifierList")
		);

		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
		));

		String startNonterminalName = "grammar";

		ImmutableList<Production> productions = ImmutableList.of(

		);

		Grammar grammar = new Grammar(packageName, className, terminalDeclarations, nonterminalDeclarations, precedenceTable, startNonterminalName, productions);
		GrammarInfo grammarInfo = new GrammarInfo(new GrammarCanonicalizer(grammar).getResult());
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();

		MapagParserClassGenerator mapagParserClassGenerator = new MapagParserClassGenerator(grammarInfo, stateMachine);
		mapagParserClassGenerator.generate();
	}

}

/*

%precedence {
    %left BAR;
    %nonassoc QUESTION_MARK, ASTERISK, PLUS;
}

%start grammar;


//
// grammar
//

grammar ::=
	KW_PACKAGE (IDENTIFIER DOT)* IDENTIFIER SEMICOLON
	KW_CLASS IDENTIFIER:className SEMICOLON
	KW_TERMINALS OPENING_CURLY_BRACE nonemptyIdentifierList CLOSING_CURLY_BRACE
	KW_NONTERMINALS OPENING_CURLY_BRACE nonemptyIdentifierList CLOSING_CURLY_BRACE
	(KW_PRECEDENCE OPENING_CURLY_BRACE precedenceDeclaration* CLOSING_CURLY_BRACE)?
	(KW_START IDENTIFIER:startSymbolName SEMICOLON)?
	production+
	;

precedenceDeclaration ::=
    (KW_LEFT | KW_RIGHT | KW_NONASSOC) nonemptyIdentifierList SEMICOLON
    ;


//
// productions
//

production ::=
	IDENTIFIER COLON_COLON_EQUALS expression SEMICOLON
	| error SEMICOLON
	;

expression ::=
    (
    	IDENTIFIER (COLON IDENTIFIER)?
	    | expression BAR expression
	    | expression ASTERISK
	    | expression PLUS
	    | expression QUESTION_MARK
	    | OPENING_PARENTHESIS expression CLOSING_PARENTHESIS (COLON IDENTIFIER)?
	)+
	;


//
// helpers
//

nonemptyIdentifierList ::=
	IDENTIFIER
	| nonemptyIdentifierList COMMA IDENTIFIER
	;


 */