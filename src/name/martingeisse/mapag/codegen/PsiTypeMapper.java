package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;

/**
 *
 */
public class PsiTypeMapper {

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;

	public PsiTypeMapper(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
	}

	public String getEffectiveTypeForNonterminal(String nonterminal) {
		return getGeneratedTypeForNonterminal(nonterminal);
	}

	public String getGeneratedTypeForNonterminal(String nonterminal) {
		return toIdentifier(nonterminal, true);
	}

	public String toIdentifier(String s, boolean firstCharacterUppercase) {
		StringBuilder builder = new StringBuilder();
		boolean firstValidCharacter = true;
		boolean forceNextCharacterCase = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				if (forceNextCharacterCase) {
					if (firstCharacterUppercase || !firstValidCharacter) {
						c = Character.toUpperCase(c);
					} else {
						c = Character.toLowerCase(c);
					}
				}
				builder.append(c);
				firstValidCharacter = false;
				forceNextCharacterCase = false;
			} else if (c >= '0' && c <= '9') {
				if (firstValidCharacter) {
					builder.append('_');
				}
				builder.append(c);
				firstValidCharacter = false;
				forceNextCharacterCase = false;
			} else if (c == '/') {
				builder.append('_');
				forceNextCharacterCase = true;
			} else {
				forceNextCharacterCase = true;
			}
		}
		return builder.toString();
	}

}
