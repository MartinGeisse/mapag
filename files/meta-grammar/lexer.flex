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
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" {CommentContent} \*+ "/"
EndOfLineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

// identifiers
ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*

%%

<YYINITIAL> {
	{Whitespace} { return TokenType.WHITE_SPACE; }
	"?" { return MapagSpecificationElementTypes.QUESTION_MARK; }
	";" { return MapagSpecificationElementTypes.SEMICOLON; }
	"," { return MapagSpecificationElementTypes.COMMA; }
	"*" { return MapagSpecificationElementTypes.ASTERISK; }
	"." { return MapagSpecificationElementTypes.DOT; }
	"|" { return MapagSpecificationElementTypes.BAR; }
	"+" { return MapagSpecificationElementTypes.PLUS; }
	":" { return MapagSpecificationElementTypes.COLON; }
	"::=" { return MapagSpecificationElementTypes.COLON_COLON_EQUALS; }
	"%prec" { return MapagSpecificationElementTypes.PERCENT_PREC; }
	{Comment} { return MapagSpecificationElementTypes.COMMENT; }
	"package" { return MapagSpecificationElementTypes.PACKAGE; }
	"class" { return MapagSpecificationElementTypes.CLASS; }
	"terminal" { return MapagSpecificationElementTypes.TERMINAL; }
	"nonterminal" { return MapagSpecificationElementTypes.NONTERMINAL; }
	"start" { return MapagSpecificationElementTypes.START; }
	"precedence" { return MapagSpecificationElementTypes.PRECEDENCE; }
	"left" { return MapagSpecificationElementTypes.LEFT; }
	"right" { return MapagSpecificationElementTypes.RIGHT; }
	"nonassoc" { return MapagSpecificationElementTypes.NONASSOC; }
	{ident} { return MapagSpecificationElementTypes.IDENTIFIER; }
}

// error fallback
[^] { return TokenType.BAD_CHARACTER; }
