package name.martingeisse.mapag.input;

public class Symbols {

	//
	// terminals
	//

	public static final MapagElementType ASTERISK = new MapagElementType("ASTERISK");
	public static final MapagElementType BAR = new MapagElementType("BAR");
	public static final MapagElementType BLOCK_COMMENT = new MapagElementType("BLOCK_COMMENT");
	public static final MapagElementType CLOSING_CURLY_BRACE = new MapagElementType("CLOSING_CURLY_BRACE");
	public static final MapagElementType CLOSING_PARENTHESIS = new MapagElementType("CLOSING_PARENTHESIS");
	public static final MapagElementType COLON = new MapagElementType("COLON");
	public static final MapagElementType COMMA = new MapagElementType("COMMA");
	public static final MapagElementType EXPANDS_TO = new MapagElementType("EXPANDS_TO");
	public static final MapagElementType IDENTIFIER = new MapagElementType("IDENTIFIER");
	public static final MapagElementType KW_EOF = new MapagElementType("KW_EOF");
	public static final MapagElementType KW_ERROR = new MapagElementType("KW_ERROR");
	public static final MapagElementType KW_LEFT = new MapagElementType("KW_LEFT");
	public static final MapagElementType KW_NONASSOC = new MapagElementType("KW_NONASSOC");
	public static final MapagElementType KW_PRECEDENCE = new MapagElementType("KW_PRECEDENCE");
	public static final MapagElementType KW_REDUCE = new MapagElementType("KW_REDUCE");
	public static final MapagElementType KW_REDUCE_ON_ERROR = new MapagElementType("KW_REDUCE_ON_ERROR");
	public static final MapagElementType KW_RESOLVE = new MapagElementType("KW_RESOLVE");
	public static final MapagElementType KW_RIGHT = new MapagElementType("KW_RIGHT");
	public static final MapagElementType KW_SHIFT = new MapagElementType("KW_SHIFT");
	public static final MapagElementType KW_START = new MapagElementType("KW_START");
	public static final MapagElementType KW_TERMINALS = new MapagElementType("KW_TERMINALS");
	public static final MapagElementType LINE_COMMENT = new MapagElementType("LINE_COMMENT");
	public static final MapagElementType OPENING_CURLY_BRACE = new MapagElementType("OPENING_CURLY_BRACE");
	public static final MapagElementType OPENING_PARENTHESIS = new MapagElementType("OPENING_PARENTHESIS");
	public static final MapagElementType PLUS = new MapagElementType("PLUS");
	public static final MapagElementType QUESTION_MARK = new MapagElementType("QUESTION_MARK");
	public static final MapagElementType SEMICOLON = new MapagElementType("SEMICOLON");

	//
	// nonterminals
	//

