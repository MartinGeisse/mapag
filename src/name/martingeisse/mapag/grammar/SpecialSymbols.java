package name.martingeisse.mapag.grammar;

/**
 *
 */
public final class SpecialSymbols {

	/**
	 * This symbol is used internally in the construction of the state machine. It must not be used
	 * anywhere in the extended or canonical grammar.
	 */
	public static final String ROOT_SYMBOL_NAME = "%start";

	/**
	 * This symbol is passed to the parser at runtime when the lexer runs out of terminals. Therefore it also plays a
	 * role in the construction of the state machine. It must not be used anywhere in the extended or canonical grammar.
	 */
	public static final String EOF_SYMBOL_NAME = "%eof";

	/**
	 * This symbol is used in error recovery. It must not be manually defined in the extended or canonical grammar, nor
	 * be assigned any precedence nor associativity, nor appear on the left-hand side of a production. It may appear in
	 * the right-hand side of productions in either grammar.
	 */
	public static final String ERROR_SYMBOL_NAME = "%error";

	// prevent instantiation
	private SpecialSymbols() {
	}

}
