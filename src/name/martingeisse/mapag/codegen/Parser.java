package name.martingeisse.mapag.codegen;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import java.util.Arrays;
import java.util.Stack;
import java.util.function.Function;

/**
 * ## This class is used to get Java-oriented IntelliJ support while writing the template as part of the parser
 * ## generator. It is not directly used by the parser generator or the generated parser.
 */
public class Parser implements PsiParser, LightPsiParser {

	/*

	TODO:
	- specify how token-to-tokencode-mapping works (generated "Tokens" class as in CUP, or user-written class)
	- specify how token singleton definitions work (generated class as for GrammarKit, or user-written class)

	 */


	// TODO Generated stuff
	private static final int EOF_TOKEN_CODE = 0;
	private static final int[] actionTable = null;
	private static final int actionTableWidth = 0;
	private static final int[] alternativeCodeToRightHandSizeLength = null;
	private static final IElementType[] alternativeCodeToNonterminalElementType = null;
	private static final int[] alternativeCodeToNonterminalSymbolCode = null;
	private static final int START_STATE = 0;
	private static final IElementType START_SYMBOL = null;

	// static table, but has to be initialized at startup since element type indices aren't compile-time constants
	private static int[] elementTypeIndexToTokenCode;

	private boolean used = false;
	private final Stack<Integer> stateStack = new Stack<>(); // TODO this class is synchronized and boxed Integers are also slow. Replace by array-based stack.
	private final Stack<Object> parseTreeStack = new Stack<>(); // same here; the stacks always have the same size
	private int state = START_STATE;

	/**
	 * This must be called at runtime initialization time (more exactly, before a parser is first created) to tell
	 * this class which IElementTypes have been mapped to which token code in the parser state machine.
	 * <p>
	 * TODO the first argument can be determined at generation time, not runtime
	 * TODO also initialize the start symbol -- can be determined at generation time too
	 *
	 * @param tokens          the tokens used by this parser
	 * @param tokenCodeMapper a function that returns the token code for each of these tokens
	 */
	public static void initializeElementTypeIndexToTokenCode(IElementType[] tokens, Function<IElementType, Integer> tokenCodeMapper) {
		int maxElementTypeIndex = 0;
		for (IElementType token : tokens) {
			if (maxElementTypeIndex < token.getIndex()) {
				maxElementTypeIndex = token.getIndex();
			}
		}
		elementTypeIndexToTokenCode = new int[maxElementTypeIndex];
		Arrays.fill(elementTypeIndexToTokenCode, -1);
		for (IElementType token : tokens) {
			elementTypeIndexToTokenCode[token.getIndex()] = tokenCodeMapper.apply(token);
		}
	}

	private static int gerTokenCodeForElementType(IElementType elementType) {
		int index = elementType.getIndex();
		if (index >= 0 && index < elementTypeIndexToTokenCode.length) {
			int tokenCode = elementTypeIndexToTokenCode[index];
			if (tokenCode >= 0) {
				return tokenCode;
			}
		}
		throw new RuntimeException("unknown token: " + elementType);
	}

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
		if (elementTypeIndexToTokenCode == null) {
			throw new IllegalStateException("initializeElementTypeIndexToTokenCode has not been called yet");
		}
		parse(builder);
	}

	private void parse(PsiBuilder psiBuilder) {

		// Parse the input using the generated machine to build a parse tree. The state machine cannot execute the
		// accept action here since the input cannot contain EOF.
		PsiBuilder.Marker preParseMarker = psiBuilder.mark();
		while (!psiBuilder.eof()) {
			consumeToken(gerTokenCodeForElementType(psiBuilder.getTokenType()), null);
		}
		preParseMarker.rollbackTo();

		// Consume the EOF token. This should (possibly after some reductions) accept the input. If not, this causes
		// a syntax error (unexpected EOF), since the parser generator wouldn't emit a "shift EOF" action.
		consumeToken(EOF_TOKEN_CODE, null);

		// At this point, the state stack should contain single element (the start state) and the associated parse
		// tree stack contains the root node as its single element. If anything in the input tried to prevent that,
		// consuming the EOF token would have failed. Now we re-parse, based on the parse tree we build, in a way
		// that the PsiBuilder likes.
		feedPsiBuilder(psiBuilder, parseTreeStack.pop());

	}

	/**
	 * Consumes a token. This performs one or several actions until the token gets shifted (or, in the case of EOF,
	 * accepted).
	 */
	private void consumeToken(int symbolCode, Object symbolData) {
		while (true) { // looped on reduce
			int action = actionTable[state * actionTableWidth + symbolCode];
			if (action == Integer.MIN_VALUE) {

				// accept
				break;

			} else if (action > 0) {

				// shift
				stateStack.push(state);
				parseTreeStack.push(symbolData);
				state = action - 1;
				break;

			} else if (action < 0) {

				// reduce
				int alternativeCode = -action - 1;
				int rightHandSideLength = alternativeCodeToRightHandSizeLength[alternativeCode];
				Object[] reduction = new Object[rightHandSideLength + 1];
				for (int i = rightHandSideLength; i > 0; i--) {
					reduction[i] = parseTreeStack.pop();
					state = stateStack.pop();
				}
				reduction[0] = alternativeCodeToNonterminalElementType[alternativeCode];
				consumeToken(alternativeCodeToNonterminalSymbolCode[alternativeCode], reduction);
				// continue consuming the original token

			} else {

				// syntax error
				throw new RuntimeException("syntax error in state " + state + " on symbolCode " + symbolCode);

			}
		}
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
