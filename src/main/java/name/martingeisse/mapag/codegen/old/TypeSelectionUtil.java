package name.martingeisse.mapag.codegen.old;

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
			return "LeafPsiElement";
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
			return "Optional<" + operandType + ">";
		} else if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementType = getEffectiveTypeForSymbol(grammar, listElementSymbol);
			return "ListNode<" + listElementType + ">";
		} else {
			throw new RuntimeException("unknown PsiStyle subclass: " + psiStyle);
		}
	}

	public static String getAdditionalConstructorArgumentsForSymbol(Grammar grammar, NonterminalDefinition nonterminalDefinition, String symbolHolder) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementType = getEffectiveTypeForSymbol(grammar, listElementSymbol);
			TerminalDefinition terminalElementDefinition = grammar.getTerminalDefinitions().get(listElementSymbol);
			if (terminalElementDefinition != null) {
				return ", createTokenSet(" + getAstNodeType(terminalElementDefinition, symbolHolder) + "), " + listElementType + ".class";
			}
			NonterminalDefinition nonterminalElementDefinition = grammar.getNonterminalDefinitions().get(listElementSymbol);
			if (nonterminalElementDefinition != null) {
				return ", createTokenSet(" + getCommaSeparatedAstNodeTypes(nonterminalElementDefinition, symbolHolder) + "), " + listElementType + ".class";
			}
			throw new RuntimeException("unknown symbol: " + listElementSymbol);
		} else {
			return "";
		}
	}

	private static String getAstNodeType(TerminalDefinition terminal, String symbolHolder) {
		return symbolHolder + '.' + terminal.getName();
	}

	private static String getCommaSeparatedAstNodeTypes(NonterminalDefinition nonterminal, String symbolHolder) {
		if (nonterminal.getPsiStyle().isDistinctSymbolPerAlternative()) {
			String result = null;
			for (Alternative alternative : nonterminal.getAlternatives()) {
				if (result == null) {
					result = "";
				} else {
					result += ", ";
				}
				result += symbolHolder + '.' + IdentifierUtil.getAlternativeVariableIdentifier(nonterminal, alternative);
			}
			return result;
		} else {
			return symbolHolder + '.' + IdentifierUtil.getNonterminalVariableIdentifier(nonterminal);
		}
	}

	public static String getEffectiveTypeForAlternative(Grammar grammar, NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle.isDistinctSymbolPerAlternative()) {
			return IdentifierUtil.getAlternativeTypeIdentifier(nonterminalDefinition, alternative);
		} else {
			return getEffectiveTypeForSymbol(grammar, nonterminalDefinition);
		}
	}

}
