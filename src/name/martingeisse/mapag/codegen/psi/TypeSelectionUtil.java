package name.martingeisse.mapag.codegen.psi;

import name.martingeisse.mapag.codegen.IdentifierUtil;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;

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
		if (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.OPTIONAL) {
			String operandSymbol = GrammarAnalysisUtil.recognizeOptionalStyledNonterminal(nonterminalDefinition).getSymbol();
			String operandType = getEffectiveTypeForSymbol(grammar, operandSymbol);
			return "Optional<" + operandType + ">";
		}
		if (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.ZERO_OR_MORE ||
			nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.ONE_OR_MORE ||
			nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.SEPARATED_ONE_OR_MORE) {

			String listElementSymbol = GrammarAnalysisUtil.recognizeRepetitionStyledNonterminal(nonterminalDefinition).getSymbol();
			String listElementType = getEffectiveTypeForSymbol(grammar, listElementSymbol);
			return "ListNode<" + listElementType + ">";
		}
		return IdentifierUtil.toIdentifier(nonterminalDefinition.getName(), true);
	}

}
