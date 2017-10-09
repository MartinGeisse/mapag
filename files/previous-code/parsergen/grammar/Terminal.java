package name.martingeisse.parsergen.grammar;

/**
 *
 */
public final class Terminal extends Symbol {

	/**
	 * This is a special terminal that should be returned by the Lexer when EOF has been reached.
	 */
	public static final Terminal EOF = new Terminal("###EOF###");

	public Terminal(String name) {
		super(name);
	}

}
