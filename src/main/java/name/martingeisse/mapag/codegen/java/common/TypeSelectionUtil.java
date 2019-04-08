package name.martingeisse.mapag.codegen.java.common;

import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.grammar.canonical.*;

/**
 *
 */
public final class TypeSelectionUtil {

	// prevent instantiation
	private TypeSelectionUtil() {
	}

	public static String getEffectiveTypeForSymbol(Grammar grammar, String symbol) {
		if (grammar.getTerminalDefinitions().get(symbol) != null) {
			return "CmToken";
		}
		NonterminalDefinition nonterminalDefinition = grammar.getNonterminalDefinitions().get(symbol);
		if (nonterminalDefinition == null) {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
		return getEffectiveTypeForSymbol(grammar, nonterminalDefinition);
	}

	public static String getEffectiveTypeForSymbol(Grammar grammar, NonterminalDefinition nonterminalDefinition) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle instanceof PsiStyle.Normal) {
			return IdentifierUtil.toIdentifier(nonterminalDefinition.getName(), true);
		} else if (psiStyle instanceof PsiStyle.Optional) {
			String operandSymbol = ((PsiStyle.Optional) psiStyle).getOperandSymbol();
			String operandType = getEffectiveTypeForSymbol(grammar, operandSymbol);
			return "CmOptional<" + operandType + ">";
		} else if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementType = getEffectiveTypeForSymbol(grammar, listElementSymbol);
			return "CmList<" + listElementType + ">";
		} else {
			throw new RuntimeException("unknown PsiStyle subclass: " + psiStyle);
		}
	}

}
