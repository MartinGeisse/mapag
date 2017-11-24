package name.martingeisse.mapag.input;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapagGeneratedMapagParser implements PsiParser, LightPsiParser {

	// ------------------------------------------------------------------------------------------------
	// --- generated stuff
	// ------------------------------------------------------------------------------------------------

	// symbols (tokens and nonterminals)
	private static final int EOF_SYMBOL_CODE = 0;
	private static final int ERROR_SYMBOL_CODE = 1;
	private static final IElementType[] SYMBOL_CODE_TO_ELEMENT_TYPE = {
    	null, // %eof -- doesn't have an IElementType
    	null, // %error -- doesn't have an IElementType
                    Symbols.ASTERISK,
                    Symbols.BAR,
                    Symbols.BLOCK_COMMENT,
                    Symbols.CLOSING_CURLY_BRACE,
                    Symbols.CLOSING_PARENTHESIS,
                    Symbols.COLON,
                    Symbols.COMMA,
                    Symbols.EXPANDS_TO,
                    Symbols.IDENTIFIER,
                    Symbols.KW_EOF,
                    Symbols.KW_ERROR,
                    Symbols.KW_LEFT,
                    Symbols.KW_NONASSOC,
                    Symbols.KW_NONTERMINALS,
                    Symbols.KW_PRECEDENCE,
                    Symbols.KW_REDUCE,
                    Symbols.KW_RESOLVE,
                    Symbols.KW_RIGHT,
                    Symbols.KW_SHIFT,
                    Symbols.KW_START,
                    Symbols.KW_TERMINALS,
                    Symbols.LINE_COMMENT,
                    Symbols.OPENING_CURLY_BRACE,
                    Symbols.OPENING_PARENTHESIS,
                    Symbols.PLUS,
                    Symbols.QUESTION_MARK,
                    Symbols.SEMICOLON,
        	};

	// state machine (general)
	private static final int START_STATE = 0;

	// state machine (action table)
	private static final int[] ACTION_TABLE = {
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  35,  0,  0,  0,  0,  0,  0,  0,  2,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -2147483648,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  18,  0,  21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  0,  0,  0,  5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  18,  0,  21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  0,  0,  0,  6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  29,  9,  0,  0,  23,  25,  0,  0,  18,  0,  21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  31,  33,  0,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  29,  9,  0,  0,  24,  25,  0,  0,  18,  0,  21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  31,  33,  0,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  29,  -8,  0,  0,  -8,  25,  0,  0,  18,  0,  21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  31,  33,  0,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  29,  -7,  0,  0,  -7,  25,  0,  0,  -7,  0,  -7,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -7,  31,  33,  0,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  18,  0,  21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  0,  0,  0,  7,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  30,  -8,  0,  0,  0,  26,  0,  0,  19,  0,  22,  0,  0,  0,  -8,  0,  -8,  0,  0,  0,  0,  0,  0,  4,  32,  34,  -8,  11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  30,  -7,  0,  0,  0,  26,  0,  0,  -7,  0,  -7,  0,  0,  0,  -7,  0,  -7,  0,  0,  0,  0,  0,  0,  -7,  32,  34,  -7,  11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  30,  13,  0,  0,  0,  26,  0,  0,  19,  0,  22,  0,  0,  0,  102,  0,  104,  0,  0,  0,  0,  0,  0,  4,  32,  34,  -2,  11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  19,  0,  22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  4,  0,  0,  0,  10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  19,  0,  22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  4,  0,  0,  0,  12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  77,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  19,  0,  22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  79,  4,  0,  0,  0,  12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  81,  0, 
                     0,  0,  0,  0,  0,  80,  0,  0,  0,  0,  20,  0,  22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  4,  0,  0,  0,  12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  89,  83,  84,  0,  0,  0,  0,  0,  0,  87,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  19,  0,  22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  4,  0,  0,  0,  12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  85,  0, 
                     0,  0,  -5,  -5,  0,  0,  -5,  -5,  0,  0,  -5,  0,  -5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -5,  -5,  -5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -5,  -5,  0,  0,  0,  -5,  0,  0,  -5,  0,  -5,  0,  0,  0,  -5,  0,  -5,  0,  0,  0,  0,  0,  0,  -5,  -5,  -5,  -5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -5,  -5,  0,  0,  0,  -5,  0,  17,  -5,  0,  -5,  0,  0,  0,  -5,  0,  -5,  0,  0,  0,  0,  0,  0,  -5,  -5,  -5,  -5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -6,  -6,  0,  0,  -6,  -6,  0,  0,  -6,  0,  -6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -6,  -6,  -6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -6,  -6,  0,  0,  0,  -6,  0,  0,  -6,  0,  -6,  0,  0,  0,  -6,  0,  -6,  0,  0,  0,  0,  0,  0,  -6,  -6,  -6,  -6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -12,  -12,  0,  0,  -12,  -12,  0,  0,  -12,  0,  -12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -12,  -12,  -12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -12,  -12,  0,  0,  0,  -12,  0,  0,  -12,  0,  -12,  0,  0,  0,  -12,  0,  -12,  0,  0,  0,  0,  0,  0,  -12,  -12,  -12,  -12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  27,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  28,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -13,  -13,  0,  0,  -13,  -13,  0,  0,  -13,  0,  -13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -13,  -13,  -13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -13,  -13,  0,  0,  0,  -13,  0,  0,  -13,  0,  -13,  0,  0,  0,  -13,  0,  -13,  0,  0,  0,  0,  0,  0,  -13,  -13,  -13,  -13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -9,  -9,  0,  0,  -9,  -9,  0,  0,  -9,  0,  -9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -9,  -9,  -9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -9,  -9,  0,  0,  0,  -9,  0,  0,  -9,  0,  -9,  0,  0,  0,  -9,  0,  -9,  0,  0,  0,  0,  0,  0,  -9,  -9,  -9,  -9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -10,  -10,  0,  0,  -10,  -10,  0,  0,  -10,  0,  -10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -10,  -10,  -10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -10,  -10,  0,  0,  0,  -10,  0,  0,  -10,  0,  -10,  0,  0,  0,  -10,  0,  -10,  0,  0,  0,  0,  0,  0,  -10,  -10,  -10,  -10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -11,  -11,  0,  0,  -11,  -11,  0,  0,  -11,  0,  -11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -11,  -11,  -11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  -11,  -11,  0,  0,  0,  -11,  0,  0,  -11,  0,  -11,  0,  0,  0,  -11,  0,  -11,  0,  0,  0,  0,  0,  0,  -11,  -11,  -11,  -11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  36,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  57,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  37,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  38,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  39,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  40,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  57,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  41,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  42,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  48,  0,  0,  0,  0,  -45,  0,  0,  0,  0,  0,  0,  0,  0,  0,  53,  0,  43,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  44,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  45,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  46,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  72,  0,  0,  0,  0,  0,  0,  0,  0,  74,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  47,  0,  0,  0,  0,  0,  55,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -31,  72,  0,  0,  0,  0,  0,  0,  0,  0,  74,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  54,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  49,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -33,  0,  0,  0,  0,  0,  0,  0,  -33,  -33,  0,  0,  0,  0,  -33,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  50,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  51,  0,  0,  0,  0,  0,  0,  0,  69,  70,  0,  0,  0,  0,  71,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  52,  56,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -30,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -34,  0,  0,  0,  0,  0,  0,  0,  -34,  -34,  0,  0,  0,  0,  -34,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -46,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -43,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -43,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -42,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -42,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  58,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  67,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -47,  0,  0,  -47,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  59,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -47,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -47,  0,  0,  0,  0,  0,  0,  0,  0,  60,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -36,  0,  0,  61,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  65,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  62,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -36,  0,  0,  0,  0,  0,  0,  0,  66,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  63,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  64,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -32,  0,  0,  -32,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -32,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -32,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -48,  0,  0,  -48,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -48,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -48,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  68,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -29,  0,  0,  0,  0,  0,  0,  0,  -29,  -29,  0,  0,  0,  0,  -29,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -37,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -39,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -38,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  73,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -19,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -19,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  75,  0,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  76,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  78,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -17,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -17,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -20,  0,  0,  0,  0,  -20,  0,  -20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  16,  0,  0,  0,  0,  0,  0,  0, 
                     -18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  82,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     -16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -26,  0,  0,  0,  0,  -26,  0,  -26,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -26,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -25,  0,  0,  0,  0,  -25,  0,  -25,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -25,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  86,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -1,  0,  0,  0,  0,  -1,  0,  -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  88,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -35,  0,  0,  0,  0,  -35,  0,  -35,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -35,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -21,  0,  0,  0,  0,  -21,  0,  -21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -21,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  106,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  97,  0,  0,  98,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  107,  0,  91,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100,  101,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  92,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  93,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  95,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  94,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  99,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -44,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -44,  0,  0,  -44,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  100,  101,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  96,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -22,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -41,  -41,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -40,  -40,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -23,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -23,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  -24,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -24,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  103,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -3,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  105,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -27,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -27,  0,  0,  -27,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  90, 
                     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -4,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
                     0,  0,  0,  0,  0,  -28,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  -28,  0,  0,  -28,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
        	};
	private static final int ACTION_TABLE_WIDTH = 52;

	// state machine (alternatives / reduction table)
	private static final int[] ALTERNATIVE_INDEX_TO_RIGHT_HAND_SIDE_LENGTH = {
                    4,
                    1,
                    3,
                    5,
                    1,
                    1,
                    2,
                    3,
                    2,
                    2,
                    2,
                    3,
                    3,
                    0,
                    2,
                    4,
                    6,
                    5,
                    2,
                    0,
                    2,
                    2,
                    1,
                    1,
                    1,
                    1,
                    0,
                    2,
                    3,
                    4,
                    13,
                    2,
                    0,
                    2,
                    2,
                    2,
                    1,
                    1,
                    1,
                    1,
                    1,
                    1,
                    2,
                    4,
                    0,
                    1,
                    0,
                    2,
        	};
	private static final IElementType[] ALTERNATIVE_INDEX_TO_NONTERMINAL_ELEMENT_TYPE = {
                    Symbols.production_Multi_1_Named_Named,
                    Symbols.rightHandSide_WithoutResolver,
                    Symbols.rightHandSide_WithPrecedenceResolver,
                    Symbols.rightHandSide_WithExplicitResolver,
                    Symbols.expression_Identifier,
                    Symbols.expression_Error,
                    Symbols.expression_Sequence,
                    Symbols.expression_Or,
                    Symbols.expression_ZeroOrMore,
                    Symbols.expression_OneOrMore,
                    Symbols.expression_Optional,
                    Symbols.expression_Parenthesized,
                    Symbols.expression_Named,
                    Symbols.resolveDeclaration_AdditionalSymbols_Start,
                    Symbols.resolveDeclaration_AdditionalSymbols_Next,
                    Symbols.production_SingleUnnamed,
                    Symbols.production_SingleNamed,
                    Symbols.production_Multi,
                    Symbols.production_Error,
                    Symbols.production_Multi_Alternatives_Start,
                    Symbols.production_Multi_Alternatives_Next,
                    Symbols.resolveDeclaration_1,
                    Symbols.resolveDeclarationSymbol_Identifier,
                    Symbols.resolveDeclarationSymbol_Eof,
                    Symbols.production_Multi_1_Unnamed,
                    Symbols.production_Multi_1_Named,
                    Symbols.rightHandSide_WithExplicitResolver_ResolveDeclarations_Start,
                    Symbols.rightHandSide_WithExplicitResolver_ResolveDeclarations_Next,
                    Symbols.precedenceDeclaration,
                    Symbols.grammar_1,
                    Symbols.grammar,
                    Symbols.nonemptyIdentifierList_1,
                    Symbols.grammar_1_PrecedenceDeclarations_Start,
                    Symbols.grammar_1_PrecedenceDeclarations_Next,
                    Symbols.production_Multi_1_Unnamed_Unnamed,
                    Symbols.nonemptyIdentifierList,
                    Symbols.precedenceDeclaration_Associativity_Left,
                    Symbols.precedenceDeclaration_Associativity_Right,
                    Symbols.precedenceDeclaration_Associativity_Nonassoc,
                    Symbols.resolveDeclaration_Action_Shift,
                    Symbols.resolveDeclaration_Action_Reduce,
                    Symbols.grammar_Productions_Start,
                    Symbols.grammar_Productions_Next,
                    Symbols.resolveDeclaration,
                    Symbols.grammar_PrecedenceTable_Absent,
                    Symbols.grammar_PrecedenceTable_Present,
                    Symbols.nonemptyIdentifierList_MoreIdentifiers_Start,
                    Symbols.nonemptyIdentifierList_MoreIdentifiers_Next,
        	};
	private static final int[] ALTERNATIVE_INDEX_TO_NONTERMINAL_SYMBOL_CODE = {
                    42,
                    50,
                    50,
                    50,
                    29,
                    29,
                    29,
                    29,
                    29,
                    29,
                    29,
                    29,
                    29,
                    48,
                    48,
                    40,
                    40,
                    40,
                    40,
                    44,
                    44,
                    46,
                    49,
                    49,
                    41,
                    41,
                    51,
                    51,
                    38,
                    31,
                    30,
                    36,
                    32,
                    32,
                    43,
                    35,
                    39,
                    39,
                    39,
                    47,
                    47,
                    34,
                    34,
                    45,
                    33,
                    33,
                    37,
                    37,
        	};

	// state machine (error messages)
	private static final String[] STATE_INPUT_EXPECTATION = {
                    "%terminals",
                    "",
                    "expression",
                    "expression",
                    "expression ) * + : ? |",
                    "expression ) * + : ? |",
                    "expression %error ( ) * + : ? identifier |",
                    "expression %error ( ) * + : ? identifier |",
                    "expression",
                    "expression %error %precedence %resolve ( * + : ; ? identifier |",
                    "expression %error %precedence %resolve ( * + : ; ? identifier |",
                    "expression %precedence %resolve * + : ; ? |",
                    "expression",
                    "expression",
                    "expression {",
                    "expression identifier }",
                    "expression",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "%error %precedence %resolve ( * + : ::= ; ? identifier |",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "identifier",
                    "identifier",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "%error ( ) * + : ? identifier |",
                    "%error %precedence %resolve ( * + : ; ? identifier |",
                    "{",
                    "identifier(s)",
                    "}",
                    "%nonterminals",
                    "{",
                    "identifier(s)",
                    "}",
                    "%precedence %start",
                    "%start",
                    "identifier",
                    ";",
                    "production",
                    "production",
                    "{",
                    "precedence-declaration %left %nonassoc %right }",
                    "precedence-declaration %left %nonassoc %right }",
                    "%start",
                    "%left %nonassoc %right }",
                    "%start",
                    "identifier",
                    "identifier",
                    "identifier(s)",
                    ", }",
                    ", ;",
                    ", }",
                    ", ;",
                    "identifier",
                    "identifier",
                    ", }",
                    ", ;",
                    ", }",
                    ", ;",
                    ";",
                    "%left %nonassoc %right }",
                    "identifier",
                    "identifier",
                    "identifier",
                    ";",
                    "identifier",
                    ": ::=",
                    "identifier",
                    "::=",
                    ";",
                    "identifier",
                    "%error ( identifier }",
                    "identifier",
                    ";",
                    "identifier",
                    "%error ( identifier }",
                    "%error ( identifier }",
                    ";",
                    "%error ( identifier }",
                    ";",
                    "%error ( identifier }",
                    "%error ( identifier }",
                    "resolve-declaration %reduce %shift }",
                    "%eof identifier",
                    ", ;",
                    ", ;",
                    "%reduce %shift }",
                    "%eof identifier",
                    ", ;",
                    "%eof identifier",
                    "%eof identifier",
                    ", ;",
                    ", ;",
                    ", ;",
                    "identifier",
                    ";",
                    "{",
                    "resolve-declaration %reduce %shift }",
                    ";",
                    "%reduce %shift }",
            };

	// other
	private static final IElementType FILE_ELEMENT_TYPE = name.martingeisse.mapag.ide.MapagParserDefinition.FILE_ELEMENT_TYPE;
	private static final int RECOVERY_SYNC_LENGTH = 3;

	// ------------------------------------------------------------------------------------------------
	// --- non-generated stuff (initialization and static stuff)
	// ------------------------------------------------------------------------------------------------

	// static table, but has to be initialized at startup since element type indices aren't compile-time constants
	private static int[] elementTypeIndexToSymbolCode;

	/**
	 * This method initializes static tables on the first parse run -- we need element type
	 * indices to be initialized before doing this.
	 */
	private static void initializeStatic() {
		if (elementTypeIndexToSymbolCode != null) {
			return;
		}
		int maxElementTypeIndex = 0;
		for (IElementType token : SYMBOL_CODE_TO_ELEMENT_TYPE) {
		    if (token != null) {
                if (maxElementTypeIndex < token.getIndex()) {
                    maxElementTypeIndex = token.getIndex();
                }
		    }
		}
		elementTypeIndexToSymbolCode = new int[maxElementTypeIndex + 1];
		Arrays.fill(elementTypeIndexToSymbolCode, -1);
		for (int symbolCode = 0; symbolCode < SYMBOL_CODE_TO_ELEMENT_TYPE.length; symbolCode++) {
			IElementType token = SYMBOL_CODE_TO_ELEMENT_TYPE[symbolCode];
			if (token != null) {
    			elementTypeIndexToSymbolCode[token.getIndex()] = symbolCode;
			}
		}
	}

	private static int getSymbolCodeForElementType(IElementType elementType) {
		int index = elementType.getIndex();
		if (index >= 0 && index < elementTypeIndexToSymbolCode.length) {
			int symbolCode = elementTypeIndexToSymbolCode[index];
			if (symbolCode >= 0) {
				return symbolCode;
			}
		}
		throw new RuntimeException("unknown token: " + elementType);
	}

	// ------------------------------------------------------------------------------------------------
	// --- non-generated stuff
	// ------------------------------------------------------------------------------------------------

	private boolean used = false;
	private int[] stateStack = new int[256];
	private Object[] parseTreeStack = new Object[256];
	private int stackSize = 0;
	private int state = START_STATE;

	@Override
	public ASTNode parse(IElementType type, PsiBuilder psiBuilder) {
		parseLight(type, psiBuilder);
		return psiBuilder.getTreeBuilt();
	}

	@Override
	public void parseLight(IElementType type, PsiBuilder builder) {
		if (used) {
			throw new IllegalStateException("cannot re-use this parser object");
		}
		used = true;
		if (type != FILE_ELEMENT_TYPE) {
			throw new IllegalArgumentException("unsupported top-level element type to parse: " + type);
		}
		parse(builder);
	}

	private void parse(PsiBuilder psiBuilder) {

		// initialize static parser information
		initializeStatic();

        // handle unrecoverable syntax errors
        PsiBuilder.Marker wholeFileMarker = psiBuilder.mark();
        PsiBuilder.Marker preParseMarker = psiBuilder.mark();
        try {

            // Parse the input using the generated machine to build a parse tree. The state machine cannot execute the
            // accept action here since the input cannot contain EOF.
            while (!psiBuilder.eof()) {
           		if (consumeSymbol(getSymbolCodeForElementType(psiBuilder.getTokenType()), null)) {
                	psiBuilder.advanceLexer();
           		} else {
           			recoverFromError(psiBuilder);
           		}
            }

            // Consume the EOF token. This should (possibly after some reductions) accept the input. If not, this causes
            // a syntax error (unexpected EOF), since the parser generator wouldn't emit a "shift EOF" action.
            {
                int originalState = state;
                if (!consumeSymbol(EOF_SYMBOL_CODE, null)) {
                    recoverFromError(psiBuilder);
                    if (!consumeSymbol(EOF_SYMBOL_CODE, null)) {
                        throw new UnrecoverableSyntaxException(state);
                    }
                }
            }

        } catch (UnrecoverableSyntaxException e) {

            // Build a "code fragment" node that contains the parsed and partially reduced part (i.e. the parse tree
            // stack), then the exception. This will report the error properly and also consume the remaining tokens.
            List<Object> nodeBuilder = new ArrayList<>();
            nodeBuilder.add(Symbols.__PARSED_FRAGMENT);
            for (int i=0; i<stackSize; i++) {
                nodeBuilder.add(parseTreeStack[i]);
            }
            nodeBuilder.add(e);
            parseTreeStack[0] = nodeBuilder.toArray();
            stackSize = 1;

        }
        preParseMarker.rollbackTo();

		// At this point, the state stack should contain single element (the start state) and the associated parse
		// tree stack contains the root node as its single element. If anything in the input tried to prevent that,
		// consuming the EOF token would have failed. Now we re-parse, based on the parse tree we build, in a way
		// that the PsiBuilder likes.
		if (stackSize != 1) {
			// either the Lexer returned an explicit EOF (which it shouldn't) or this is a bug in the parser generator
			throw new RuntimeException("unexpected stack size after accepting start symbol");
		}
		feedPsiBuilder(psiBuilder, parseTreeStack[0]);

		// Before we consider the file parsed, we must advance the lexer once more so it consumes end-of-file
		// whitespace and comments as part of the root AST node.
		psiBuilder.advanceLexer();
		wholeFileMarker.done(FILE_ELEMENT_TYPE);

	}

	/**
	 * Consumes a symbol (token, nonterminal or EOF). This performs one or several actions until the symbol gets shifted
	 * (or, in the case of EOF, accepted).
	 *
	 * Returns true on success, false on syntax error. This method does not handle syntax errors itself.
	 */
	private boolean consumeSymbol(int symbolCode, Object symbolData) throws UnrecoverableSyntaxException {
		while (true) { // looped on reduce
			int action = ACTION_TABLE[state * ACTION_TABLE_WIDTH + symbolCode];
			if (action == Integer.MIN_VALUE) { // accept
				return true;
			} else if (action > 0) { // shift
				shift(symbolData, action - 1);
				return true;
			} else if (action < 0) { // reduce, then continue with the original symbol
				reduce(-action - 1);
			} else { // syntax error
				return false;
			}
		}
	}

	private void shift(Object data, int newState) {
		if (stackSize == stateStack.length) {
			stackSize = stackSize * 2;
			stateStack = Arrays.copyOf(stateStack, stackSize);
			parseTreeStack = Arrays.copyOf(parseTreeStack, stackSize);
		}
		stateStack[stackSize] = state;
		parseTreeStack[stackSize] = data;
		stackSize++;
		state = newState;
	}

	private void reduce(int alternativeIndex) throws UnrecoverableSyntaxException {

		// determine the reduction (nonterminal + alternative) to reduce
		int rightHandSideLength = ALTERNATIVE_INDEX_TO_RIGHT_HAND_SIDE_LENGTH[alternativeIndex];
		IElementType nonterminalElementType = ALTERNATIVE_INDEX_TO_NONTERMINAL_ELEMENT_TYPE[alternativeIndex];
		int nonterminalSymbolCode = ALTERNATIVE_INDEX_TO_NONTERMINAL_SYMBOL_CODE[alternativeIndex];

		// pop (rightHandSideLength) states off the state stack
		if (rightHandSideLength > 0) {
			stackSize -= rightHandSideLength;
			state = stateStack[stackSize];
		}

		// build a parse tree node from the nonterminal element type and the subtrees in the parse tree stack
		Object[] reduction = new Object[rightHandSideLength + 1];
		reduction[0] = nonterminalElementType;
		System.arraycopy(parseTreeStack, stackSize, reduction, 1, rightHandSideLength);

		// shift the nonterminal (errors cannot occur here in LR(1) parsing)
		if (!consumeSymbol(nonterminalSymbolCode, reduction)) {
			throw new RuntimeException("syntax error while shifting a nonterminal... WTF?");
		}

	}

	private void feedPsiBuilder(PsiBuilder builder, Object what) {
	    if (what == null) {
            builder.advanceLexer();
	    } else if (what instanceof Object[]) {
			Object[] reduction = (Object[]) what;
			PsiBuilder.Marker marker = builder.mark();
			for (int i = 1; i < reduction.length; i++) {
				feedPsiBuilder(builder, reduction[i]);
			}
			marker.done((IElementType) reduction[0]);
		} else if (what instanceof List<?>) {
			// an object list has the same meaning as an object array (needed for error symbols)
			feedPsiBuilder(builder, ((List<?>)what).toArray());
		} else if (what instanceof ErrorLocationIndicator) {
		    ErrorLocationIndicator errorLocationIndicator = (ErrorLocationIndicator)what;
			builder.error(STATE_INPUT_EXPECTATION[errorLocationIndicator.state]);
		} else if (what instanceof UnrecoverableSyntaxException) {
            builder.error(((UnrecoverableSyntaxException)what).getMessage());
            while (!builder.eof()) {
            	builder.advanceLexer();
            }
		}
	}

	private void recoverFromError(PsiBuilder psiBuilder) throws UnrecoverableSyntaxException {

		// Attempt error recovery. For now, this parser uses the same logic as Java CUP: find the first state from the
		// stack that can shift an error symbol, then throw away input terminals until parsing succeeds for
		// RECOVERY_SYNC_LENGTH terminals. This is okay-ish but it will never find recovery-capable states deeper in
		// the stack. For example, in a C-like language, if we allow a statement to consist of an error symbol, but
		// also allow a function to consist of an error symbol, then a syntax error in a statement will never try to
		// reduce the whole broken function to %error -- it will always insist on reducing only the broken statement
		// to %error.
		//
		// Note the edge cases: Both the current state (at the current stack size) and the start state (with an empty
		// stack) could be able to consume the error symbol.

		int originalState = state; // used for error messages

		// dig up a recovery-capable state from the stack
		int originalStackSize = stackSize;
		while (ACTION_TABLE[state * ACTION_TABLE_WIDTH + ERROR_SYMBOL_CODE] == 0) {
			stackSize--;
			if (stackSize < 0) {
				// we didn't even find a recovery-capable state
				stackSize = originalStackSize;
				throw new UnrecoverableSyntaxException(originalState);
			}
			state = stateStack[stackSize];
		}

		// all symbols (terminals and nonterminals) we removed make up the first part of the erroneous content
		List<Object> errorNodeBuilder = new ArrayList<>();
        errorNodeBuilder.add(Symbols.__PARSED_FRAGMENT); // not an error-indicating element type, see next paragraph
		for (int i = stackSize; i < originalStackSize; i++) {
			errorNodeBuilder.add(parseTreeStack[i]);
		}

		// This special object is used to signal the exact location of the error to the PsiBuilder. We do not mark
		// the whole error node as an error because IntelliJ would then underline that whole node, making it harder
		// for the user to locate the error. For example, if the grammar allows to reduce the content for a whole
		// broken statement to %error, IntelliJ would underline the whole broken statement as an error, not just the
		// location where the error occurred.
		ErrorLocationIndicator errorLocationIndicator = new ErrorLocationIndicator();
		errorLocationIndicator.state = originalState;
		errorNodeBuilder.add(errorLocationIndicator);

		// shift the error symbol. The parse tree is the node builder, so we can add further discarded tokens below.
		if (!consumeSymbol(ERROR_SYMBOL_CODE, errorNodeBuilder)) {
			throw new RuntimeException("failed to push error symbol in state that should consume it");
		}

		// throw away further erroneous content until parsing works again for RECOVERY_SYNC_LENGTH steps
		while (true) {

			// make a backup of the state and stack
			int backupState = state;
			int backupStackSize = stackSize;
			int[] backupStateStack = Arrays.copyOf(stateStack, stackSize);
			Object[] backupParseTreeStack = Arrays.copyOf(parseTreeStack, stackSize);

			// Attempt to parse for RECOVERY_SYNC_LENGTH steps (stop early if we hit EOF). If we reach EOF, then we
			// must be able to consume that too
			PsiBuilder.Marker marker = psiBuilder.mark();
			boolean success = true;
			for (int i = 0; i < RECOVERY_SYNC_LENGTH && !psiBuilder.eof(); i++) {
           		if (consumeSymbol(getSymbolCodeForElementType(psiBuilder.getTokenType()), null)) {
                	psiBuilder.advanceLexer();
           		} else {
           			success = false;
           			break;
           		}
			}
			if (success && psiBuilder.eof()) {
				success = consumeSymbol(EOF_SYMBOL_CODE, null);
			}
			marker.rollbackTo();

			// restore state and stack backup
			System.arraycopy(backupStateStack, 0, stateStack, 0, backupStackSize);
			System.arraycopy(backupParseTreeStack, 0, parseTreeStack, 0, backupStackSize);
			stackSize = backupStackSize;
			state = backupState;

			// Check if successful. If so, resume normal parsing. If not, discard a token.
			if (success) {
				return;
			}
			if (psiBuilder.eof()) {
				// Error recovery failed, so we'll signal a "giving up" syntax error and wrap the remainder of the
				// input in a dummy AST node. We don't bother restoring the original parser state since it's
				// irrelevant now. The PSI builder need not be reset here -- that happens automatically after the
				// catch block.
				stackSize = originalStackSize;
				throw new UnrecoverableSyntaxException(originalState);
			}
			errorNodeBuilder.add(null);
			psiBuilder.advanceLexer();

		}

	}

    private static class UnrecoverableSyntaxException extends Exception {

        public UnrecoverableSyntaxException() {
            super("syntax error");
        }

        public UnrecoverableSyntaxException(int state) {
            super("expected one of: " + STATE_INPUT_EXPECTATION[state]);
        }

    }

    private static class ErrorLocationIndicator {
        int state;
    }

}
