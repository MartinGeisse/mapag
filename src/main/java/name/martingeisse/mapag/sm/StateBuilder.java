package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
final class StateBuilder {

	private final GrammarInfo grammarInfo;
	private final Set<StateElement> elements = new HashSet<>();
	private final StateMachineBuildingCache cache;

	public StateBuilder(GrammarInfo grammarInfo, StateMachineBuildingCache cache) {
		this.grammarInfo = ParameterUtil.ensureNotNull(grammarInfo, "grammarInfo");
		this.cache = cache;
	}

	public StateBuilder addElementClosure(StateElement rootElement) {
		ParameterUtil.ensureNotNull(rootElement, "rootElement");
		if (elements.add(rootElement)) {
			if (!rootElement.isAtEnd()) {
				String currentRightSideSymbol = rootElement.getNextSymbol();
				if (grammarInfo.getGrammar().isNonterminal(currentRightSideSymbol)) {
					ImmutableSet<String> localFollowSet = computeLocalFollowSet(rootElement);
					includeNonterminal(currentRightSideSymbol, localFollowSet);
				}
			}
		}
		return this;
	}

	private ImmutableSet<String> computeLocalFollowSet(StateElement rootElement) {
		ImmutableList<String> expansion = rootElement.getAlternative().getExpansion().getSymbols();
		ImmutableList<String> followSentence = expansion.subList(rootElement.getPosition() + 1, expansion.size());
		ImmutableSet<String> localFollowSet = grammarInfo.determineFirstSetForSentence(followSentence);
		if (grammarInfo.isSentenceVanishable(followSentence)) {
			Set<String> temp = new HashSet<>(localFollowSet);
			temp.add(rootElement.getFollowTerminal());
			return ImmutableSet.copyOf(temp);
		} else {
			return localFollowSet;
		}
	}

	private void includeNonterminal(String nonterminal, ImmutableSet<String> localFollowSet) {
		NonterminalDefinition nonterminalDefinition = grammarInfo.getGrammar().getNonterminalDefinitions().get(nonterminal);
		for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
			if (alternative.getAttributes().isReduceOnEofOnly()) {
				if (localFollowSet.contains(SpecialSymbols.EOF_SYMBOL_NAME)) {
					addElementClosure(cache.buildStartingStateElement(nonterminal, alternative, SpecialSymbols.EOF_SYMBOL_NAME));
				}
			} else {
				for (String followTerminal : localFollowSet) {
					addElementClosure(cache.buildStartingStateElement(nonterminal, alternative, followTerminal));
				}
			}
		}
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public State build() {
		if (elements.isEmpty()) {
			throw new IllegalStateException("no state elements have been added");
		}
		return new State(ImmutableSet.copyOf(elements));
	}

}
