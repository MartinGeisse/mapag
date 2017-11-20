package name.martingeisse.mapag.input;

import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.ide.MapagSourceFile;

/**
 * Converts the PSI to an extended grammar.
 */
public class PsiToGrammarConverter {

	public Grammar convert(MapagSourceFile mapagSourceFile) {
		name.martingeisse.mapag.input.psi.Grammar psiGrammar = mapagSourceFile.getGrammar();
		if (psiGrammar == null) {
			throw new RuntimeException("could not find grammar PSI node");
		}
		return convert(psiGrammar);
	}

	public Grammar convert(name.martingeisse.mapag.input.psi.Grammar psiGrammar) {
		System.out.println(mapagSourceFile);
		throw new RuntimeException();
	}

}
