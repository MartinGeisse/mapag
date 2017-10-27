package name.martingeisse.mapag.codegen;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import java.util.Arrays;

/**
 * ## This class is used to get Java-oriented IntelliJ support while writing the template as part of the parser
 * ## generator. It is not directly used by the parser generator or the generated parser.
 */
public class Parser implements PsiParser, LightPsiParser {

	// ------------------------------------------------------------------------------------------------
	// --- generated stuff TODO
	// ------------------------------------------------------------------------------------------------

	// symbols (tokens and nonterminals)
	private static final int EOF_TOKEN_CODE = 0;
	private static final IElementType[] TOKEN_CODE_TO_TOKEN = {
		Tokens.IDENTIFIER,
	};
	private static final IElementType START_SYMBOL = null;

	// state machine (general)
	private static final int START_STATE = 0;

	// state machine (action table)
	private static final int[] ACTION_TABLE = {

	};
	private static final int ACTION_TABLE_WIDTH = 0;

	// state machine (alternatives / reduction table)
	private static final int[] ALTERNATIVE_CODE_TO_RIGHT_HAND_SIDE_LENGTH = {

	};
	private static final IElementType[] ALTERNATIVE_CODE_TO_NONTERMINAL_ELEMENT_TYPE = {

	};
	private static final int[] ALTERNATIVE_CODE_TO_NONTERMINAL_SYMBOL_CODE = {

	};

	// ------------------------------------------------------------------------------------------------
	// --- non-generated stuff (initialization and static stuff)
	// ------------------------------------------------------------------------------------------------

	// static table, but has to be initialized at startup since element type indices aren't compile-time constants
	private static int[] elementTypeIndexToTokenCode;

	/**
	 * This method initializes static tables on the first parse run -- we need element type
	 * indices to be initialized before doing this.
	 */
	private static void initializeStatic() {
		if (elementTypeIndexToTokenCode != null) {
			return;
		}
		int maxElementTypeIndex = 0;
		for (IElementType token : TOKEN_CODE_TO_TOKEN) {
			if (maxElementTypeIndex < token.getIndex()) {
				maxElementTypeIndex = token.getIndex();
			}
		}
		elementTypeIndexToTokenCode = new int[maxElementTypeIndex];
		Arrays.fill(elementTypeIndexToTokenCode, -1);
		for (int tokenCode = 0; tokenCode < TOKEN_CODE_TO_TOKEN.length; tokenCode++) {
			IElementType token = TOKEN_CODE_TO_TOKEN[tokenCode];
			elementTypeIndexToTokenCode[token.getIndex()] = tokenCode;
		}
	}

	private static int getTokenCodeForElementType(IElementType elementType) {
		int index = elementType.getIndex();
		if (index >= 0 && index < elementTypeIndexToTokenCode.length) {
			int tokenCode = elementTypeIndexToTokenCode[index];
			if (tokenCode >= 0) {
				return tokenCode;
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
		if (type != START_SYMBOL) {
			throw new IllegalArgumentException("unsupported top-level element type to parse: " + type);
		}
		parse(builder);
	}

	private void parse(PsiBuilder psiBuilder) {

		// initialize static parser information
		initializeStatic();

		// Parse the input using the generated machine to build a parse tree. The state machine cannot execute the
		// accept action here since the input cannot contain EOF.
		PsiBuilder.Marker preParseMarker = psiBuilder.mark();
		while (!psiBuilder.eof()) {
			consumeSymbol(getTokenCodeForElementType(psiBuilder.getTokenType()), null);
		}
		preParseMarker.rollbackTo();

		// Consume the EOF token. This should (possibly after some reductions) accept the input. If not, this causes
		// a syntax error (unexpected EOF), since the parser generator wouldn't emit a "shift EOF" action.
		consumeSymbol(EOF_TOKEN_CODE, null);

		// At this point, the state stack should contain single element (the start state) and the associated parse
		// tree stack contains the root node as its single element. If anything in the input tried to prevent that,
		// consuming the EOF token would have failed. Now we re-parse, based on the parse tree we build, in a way
		// that the PsiBuilder likes.
		if (stackSize != 1) {
			// either the Lexer returned an explicit EOF (which it shouldn't) or this is a bug in the parser generator
			throw new RuntimeException("unexpected stack size after accepting start symbol");
		}
		feedPsiBuilder(psiBuilder, parseTreeStack[0]);

	}

	/**
	 * Consumes a symbol (token or nonterminal). This performs one or several actions until the token gets shifted
	 * (or, in the case of EOF, accepted).
	 */
	private void consumeSymbol(int symbolCode, Object symbolData) {
		while (true) { // looped on reduce
			int action = ACTION_TABLE[state * ACTION_TABLE_WIDTH + symbolCode];
			if (action == Integer.MIN_VALUE) { // accept
				break;
			} else if (action > 0) { // shift
				shift(symbolData, action - 1);
				break;
			} else if (action < 0) { // reduce, then continue with the original symbol
				reduce(-action - 1);
			} else { // syntax error
				throw new RuntimeException("syntax error in state " + state + " on symbolCode " + symbolCode);
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

	private void reduce(int alternativeCode) {

		// determine the alternative to reduce
		int rightHandSideLength = ALTERNATIVE_CODE_TO_RIGHT_HAND_SIDE_LENGTH[alternativeCode];
		IElementType nonterminalElementType = ALTERNATIVE_CODE_TO_NONTERMINAL_ELEMENT_TYPE[alternativeCode];
		int nonterminalSymbolCode = ALTERNATIVE_CODE_TO_NONTERMINAL_SYMBOL_CODE[alternativeCode];

		// pop (rightHandSideLength) states off the state stack
		if (rightHandSideLength > 0) {
			stackSize -= rightHandSideLength;
			state = stateStack[stackSize];
		}

		// build a parse tree node from the nonterminal element type and the subtrees in the parse tree stack
		Object[] reduction = new Object[rightHandSideLength + 1];
		reduction[0] = nonterminalElementType;
		System.arraycopy(parseTreeStack, stackSize, reduction, 1, rightHandSideLength);

		// shift the nonterminal
		consumeSymbol(nonterminalSymbolCode, reduction);

	}

	private void feedPsiBuilder(PsiBuilder builder, Object what) {
		if (what instanceof Object[]) {
			Object[] reduction = (Object[]) what;
			PsiBuilder.Marker marker = builder.mark();
			for (int i = 1; i < reduction.length; i++) {
				feedPsiBuilder(builder, reduction[i]);
			}
			marker.done((IElementType) reduction[0]);
		}
	}

}
