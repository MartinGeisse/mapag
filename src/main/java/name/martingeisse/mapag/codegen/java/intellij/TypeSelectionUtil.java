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
			String operandTypeCm = getEffectiveTypeForSymbol(Usage.CM, grammar, operandSymbol);
			if (usage == Usage.CM) {
				return "CmOptional<" + operandTypeCm + ">";
			} else {
				String operandTypePsi = getEffectiveTypeForSymbol(Usage.PSI, grammar, operandSymbol);
				return "CmOptionalImpl<" + operandTypeCm + ", " + operandTypePsi + ">";
			}
		} else if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementTypeCm = getEffectiveTypeForSymbol(Usage.CM, grammar, listElementSymbol);
			if (usage == Usage.CM) {
				return "CmList<" + listElementTypeCm + ">";
			} else {
				String listElementTypePsi = getEffectiveTypeForSymbol(Usage.PSI, grammar, listElementSymbol);
				return "CmListImpl<" + listElementTypeCm + ", " + listElementTypePsi + ">";
			}
		} else {
			throw new RuntimeException("unknown PsiStyle subclass: " + psiStyle);
		}
	}

	public static String getAdditionalConstructorArgumentsForSymbol(Grammar grammar, NonterminalDefinition nonterminalDefinition, String symbolHolder) {
		PsiStyle psiStyle = nonterminalDefinition.getPsiStyle();
		if (psiStyle instanceof PsiStyle.Repetition) {
			String listElementSymbol = ((PsiStyle.Repetition) psiStyle).getElementSymbol();
			String listElementTypeCm = getEffectiveTypeForSymbol(Usage.CM, grammar, listElementSymbol);
			String listElementTypePsi = getEffectiveTypeForSymbol(Usage.PSI, grammar, listElementSymbol);
			TerminalDefinition terminalElementDefinition = grammar.getTerminalDefinitions().get(listElementSymbol);
			if (terminalElementDefinition != null) {
				return ", createTokenSet(" + getAstNodeType(terminalElementDefinition, symbolHolder) + "), "
					+ listElementTypeCm + ".class, " + listElementTypePsi + ".class";
			}
			NonterminalDefinition nonterminalElementDefinition = grammar.getNonterminalDefinitions().get(listElementSymbol);
			if (nonterminalElementDefinition != null) {
				return ", createTokenSet(" + getCommaSeparatedAstNodeTypes(nonterminalElementDefinition, symbolHolder) + "), "
					+ listElementTypeCm + ".class, " + listElementTypePsi + ".class";
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
