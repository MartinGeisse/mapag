package name.martingeisse.mapag.codegen.intellij;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.util.GenericIdentifierUtil;

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
			return toIdentifier(nonterminalDefinition.getName() + '/' + alternative.getName(), true);
		}
	}

	public static String getAlternativeVariableIdentifier(NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		if (nonterminalDefinition.getAlternatives().size() == 1) {
			return getNonterminalVariableIdentifier(nonterminalDefinition);
		} else {
			return toIdentifier(nonterminalDefinition.getName() + '/' + alternative.getName(), false);
		}
	}

	public static String toIdentifier(String s, boolean firstCharacterUppercase) {
		return GenericIdentifierUtil.toIdentifier(s, firstCharacterUppercase);
	}

}
