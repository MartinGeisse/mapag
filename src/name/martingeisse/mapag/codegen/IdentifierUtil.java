package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;

/**
 *
 */
public final class IdentifierUtil {

	// prevent instantiation
	private IdentifierUtil() {
	}

	public static String getNonterminalClassIdentifier(NonterminalDefinition nonterminalDefinition) {
		return getNonterminalClassIdentifier(nonterminalDefinition.getName());
	}

	public static String getNonterminalClassIdentifier(String nonterminalName) {
		return toIdentifier(nonterminalName, true);
	}

	public static String getNonterminalVariableIdentifier(NonterminalDefinition nonterminalDefinition) {
		return getNonterminalVariableIdentifier(nonterminalDefinition.getName());
	}

	public static String getNonterminalVariableIdentifier(String nonterminalName) {
		return toIdentifier(nonterminalName, false);
	}

	public static String getAlternativeClassIdentifier(NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		if (nonterminalDefinition.getAlternatives().size() == 1) {
			return getNonterminalClassIdentifier(nonterminalDefinition);
		} else {
			return toIdentifier(nonterminalDefinition.getName() + '/' + alternative.getAnnotation().getAlternativeName(), true);
		}
	}

	public static String getAlternativeVariableIdentifier(NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		if (nonterminalDefinition.getAlternatives().size() == 1) {
			return getNonterminalVariableIdentifier(nonterminalDefinition);
		} else {
			return toIdentifier(nonterminalDefinition.getName() + '/' + alternative.getAnnotation().getAlternativeName(), false);
		}
	}

	public static String toIdentifier(String s, boolean firstCharacterUppercase) {
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
