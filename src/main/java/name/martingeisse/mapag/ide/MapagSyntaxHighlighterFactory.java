package name.martingeisse.mapag.ide;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import name.martingeisse.mapag.input.MapagElementType;
import name.martingeisse.mapag.input.MapagLexer;
import name.martingeisse.mapag.input.Symbols;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

	@Override
	@NotNull
	public SyntaxHighlighter getSyntaxHighlighter(final Project project, final VirtualFile virtualFile) {
		return new SyntaxHighlighterBase() {

			@NotNull
			@Override
			public Lexer getHighlightingLexer() {
				return new MapagLexer();
			}

			@NotNull
			@Override
			public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
				if (tokenType instanceof MapagElementType) {
					if (tokenType == Symbols.IDENTIFIER) {
						return MapagTextAttributeKeys.IDENTIFIER;
					} else if (tokenType == Symbols.KW_TERMINALS) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_PRECEDENCE) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_LEFT) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_RIGHT) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_NONASSOC) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_START) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_RESOLVE) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_SHIFT) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_REDUCE) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_EOF) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_ERROR) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_REDUCE_ON_ERROR) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.KW_EMPTY) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == Symbols.OPENING_CURLY_BRACE) {
						return MapagTextAttributeKeys.PARENTHESES;
					} else if (tokenType == Symbols.CLOSING_CURLY_BRACE) {
						return MapagTextAttributeKeys.PARENTHESES;
					} else if (tokenType == Symbols.OPENING_PARENTHESIS) {
						return MapagTextAttributeKeys.PARENTHESES;
					} else if (tokenType == Symbols.CLOSING_PARENTHESIS) {
						return MapagTextAttributeKeys.PARENTHESES;
					} else if (tokenType == Symbols.COMMA) {
						return MapagTextAttributeKeys.COMMA;
					} else if (tokenType == Symbols.SEMICOLON) {
						return MapagTextAttributeKeys.SEMICOLON;
					} else if (tokenType == Symbols.COLON) {
						return MapagTextAttributeKeys.COLON;
					} else if (tokenType == Symbols.EXPANDS_TO) {
						return MapagTextAttributeKeys.EXPANDS_TO;
					} else if (tokenType == Symbols.QUESTION_MARK) {
						return MapagTextAttributeKeys.QUESTION_MARK;
					} else if (tokenType == Symbols.ASTERISK) {
						return MapagTextAttributeKeys.ASTERISK;
					} else if (tokenType == Symbols.PLUS) {
						return MapagTextAttributeKeys.PLUS;
					} else if (tokenType == Symbols.BAR) {
						return MapagTextAttributeKeys.BAR;
					} else if (tokenType == Symbols.BLOCK_COMMENT) {
						return MapagTextAttributeKeys.BLOCK_COMMENT;
					} else if (tokenType == Symbols.LINE_COMMENT) {
						return MapagTextAttributeKeys.LINE_COMMENT;
					} else if (tokenType == TokenType.BAD_CHARACTER) {
						return MapagTextAttributeKeys.BAD_CHARACTER;
					}
				}
				return EMPTY;
			}

		};
	}

}
