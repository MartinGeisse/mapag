package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.grammar.canonical.*;

/**
 *
 */
public final class TypeSelectionUtil {

	// prevent instantiation
	private TypeSelectionUtil() {
	}

	public static String getEffectiveTypeForSymbol(Usage usage, Grammar grammar, String symbol) {
		if (grammar.getTerminalDefinitions().get(symbol) != null) {
			return usage == Usage.PSI ? "LeafPsiElement" : "CmToken";
		}
		NonterminalDefinition nonterminalDefinition = grammar.getNonterminalDefinitions().get(symbol);
		if (nonterminalDefinition == null) {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
		return getEffectiveTypeForSymbol(usage, grammar, nonterminalDefinition);
	}

	public static String getEffectiveTypeForSymbol(Usage usage, Grammar grammar, NonterminalDefinition nonterminalDefinition) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle instanceof PsiStyle.Normal) {
			return IdentifierUtil.toIdentifier(nonterminalDefinition.getName(), true) + (usage == Usage.PSI ? "Impl" : "");
		} else if (psiStyle instanceof PsiStyle.Optional) {
			String operandSymbol = ((PsiStyle.Optional) psiStyle).getOperandSymbol();
			String operandType = getEffectiveTypeForSymbol(Usage.CM, grammar, operandSymbol);
			return (usage == Usage.PSI ? "" : "Cm") + "Optional<" + operandType + ">";
		} else if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementType = getEffectiveTypeForSymbol(Usage.CM, grammar, listElementSymbol);
			return (usage == Usage.PSI ? "" : "Cm") + "ListNode<" + listElementType + ">";
		} else {
			throw new RuntimeException("unknown PsiStyle subclass: " + psiStyle);
		}
	}

	public static String getAdditionalConstructorArgumentsForSymbol(Grammar grammar, NonterminalDefinition nonterminalDefinition, String symbolHolder) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementType = getEffectiveTypeForSymbol(Usage.CM, grammar, listElementSymbol);
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

	public static String getEffectiveTypeForAlternative(Usage usage, Grammar grammar, NonterminalDefinition nonterminalDefinition, Alternative alternative) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle.isDistinctSymbolPerAlternative()) {
			return IdentifierUtil.getAlternativeTypeIdentifier(nonterminalDefinition, alternative) + (usage == Usage.PSI ? "Impl" : "");
		} else {
			return getEffectiveTypeForSymbol(usage, grammar, nonterminalDefinition);
		}
	}

	public enum Usage {
		CM, PSI
	}

}
