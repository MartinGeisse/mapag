package name.martingeisse.mapag.ide;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import name.martingeisse.mapag.input.FlexGeneratedMapagLexer;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

	public MapagSyntaxHighlighterFactory() {
		System.out.println();
	}

	@Override
	@NotNull
	public SyntaxHighlighter getSyntaxHighlighter(final Project project, final VirtualFile virtualFile) {
		return new SyntaxHighlighterBase() {

			@NotNull
			@Override
			public Lexer getHighlightingLexer() {
				return new FlexAdapter(new FlexGeneratedMapagLexer(null));
			}

			@NotNull
			@Override
			public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
				if (tokenType instanceof MapagSpecificationToken) {
					if (tokenType == MapagSpecificationElementTypes.IDENTIFIER) {
						return MapagTextAttributeKeys.IDENTIFIER;
					} else if (tokenType == MapagSpecificationElementTypes.PACKAGE) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.CLASS) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.LEFT) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.RIGHT) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.NONASSOC) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.TERMINAL) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.NONTERMINAL) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.PRECEDENCE) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.PERCENT_PREC) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.START) {
						return MapagTextAttributeKeys.KEYWORD;
					} else if (tokenType == MapagSpecificationElementTypes.DOT) {
						return MapagTextAttributeKeys.DOT;
					} else if (tokenType == MapagSpecificationElementTypes.COMMA) {
						return MapagTextAttributeKeys.COMMA;
					} else if (tokenType == MapagSpecificationElementTypes.QUESTION_MARK) {
						return MapagTextAttributeKeys.QUESTION_MARK;
					} else if (tokenType == MapagSpecificationElementTypes.ASTERISK) {
						return MapagTextAttributeKeys.ASTERISK;
					} else if (tokenType == MapagSpecificationElementTypes.PLUS) {
						return MapagTextAttributeKeys.PLUS;
					} else if (tokenType == MapagSpecificationElementTypes.BAR) {
						return MapagTextAttributeKeys.BAR;
					} else if (tokenType == MapagSpecificationElementTypes.COLON) {
						return MapagTextAttributeKeys.COLON;
					} else if (tokenType == MapagSpecificationElementTypes.COLON_COLON_EQUALS) {
						return MapagTextAttributeKeys.COLON_COLON_EQUALS;
					} else if (tokenType == MapagSpecificationElementTypes.SEMICOLON) {
						return MapagTextAttributeKeys.SEMICOLON;
					} else if (tokenType == TokenType.BAD_CHARACTER) {
						return MapagTextAttributeKeys.BAD_CHARACTER;
					}
				}
				return EMPTY;
				// TODO comments
			}

		};
	}

}
