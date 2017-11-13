package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalAnnotation;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;

/**
 *
 */
public class PsiTypeMapper {

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;

	public PsiTypeMapper(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
	}

	public String getEffectiveTypeForSymbol(String symbol) {
		if (grammar.getTerminalDefinitions().get(symbol) != null) {
			return "LeafPsiElement";
		} else if (grammar.getNonterminalDefinitions().get(symbol) != null) {
			return getEffectiveTypeForNonterminal(symbol);
		} else {
			throw new RuntimeException("unknown symbol: " + symbol);
		}
	}

	public String getEffectiveTypeForNonterminal(String nonterminal) {
		NonterminalDefinition nonterminalDefinition = grammar.getNonterminalDefinitions().get(nonterminal);
		NonterminalAnnotation.PsiStyle psiStyle = nonterminalDefinition.getAnnotation().getPsiStyle();
		switch (psiStyle) {

			case NORMAL:
				return getGeneratedTypeForNonterminal(nonterminal);

			case OPTIONAL:
				// TODO
				return getGeneratedTypeForNonterminal(nonterminal);

			case ZERO_OR_MORE:
				return getCollectionTypeForNonterminal(nonterminalDefinition, true);

			case ONE_OR_MORE:
				return getCollectionTypeForNonterminal(nonterminalDefinition, false);

			default:
				return getGeneratedTypeForNonterminal(nonterminal);

		}
	}

	private String getCollectionTypeForNonterminal(NonterminalDefinition nonterminalDefinition, boolean zeroAllowed) {
		String nonterminalName = nonterminalDefinition.getName();
		if (nonterminalDefinition.getAlternatives().size() != 2) {
			throw new RuntimeException("collection-type nonterminal " + nonterminalName + " has " +
				nonterminalDefinition.getAlternatives().size() + " alternatives, expected 2");
		}
		Alternative baseCaseAlternative, repetitionCaseAlternative;
		if (nonterminalDefinition.getAlternatives().get(0).getExpansion().size() == 2) {
			repetitionCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			baseCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else if (nonterminalDefinition.getAlternatives().get(1).getExpansion().size() == 2) {
			baseCaseAlternative = nonterminalDefinition.getAlternatives().get(0);
			repetitionCaseAlternative = nonterminalDefinition.getAlternatives().get(1);
		} else {
			throw new RuntimeException("could not find alternative with expansion length 2 as repetition case for repetition-type nonterminal " + nonterminalName);
		}
		if (baseCaseAlternative.getExpansion().size() != (zeroAllowed ? 0 : 1)) {
			throw new RuntimeException("could not recognize base case for repetition-type nonterminal " + nonterminalName);
		}
		if (!repetitionCaseAlternative.getExpansion().get(0).equals(nonterminalName)) {
			throw new RuntimeException("could not find left-recursion for repetition-type nonterminal " + nonterminalName);
		}
		String elementSymbol = repetitionCaseAlternative.getExpansion().get(1);
		if (!zeroAllowed && !baseCaseAlternative.getExpansion().get(0).equals(elementSymbol)) {
			throw new RuntimeException("base-case uses different element symbol than repetition case for nonterminal " + elementSymbol);
		}
		String elementType = getEffectiveTypeForSymbol(elementSymbol);
		return "List<" + elementType + ">";
	}

	public String getGeneratedTypeForNonterminal(String nonterminal) {
		return toIdentifier(nonterminal, true);
	}

	public String toIdentifier(String s, boolean firstCharacterUppercase) {
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
