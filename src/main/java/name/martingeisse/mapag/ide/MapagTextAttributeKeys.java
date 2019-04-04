package name.martingeisse.mapag.ide;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagTextAttributeKeys {

	public static final TextAttributesKey[] KEYWORD = single("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey[] IDENTIFIER = single("IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey[] BAD_CHARACTER = new TextAttributesKey[]{HighlighterColors.BAD_CHARACTER};
	public static final TextAttributesKey[] BLOCK_COMMENT = single("BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	public static final TextAttributesKey[] LINE_COMMENT = single("LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

	public static final TextAttributesKey[] PARENTHESES = single("PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
	public static final TextAttributesKey[] COMMA = single("COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey[] SEMICOLON = single("SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
	public static final TextAttributesKey[] COLON = single("COLON", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey[] EXPANDS_TO = single("EXPANDS_TO", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey[] QUESTION_MARK = single("QUESTION_MARK", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey[] ASTERISK = single("ASTERISK", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey[] PLUS = single("PLUS", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey[] BAR = single("BAR", DefaultLanguageHighlighterColors.OPERATION_SIGN);

	private static TextAttributesKey[] single(@NonNls @NotNull String externalName, TextAttributesKey fallbackAttributeKey) {
		return new TextAttributesKey[]{
			TextAttributesKey.createTextAttributesKey(externalName, fallbackAttributeKey)
		};
	}
}
