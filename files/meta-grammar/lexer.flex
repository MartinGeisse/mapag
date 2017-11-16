package name.martingeisse.mapag.input;

import java.lang.Error;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

%%

%class FlexGeneratedMapagLexer
%implements FlexLexer
%public
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

// whitespace
Newline = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}

// comments
BlockComment = "/*" {CommentContent} \*+ "/"
LineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

// identifiers
Identifier = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*

%%

<YYINITIAL> {

	// whitespace
	{Whitespace} { return TokenType.WHITE_SPACE; }

	// keywords
	"%terminals" { return Symbols.KW_TERMINALS; }
	"%nonterminals" { return Symbols.KW_NONTERMINALS; }
	"%precedence" { return Symbols.KW_PRECEDENCE; }
	"%left" { return Symbols.KW_LEFT; }
	"%right" { return Symbols.KW_RIGHT; }
	"%nonassoc" { return Symbols.KW_NONASSOC; }
	"%start" { return Symbols.KW_START; }

	// braces and parentheses
	"{" { return Symbols.OPENING_CURLY_BRACE; }
	"}" { return Symbols.CLOSING_CURLY_BRACE; }
	"(" { return Symbols.OPENING_PARENTHESIS; }
	")" { return Symbols.CLOSING_PARENTHESIS; }

	// punctuation and operators
	"," { return Symbols.COMMA; }
	";" { return Symbols.SEMICOLON; }
	":" { return Symbols.COLON; }
	"::=" { return Symbols.EXPANDS_TO; }
	"?" { return Symbols.QUESTION_MARK; }
	"*" { return Symbols.ASTERISK; }
	"+" { return Symbols.PLUS; }
	"|" { return Symbols.BAR; }

	// comments
	{BlockComment} { return Symbols.BLOCK_COMMENT; }
	{LineComment} { return Symbols.LINE_COMMENT; }

	// identifiers
	{Identifier} { return Symbols.IDENTIFIER; }

}

// error fallback
[^] { return TokenType.BAD_CHARACTER; }
