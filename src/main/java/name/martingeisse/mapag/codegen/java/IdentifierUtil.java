package name.martingeisse.mapag.codegen.java;

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

	public static String getNonterminalTypeIdentifier(NonterminalDefinition nonterminalDefinition) {
		return getNonterminalTypeIdentifier(nonterminalDefinition.getName());
	}

	public static String getNonterminalTypeIdentifier(String nonterminalName) {
		return toIdentifier(nonterminalName, true);
	}

	public static String getNonterminalVariableIdentifier(NonterminalDefinition nonterminalDefinition) {
		return getNonterminalVariableIdentifier(nonterminalDefinition.getName());
	}

	public static String getNonterminalVariableIdentifier(String nonterminalName) {
		return toIdentifier(nonterminalName, false);
	}

	public static String getAlternativeTypeIdentifier(NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		if (nonterminalDefinition.getAlternatives().size() == 1) {
			return getNonterminalTypeIdentifier(nonterminalDefinition);
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
