package name.martingeisse.mapag.input;

import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.ide.MapagSourceFile;

/**
 * Converts the PSI to an extended grammar.
 */
public class PsiToGrammarConverter {

	public Grammar convert(MapagSourceFile mapagSourceFile) {
		System.out.println(mapagSourceFile);
		throw new RuntimeException();
	}

}
