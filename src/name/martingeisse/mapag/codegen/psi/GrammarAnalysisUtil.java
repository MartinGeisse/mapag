package name.martingeisse.mapag.codegen.psi;

import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.IdentifierUtil;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;

import java.io.IOException;

/**
 * Utility methods to analyze the grammar structure with respect to PSI class generation.
 */
final class GrammarAnalysisUtil {

	// prevent instantiation
	private GrammarAnalysisUtil() {
	}

	/**
	 * Analyzes and validates the structure of the specified nonterminal to be a repetition, and returns the list elements' expansion element.
	 */
	static ExpansionElement recognizeRepetitionStyledNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException {
		boolean hasSeparator = (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.SEPARATED_ONE_OR_MORE);
		boolean zeroBased = (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.ZERO_OR_MORE);
		String nonterminalName = nonterminalDefinition.getName();
		if (nonterminalDefinition.getAlternatives().size() != 2) {
			throw new RuntimeException("repetition-styled nonterminal " + nonterminalName + " has " +
				nonterminalDefinition.getAlternatives().size() + " alternatives, expected 2");
		}
		int repetitionCaseLength = (hasSeparator ? 3 : 2);
		Alternative baseCaseAlternative, repetitionCaseAlternative;
		if (nonterminalDefinition.getAlternatives().get(0).getExpansion().getElements().size() == repetitionCaseLength) {
			repetitionCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			baseCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else if (nonterminalDefinition.getAlternatives().get(1).getExpansion().getElements().size() == repetitionCaseLength) {
			baseCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			repetitionCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else {
			throw new RuntimeException("could not find alternative with expansion length " + repetitionCaseLength + " as repetition case for repetition-styled nonterminal " + nonterminalName);
		}
		if (baseCaseAlternative.getExpansion().getElements().size() != (zeroBased ? 0 : 1)) {
			throw new RuntimeException("could not recognize base case for repetition-styled nonterminal " + nonterminalName);
		}
		if (!repetitionCaseAlternative.getExpansion().getElements().get(0).getSymbol().equals(nonterminalName)) {
			throw new RuntimeException("could not find left-recursion for repetition-styled nonterminal " + nonterminalName);
		}
		ExpansionElement expansionElement = repetitionCaseAlternative.getExpansion().getElements().get(hasSeparator ? 2 : 1);
		String elementSymbol = expansionElement.getSymbol();
		if (!zeroBased && !baseCaseAlternative.getExpansion().getElements().get(0).getSymbol().equals(elementSymbol)) {
			throw new RuntimeException("base-case uses different element symbol than repetition case for nonterminal " + elementSymbol);
		}
		return expansionElement;
	}

	/**
	 * Analyzes and validates the structure of the specified nonterminal to be an optional, and returns the operand's expansion element.
	 */
	static ExpansionElement recognizeOptionalStyledNonterminal(NonterminalDefinition nonterminalDefinition) {
		String nonterminalName = nonterminalDefinition.getName();
		if (nonterminalDefinition.getAlternatives().size() != 2) {
			throw new RuntimeException("optional-styled nonterminal " + nonterminalName + " has " +
				nonterminalDefinition.getAlternatives().size() + " alternatives, expected 2");
		}
		Alternative presentCaseAlternative;
		if (nonterminalDefinition.getAlternatives().get(0).getExpansion().getElements().size() == 0) {
			presentCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else if (nonterminalDefinition.getAlternatives().get(1).getExpansion().getElements().size() == 0) {
			presentCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
		} else {
			throw new RuntimeException("could not find alternative with expansion length 0 as absent case for optional-styled nonterminal " + nonterminalName);
		}
		if (presentCaseAlternative.getExpansion().getElements().size() != 1) {
			throw new RuntimeException("could not recognize present case for optional-styled nonterminal " + nonterminalName);
		}
		return presentCaseAlternative.getExpansion().getElements().get(0);
	}


}
