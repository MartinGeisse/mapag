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

// Comments. Note: do NOT make the newline a part of the LineComment -- it will confuse the auto-formatter. The
// "longest match" algorithm will eat everything before the newline even without specifying it explicitly.
//
// Also, the IDE commenter wants an unterminated block comment to formally be a comment too. We use a rule that
// matches a block comment without the terminating characters. If the comment is properly terminated, the normal
// block comment rule will match a longer substring and take precedence.
UnterminatedBlockComment = "/*" {CommentContent} \**
TerminatedBlockComment = "/*" {CommentContent} \*+ "/"
BlockComment = {UnterminatedBlockComment} | {TerminatedBlockComment}
LineComment = "//" [^\r\n]*
CommentContent = ( [^*] | \*+[^*/] )*

// identifiers
Identifier = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*

%%

<YYINITIAL> {

	// whitespace
	{Whitespace} { return TokenType.WHITE_SPACE; }

	// keywords
	"%terminals" { return Symbols.KW_TERMINALS; }
	"%precedence" { return Symbols.KW_PRECEDENCE; }
	"%left" { return Symbols.KW_LEFT; }
	"%right" { return Symbols.KW_RIGHT; }
	"%nonassoc" { return Symbols.KW_NONASSOC; }
	"%start" { return Symbols.KW_START; }
	"%resolve" { return Symbols.KW_RESOLVE; }
	"%shift" { return Symbols.KW_SHIFT; }
	"%reduce" { return Symbols.KW_REDUCE; }
	"%eof" { return Symbols.KW_EOF; }
	"%error" { return Symbols.KW_ERROR; }
	"%reduceOnError" { return Symbols.KW_REDUCE_ON_ERROR; }
	"%empty" { return Symbols.KW_EMPTY; }

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
