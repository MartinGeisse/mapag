




package name.martingeisse.mapag.input;

    import com.intellij.lang.ASTNode;
    import com.intellij.lang.LightPsiParser;
    import com.intellij.lang.PsiBuilder;
    import com.intellij.lang.PsiParser;
    import com.intellij.psi.TokenType;
    import com.intellij.psi.tree.IElementType;



import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapagGeneratedMapagParser     implements PsiParser, LightPsiParser
 {

	// ------------------------------------------------------------------------------------------------
	// --- generated stuff
	// ------------------------------------------------------------------------------------------------

	// symbols (tokens and nonterminals)
	private static final int EOF_SYMBOL_CODE = 0;
	private static final int ERROR_SYMBOL_CODE = 1;
	private static final IElementType[] SYMBOL_CODE_TO_ELEMENT_TYPE = {
    	null, // %eof -- doesn't have an IElementType
    	null, // %error -- doesn't have an IElementType
    	TokenType.BAD_CHARACTER, // %badchar
                    Symbols.ASTERISK,
                    Symbols.BAR,
                    Symbols.BLOCK_COMMENT,
                    Symbols.CLOSING_CURLY_BRACE,
                    Symbols.CLOSING_PARENTHESIS,
                    Symbols.COLON,
                    Symbols.COMMA,
                    Symbols.EXPANDS_TO,
                    Symbols.IDENTIFIER,
                    Symbols.KW_EMPTY,
                    Symbols.KW_EOF,
                    Symbols.KW_ERROR,
                    Symbols.KW_LEFT,
                    Symbols.KW_NONASSOC,
                    Symbols.KW_PRECEDENCE,
                    Symbols.KW_REDUCE,
                    Symbols.KW_REDUCE_ON_ERROR,
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
	private static final int ACTION_TABLE_WIDTH = 54;
	private static final int[] ACTION_TABLE;
	static {
		try (InputStream inputStream = MapagGeneratedMapagParser.class.getResourceAsStream("/MapagGeneratedMapagParser.actions")) {
			try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
				ACTION_TABLE = new int[138 * ACTION_TABLE_WIDTH];
				for (int i = 0; i < ACTION_TABLE.length; i++) {
					ACTION_TABLE[i] = dataInputStream.readInt();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// state machine (alternatives / reduction table)
	private static final int[] ALTERNATIVE_INDEX_TO_RIGHT_HAND_SIDE_LENGTH = {
                    2,
                    1,
                    1,
                    1,
                    2,
                    3,
                    2,
                    2,
                    6,
                    6,
                    2,
                    3,
                    3,
                    4,
                    6,
                    5,
                    2,
                    2,
                    1,
                    4,
                    4,
                    3,
                    1,
                    3,
                    2,
                    4,
                    4,
                    0,
                    2,
                    0,
                    1,
                    0,
                    2,
                    3,
                    2,
                    1,
                    1,
                    3,
                    0,
                    2,
                    6,
                    1,
                    1,
                    1,
                    2,
                    1,
                    1,
                    1,
                    3,
                    3,
                    4,
                    0,
                    2,
                    2,
                    4,
                    1,
                    1,
                    1,
                    1,
                    1,
        	};
	private static final Object[] ALTERNATIVE_INDEX_TO_PARSE_NODE_HEAD = {
                    Symbols.rightHandSide,
                    Symbols.expression_Empty,
                    Symbols.expression_Identifier,
                    Symbols.expression_Error,
                    Symbols.expression_Sequence,
                    Symbols.expression_Or,
                    Symbols.expression_ZeroOrMore,
                    Symbols.expression_OneOrMore,
                    Symbols.expression_SeparatedZeroOrMore,
                    Symbols.expression_SeparatedOneOrMore,
                    Symbols.expression_Optional,
                    Symbols.expression_Parenthesized,
                    Symbols.expression_Named,
                    Symbols.production_SingleUnnamed,
                    Symbols.production_SingleNamed,
                    Symbols.production_Multi,
                    Symbols.production_ErrorWithoutNonterminalNameWithSemicolon,
                    Symbols.production_ErrorWithoutNonterminalNameWithClosingCurlyBrace,
                    Symbols.production_ErrorWithoutNonterminalNameAtEof,
                    Symbols.production_ErrorWithNonterminalNameWithSemicolon,
                    Symbols.production_ErrorWithNonterminalNameWithClosingCurlyBrace,
                    Symbols.production_ErrorWithNonterminalNameAtEof,
                    new ListNodeGenerationWrapper(Symbols.synthetic_SeparatedList_TerminalDeclaration_COMMA_Nonempty),
                    new ListNodeGenerationWrapper(Symbols.synthetic_SeparatedList_TerminalDeclaration_COMMA_Nonempty),
                    Symbols.production_Multi_Alternatives_Unnamed,
                    Symbols.production_Multi_Alternatives_Named,
                    Symbols.grammar_TerminalDeclarations,
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_PrecedenceDeclaration),
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_PrecedenceDeclaration),
                    Symbols.grammar_PrecedenceTable_Optional,
                    Symbols.grammar_PrecedenceTable_Optional,
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_AlternativeAttribute),
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_AlternativeAttribute),
                    Symbols.precedenceDeclaration_Normal,
                    Symbols.precedenceDeclaration_ErrorWithSemicolon,
                    Symbols.precedenceDeclaration_ErrorWithoutSemicolon,
                    new ListNodeGenerationWrapper(Symbols.synthetic_SeparatedList_PrecedenceDeclarationSymbol_COMMA_Nonempty),
                    new ListNodeGenerationWrapper(Symbols.synthetic_SeparatedList_PrecedenceDeclarationSymbol_COMMA_Nonempty),
                    new ListNodeGenerationWrapper(Symbols.production_Multi_Alternatives_List),
                    new ListNodeGenerationWrapper(Symbols.production_Multi_Alternatives_List),
                    Symbols.grammar,
                    Symbols.terminalDeclaration,
                    Symbols.precedenceDeclarationSymbol,
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_Production_Nonempty),
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_Production_Nonempty),
                    Symbols.resolveDeclaration_Action_Shift,
                    Symbols.resolveDeclaration_Action_Reduce,
                    new ListNodeGenerationWrapper(Symbols.synthetic_SeparatedList_IDENTIFIER_COMMA_Nonempty),
                    new ListNodeGenerationWrapper(Symbols.synthetic_SeparatedList_IDENTIFIER_COMMA_Nonempty),
                    Symbols.resolveDeclaration,
                    Symbols.grammar_PrecedenceTable,
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_ResolveDeclaration),
                    new ListNodeGenerationWrapper(Symbols.synthetic_List_ResolveDeclaration),
                    Symbols.alternativeAttribute_Precedence,
                    Symbols.alternativeAttribute_ResolveBlock,
                    Symbols.alternativeAttribute_ReduceOnError,
                    Symbols.alternativeAttribute_Eof,
                    Symbols.precedenceDeclaration_Normal_Associativity_Left,
                    Symbols.precedenceDeclaration_Normal_Associativity_Right,
                    Symbols.precedenceDeclaration_Normal_Associativity_Nonassoc,
        	};
	private static final int[] ALTERNATIVE_INDEX_TO_NONTERMINAL_SYMBOL_CODE = {
                    45,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    32,
                    40,
                    40,
                    40,
                    40,
                    40,
                    40,
                    40,
                    40,
                    40,
                    52,
                    52,
                    41,
                    41,
                    36,
                    47,
                    47,
                    35,
                    35,
                    46,
                    46,
                    37,
                    37,
                    37,
                    51,
                    51,
                    42,
                    42,
                    33,
                    53,
                    39,
                    48,
                    48,
                    44,
                    44,
                    50,
                    50,
                    43,
                    34,
                    49,
                    49,
                    31,
                    31,
                    31,
                    31,
                    38,
                    38,
                    38,
        	};

	// state machine (error messages)
	private static final String[] STATE_INPUT_EXPECTATION = {
                    "%terminals",
                    "",
                    "%eof %precedence %reduceOnError %resolve ;",
                    "%eof %precedence %reduceOnError %resolve ;",
                    "identifier",
                    "%eof %precedence %reduceOnError %resolve ;",
                    "%eof %precedence %reduceOnError %resolve ;",
                    "{",
                    "resolve-declaration %reduce %shift }",
                    "resolve-declaration %reduce %shift }",
                    "%eof %precedence %reduceOnError %resolve ;",
                    "expression",
                    "expression",
                    "expression",
                    "expression ) * + , : ? |",
                    "expression ) * + , : ? |",
                    "expression ) * + , : ? |",
                    "expression %empty %error ( ) * + , : ? identifier |",
                    "expression %empty %error ( ) * + , : ? identifier |",
                    "expression",
                    "expression",
                    "expression",
                    "expression",
                    "expression ) * + : ? |",
                    "expression ) * + : ? |",
                    "expression ) * + : ? |",
                    "expression %empty %error ( ) * + : ? identifier |",
                    "expression %empty %error ( ) * + : ? identifier |",
                    "expression",
                    "expression %empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "expression %empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "expression %eof %precedence %reduceOnError %resolve * + : ; ? |",
                    "expression",
                    "expression",
                    "expression {",
                    "expression identifier }",
                    "expression",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ::= ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "* +",
                    "* +",
                    "* +",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "identifier",
                    "identifier",
                    "identifier",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
                    "%empty %error ( ) * + , : ? identifier |",
                    "%empty %error ( ) * + : ? identifier |",
                    "%empty %eof %error %precedence %reduceOnError %resolve ( * + : ; ? identifier |",
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
                    "%start",
                    "{",
                    "identifier",
                    ", }",
                    "%precedence %start",
                    "%left %nonassoc %right ; }",
                    "%left %nonassoc %right }",
                    "identifier",
                    ", ;",
                    "%left %nonassoc %right }",
                    "identifier",
                    "identifier",
                    "identifier",
                    "identifier",
                    ", ;",
                    "; }",
                    "identifier",
                    "identifier",
                    ": ::=",
                    "identifier",
                    "::=",
                    ";",
                    "identifier",
                    "; }",
                    "identifier",
                    "identifier",
                    "%empty %error ( identifier }",
                    "identifier",
                    ";",
                    "identifier",
                    ";",
                    "%empty %error ( identifier }",
                    ";",
                    "%empty %error ( identifier }",
                    "%empty %error ( identifier }",
                    "identifier",
                    ", ;",
                    "%reduce %shift }",
                    "identifier",
                    "identifier",
                    "%eof %precedence %reduceOnError %resolve ;",
                    "%left %nonassoc %right }",
                    "identifier",
                    "identifier",
                    "%reduce %shift }",
                    ", ;",
                    "identifier",
                    ", ;",
                    ", ;",
                    ", ;",
                    "identifier",
                    ", }",
                    ", }",
                    ", }",
            };

	// other
	private static final int RECOVERY_SYNC_LENGTH = 3;

    // target-specific declarations
	
    private static final IElementType FILE_ELEMENT_TYPE = name.martingeisse.mapag.ide.MapagParserDefinition.FILE_ELEMENT_TYPE;


    private void beginSpeculativeTokenConsumption() {
        speculativeTokenConsumptionMarker = psiBuilder.mark();
    }

    private void endSpeculativeTokenConsumption() {
        speculativeTokenConsumptionMarker.rollbackTo();
    }

    private IElementType getTokenType() {
        return psiBuilder.getTokenType();
    }

    private boolean isAtEof() {
        return psiBuilder.eof();
    }

    private void nextToken() {
        psiBuilder.advanceLexer();
    }

    private void reportErrorForCurrentToken(String errorMessage) {
        psiBuilder.error(errorMessage);
    }

	private void feedPsiBuilder(Object what, IElementType parentElementType) {
	    if (what == null) {
            nextToken();
	    } else if (what instanceof Object[]) {
			Object[] reduction = (Object[]) what;
			IElementType elementType;
			boolean buildMarker;
			if (reduction[0] instanceof ListNodeGenerationWrapper) {
				ListNodeGenerationWrapper listNodeGenerationWrapper = (ListNodeGenerationWrapper)reduction[0];
				elementType = listNodeGenerationWrapper.elementType;
				buildMarker = (parentElementType == null || elementType != parentElementType);
			} else {
				elementType = (IElementType)reduction[0];
				buildMarker = true;
			}
			PsiBuilder.Marker marker = null;
			if (buildMarker) {
				marker = psiBuilder.mark();
			}
			for (int i = 1; i < reduction.length; i++) {
				feedPsiBuilder(reduction[i], elementType);
			}
			if (buildMarker) {
				marker.done(elementType);
			}
		} else if (what instanceof List<?>) {
			// an object list has the same meaning as an object array (needed for error symbols)
			feedPsiBuilder(((List<?>)what).toArray(), parentElementType);
		} else if (what instanceof ErrorLocationIndicator) {
		    ErrorLocationIndicator errorLocationIndicator = (ErrorLocationIndicator)what;
			reportErrorForCurrentToken("expected one of: " + STATE_INPUT_EXPECTATION[errorLocationIndicator.state]);
		} else if (what instanceof UnrecoverableSyntaxException) {
            reportErrorForCurrentToken(((UnrecoverableSyntaxException)what).getMessage());
            while (!isAtEof()) {
            	nextToken();
            }
		}
	}



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
	// --- The main parser code. Code generation here mostly deals with IntelliJ vs. standalone.
	// ------------------------------------------------------------------------------------------------

	private PsiBuilder psiBuilder;
	private PsiBuilder.Marker speculativeTokenConsumptionMarker;

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
	public void parseLight(IElementType type, PsiBuilder psiBuilder) {
		if (type != FILE_ELEMENT_TYPE) {
			throw new IllegalArgumentException("unsupported top-level element type to parse: " + type);
		}
	    this.psiBuilder = psiBuilder;
		parse();
	}

	private void parse() {

        // prevent re-use of this object since the internal state gets changed during parsing
		if (used) {
			throw new IllegalStateException("cannot re-use this parser object");
		}
		used = true;

		// initialize static parser information
		initializeStatic();


        // handle unrecoverable syntax errors
        PsiBuilder.Marker wholeFileMarker = psiBuilder.mark();
        PsiBuilder.Marker preParseMarker = psiBuilder.mark();
        try {

            // Parse the input using the generated machine to build a parse tree. The state machine cannot execute the
            // accept action here since the input cannot contain EOF.
            while (!isAtEof()) {
           		if (consumeSymbol(getSymbolCodeForElementType(getTokenType()), null)) {
                	nextToken();
           		} else {
           			recoverFromError();
           		}
            }

            // Consume the EOF token. This should (possibly after some reductions) accept the input. If not, this causes
            // a syntax error (unexpected EOF), since the parser generator wouldn't emit a "shift EOF" action.
            {
                int originalState = state;
                if (!consumeSymbol(EOF_SYMBOL_CODE, null)) {
                    recoverFromError();
                    if (!consumeSymbol(EOF_SYMBOL_CODE, null)) {
                        throw new UnrecoverableSyntaxException(originalState);
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
		// consuming the EOF token would have failed.
		if (stackSize != 1) {
			// either the Lexer returned an explicit EOF (which it shouldn't) or this is a bug in the parser generator
			throw new RuntimeException("unexpected stack size after accepting start symbol");
		}

		// Now we re-parse, based on the parse tree we build, in a way that the PsiBuilder likes.
		feedPsiBuilder(parseTreeStack[0], null);

		// Before we consider the file parsed, we must advance the lexer once more so it consumes end-of-file
		// whitespace and comments as part of the root AST node.
		nextToken();
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
		Object parseNodeHead = ALTERNATIVE_INDEX_TO_PARSE_NODE_HEAD[alternativeIndex];
		int nonterminalSymbolCode = ALTERNATIVE_INDEX_TO_NONTERMINAL_SYMBOL_CODE[alternativeIndex];


		// pop (rightHandSideLength) states off the state stack
		if (rightHandSideLength > 0) {
			stackSize -= rightHandSideLength;
			state = stateStack[stackSize];
		}

		// build a parse tree node from the nonterminal element type and the subtrees in the parse tree stack
		Object[] reduction = new Object[rightHandSideLength + 1];
		reduction[0] = parseNodeHead;
		System.arraycopy(parseTreeStack, stackSize, reduction, 1, rightHandSideLength);

		// shift the nonterminal (errors cannot occur here in LR(1) parsing)
		if (!consumeSymbol(nonterminalSymbolCode, reduction)) {
			throw new RuntimeException("syntax error while shifting a nonterminal... WTF?");
		}

	}

	private void recoverFromError() throws UnrecoverableSyntaxException {

		int originalState = state; // used for error messages

    	// First, attempt implicit reduce-on-error to reduce a nonterminal that was actually completed directly before
    	// the error, but not yet reduced since LR(1) would demand the right lookahead token to reduce. We want
    	// to reduce that nonterminal to give other IDE parts a better partial PSI to work with. However, we must
    	// make sure that we don't reduce a state away from the stack that could handle the error symbol, because
    	// we would prevent subsequent error recovery. Also, we obviously cannot reduce "the" alternative if there
    	// are multiple ones. Note that specifying %reduceOnError overrides any of these concerns, but doing so places
    	// the corresponding reductions into the parse table, so once we're here, we can ignore that modifier.
    	// Note that we might be able to reduce multiple times before we have to start error recovery, hence the loop.
    	outerLoop: while (true) {

    	    // look for a unique reduce action code in the action table
            int reduceActionCode = 0;
            for (int i = 0; i < ACTION_TABLE_WIDTH; i++) {
                int action = ACTION_TABLE[state * ACTION_TABLE_WIDTH + i];
                if (action < 0 && action != Integer.MIN_VALUE) {
                    if (reduceActionCode == 0) {
                        reduceActionCode = action; // found a reduce action
                    } else if (reduceActionCode != action) {
                        break outerLoop; // found a different reduce action, so we cannot reduce
                    }
                }
            }
			if (reduceActionCode == 0) {
				break;
			}

            // Make sure we don't remove a state from the stack that could consume %error. At depth
            // (rightHandSideLength) on the state stack lies the state to reveal by reducing (and before shifting
            // the nonterminal), so that state is NOT checked here -- even if it can handle %error, reducing won't
            // prevent that. Only the states above it on the stack are checked. As an implicit special case, we can
            // always reduce an alternative with a zero-length right hand side, no matter the current state and stack
            // contents.
            int alternativeIndex = -reduceActionCode - 1;
            int rightHandSideLength = ALTERNATIVE_INDEX_TO_RIGHT_HAND_SIDE_LENGTH[alternativeIndex];
            int probeState = state, depth = 0;
            while (depth < rightHandSideLength) {
                if (ACTION_TABLE[probeState * ACTION_TABLE_WIDTH + ERROR_SYMBOL_CODE] != 0) {
                    break outerLoop; // found error-handling state
                }
                depth++;
                probeState = stateStack[stackSize - depth];
            }

            // no state found that could recover, so let's reduce
            reduce(alternativeIndex);

    	}

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
			beginSpeculativeTokenConsumption();
			boolean success = true;
			for (int i = 0; i < RECOVERY_SYNC_LENGTH && !isAtEof(); i++) {
           		if (consumeSymbol(getSymbolCodeForElementType(getTokenType()), null)) {
                	nextToken();
           		} else {
           			success = false;
           			break;
           		}
			}
			if (success && isAtEof()) {
				success = consumeSymbol(EOF_SYMBOL_CODE, null);
			}
			endSpeculativeTokenConsumption();

			// restore state and stack backup
			System.arraycopy(backupStateStack, 0, stateStack, 0, backupStackSize);
			System.arraycopy(backupParseTreeStack, 0, parseTreeStack, 0, backupStackSize);
			stackSize = backupStackSize;
			state = backupState;

			// Check if successful. If so, resume normal parsing. If not, discard a token.
			if (success) {
				return;
			}
			if (isAtEof()) {
				// Error recovery failed, so we'll signal a "giving up" syntax error and wrap the remainder of the
				// input in a dummy AST node. We don't bother restoring the original parser state since it's
				// irrelevant now. The PSI builder need not be reset here -- that happens automatically after the
				// catch block.
				stackSize = originalStackSize;
				throw new UnrecoverableSyntaxException(originalState);
			}
			errorNodeBuilder.add(null);
			nextToken();

		}

	}

    private static class UnrecoverableSyntaxException extends Exception {

        UnrecoverableSyntaxException(int state) {
            super("expected one of: " + STATE_INPUT_EXPECTATION[state]);
        }

    }

    private static class ErrorLocationIndicator {
        int state;
    }

	private static class ListNodeGenerationWrapper {

		IElementType elementType;

		ListNodeGenerationWrapper(IElementType elementType) {
			this.elementType = elementType;
		}

		public String toString() {
		    return "LIST(" + elementType + ")";
		}

	}

}