	public static final MapagElementType alternativeAttribute_Eof = new MapagElementType("alternativeAttribute_Eof");
	public static final MapagElementType alternativeAttribute_Precedence = new MapagElementType("alternativeAttribute_Precedence");
	public static final MapagElementType alternativeAttribute_ReduceOnError = new MapagElementType("alternativeAttribute_ReduceOnError");
	public static final MapagElementType alternativeAttribute_ResolveBlock = new MapagElementType("alternativeAttribute_ResolveBlock");
	public static final MapagElementType alternativeAttribute_ResolveBlock_ResolveDeclarations_Next = new MapagElementType("alternativeAttribute_ResolveBlock_ResolveDeclarations_Next");
	public static final MapagElementType alternativeAttribute_ResolveBlock_ResolveDeclarations_Start = new MapagElementType("alternativeAttribute_ResolveBlock_ResolveDeclarations_Start");
	public static final MapagElementType expression_Error = new MapagElementType("expression_Error");
	public static final MapagElementType expression_Identifier = new MapagElementType("expression_Identifier");
	public static final MapagElementType expression_Named = new MapagElementType("expression_Named");
	public static final MapagElementType expression_OneOrMore = new MapagElementType("expression_OneOrMore");
	public static final MapagElementType expression_Optional = new MapagElementType("expression_Optional");
	public static final MapagElementType expression_Or = new MapagElementType("expression_Or");
	public static final MapagElementType expression_Parenthesized = new MapagElementType("expression_Parenthesized");
	public static final MapagElementType expression_Sequence = new MapagElementType("expression_Sequence");
	public static final MapagElementType expression_ZeroOrMore = new MapagElementType("expression_ZeroOrMore");
	public static final MapagElementType grammar = new MapagElementType("grammar");
	public static final MapagElementType grammar_1 = new MapagElementType("grammar_1");
	public static final MapagElementType grammar_1_PrecedenceDeclarations_Next = new MapagElementType("grammar_1_PrecedenceDeclarations_Next");
	public static final MapagElementType grammar_1_PrecedenceDeclarations_Start = new MapagElementType("grammar_1_PrecedenceDeclarations_Start");
	public static final MapagElementType grammar_PrecedenceTable_Absent = new MapagElementType("grammar_PrecedenceTable_Absent");
	public static final MapagElementType grammar_PrecedenceTable_Present = new MapagElementType("grammar_PrecedenceTable_Present");
	public static final MapagElementType grammar_Productions_Next = new MapagElementType("grammar_Productions_Next");
	public static final MapagElementType grammar_Productions_Start = new MapagElementType("grammar_Productions_Start");
	public static final MapagElementType nonemptyIdentifierList = new MapagElementType("nonemptyIdentifierList");
	public static final MapagElementType nonemptyIdentifierList_1 = new MapagElementType("nonemptyIdentifierList_1");
	public static final MapagElementType nonemptyIdentifierList_MoreIdentifiers_Next = new MapagElementType("nonemptyIdentifierList_MoreIdentifiers_Next");
	public static final MapagElementType nonemptyIdentifierList_MoreIdentifiers_Start = new MapagElementType("nonemptyIdentifierList_MoreIdentifiers_Start");
	public static final MapagElementType precedenceDeclaration = new MapagElementType("precedenceDeclaration");
	public static final MapagElementType precedenceDeclaration_Associativity_Left = new MapagElementType("precedenceDeclaration_Associativity_Left");
	public static final MapagElementType precedenceDeclaration_Associativity_Nonassoc = new MapagElementType("precedenceDeclaration_Associativity_Nonassoc");
	public static final MapagElementType precedenceDeclaration_Associativity_Right = new MapagElementType("precedenceDeclaration_Associativity_Right");
	public static final MapagElementType production_Error1 = new MapagElementType("production_Error1");
	public static final MapagElementType production_Error2 = new MapagElementType("production_Error2");
	public static final MapagElementType production_Error3 = new MapagElementType("production_Error3");
	public static final MapagElementType production_Error4 = new MapagElementType("production_Error4");
	public static final MapagElementType production_Multi = new MapagElementType("production_Multi");
	public static final MapagElementType production_Multi_1_Named = new MapagElementType("production_Multi_1_Named");
	public static final MapagElementType production_Multi_1_Named_Named = new MapagElementType("production_Multi_1_Named_Named");
	public static final MapagElementType production_Multi_1_Unnamed = new MapagElementType("production_Multi_1_Unnamed");
	public static final MapagElementType production_Multi_1_Unnamed_Unnamed = new MapagElementType("production_Multi_1_Unnamed_Unnamed");
	public static final MapagElementType production_Multi_Alternatives_Next = new MapagElementType("production_Multi_Alternatives_Next");
	public static final MapagElementType production_Multi_Alternatives_Start = new MapagElementType("production_Multi_Alternatives_Start");
	public static final MapagElementType production_SingleNamed = new MapagElementType("production_SingleNamed");
	public static final MapagElementType production_SingleUnnamed = new MapagElementType("production_SingleUnnamed");
	public static final MapagElementType resolveDeclaration = new MapagElementType("resolveDeclaration");
	public static final MapagElementType resolveDeclaration_1 = new MapagElementType("resolveDeclaration_1");
	public static final MapagElementType resolveDeclaration_Action_Reduce = new MapagElementType("resolveDeclaration_Action_Reduce");
	public static final MapagElementType resolveDeclaration_Action_Shift = new MapagElementType("resolveDeclaration_Action_Shift");
	public static final MapagElementType resolveDeclaration_AdditionalSymbols_Next = new MapagElementType("resolveDeclaration_AdditionalSymbols_Next");
	public static final MapagElementType resolveDeclaration_AdditionalSymbols_Start = new MapagElementType("resolveDeclaration_AdditionalSymbols_Start");
	public static final MapagElementType rightHandSide = new MapagElementType("rightHandSide");
	public static final MapagElementType rightHandSide_Attributes_Next = new MapagElementType("rightHandSide_Attributes_Next");
	public static final MapagElementType rightHandSide_Attributes_Start = new MapagElementType("rightHandSide_Attributes_Start");

	//
	// special
	//

	// partially parsed input in case of an error
	public static final MapagElementType __PARSED_FRAGMENT = new MapagElementType("__PARSED_FRAGMENT");

}
