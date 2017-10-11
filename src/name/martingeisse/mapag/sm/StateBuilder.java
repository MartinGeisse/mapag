package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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

	public StateBuilder(GrammarInfo grammarInfo) {
		this.grammarInfo = ParameterUtil.ensureNotNull(grammarInfo, "grammarInfo");
	}

	public void addElementClosure(StateElement rootElement) {
		if (elements.add(rootElement)) {
			if (!rootElement.isAtEnd()) {
				String currentRightSideSymbol = rootElement.getNextSymbol();
				if (grammarInfo.getGrammar().isNonterminal(currentRightSideSymbol)) {
					ImmutableSet<String> localFollowSet = computeLocalFollowSet(rootElement);
					includeNonterminal(currentRightSideSymbol, localFollowSet);
				}
			}
		}
	}

	private ImmutableSet<String> computeLocalFollowSet(StateElement rootElement) {
		ImmutableList<String> expansion = rootElement.getAlternative().getExpansion();
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
			for (String followTerminal : localFollowSet) {
				addElementClosure(new StateElement(nonterminal, alternative, 0, followTerminal));
			}
		}
	}

	public State build() {
		return new State(elements);
	}

}
