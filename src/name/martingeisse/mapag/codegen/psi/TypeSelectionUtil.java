package name.martingeisse.mapag.codegen.psi;

import name.martingeisse.mapag.codegen.IdentifierUtil;
import name.martingeisse.mapag.grammar.canonical.*;

/**
 *
 */
final class TypeSelectionUtil {

	// prevent instantiation
	private TypeSelectionUtil() {
	}

	static String getEffectiveTypeForSymbol(Grammar grammar, String symbol) {
		if (grammar.getTerminalDefinitions().get(symbol) != null) {
			return "LeafPsiElement";
		}
		NonterminalDefinition nonterminalDefinition = grammar.getNonterminalDefinitions().get(symbol);
		if (nonterminalDefinition == null) {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
		return getEffectiveTypeForSymbol(grammar, nonterminalDefinition);
	}

	static String getEffectiveTypeForSymbol(Grammar grammar, NonterminalDefinition nonterminalDefinition) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle instanceof PsiStyle.Normal) {
			return IdentifierUtil.toIdentifier(nonterminalDefinition.getName(), true);
		} else if (psiStyle instanceof PsiStyle.Optional) {
			String operandSymbol = ((PsiStyle.Optional) psiStyle).getOperandSymbol();
			String operandType = getEffectiveTypeForSymbol(grammar, operandSymbol);
			return "Optional<" + operandType + ">";
		} else if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementType = getEffectiveTypeForSymbol(grammar, listElementSymbol);
			return "ListNode<" + listElementType + ">";
		} else if (psiStyle instanceof PsiStyle.Transparent) {
			throw new RuntimeException("trying to determine the effective type for PsiStyle=transparent nonterminal " + nonterminalDefinition.getName());
		} else {
			throw new RuntimeException("unknown PsiStyle subclass: " + psiStyle);
		}
	}

	static String getEffectiveTypeForAlternative(Grammar grammar, NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle.isDistinctSymbolPerAlternative()) {
			return IdentifierUtil.getAlternativeClassIdentifier(nonterminalDefinition, alternative);
		} else {
			return getEffectiveTypeForSymbol(grammar, nonterminalDefinition);
		}
	}

}
