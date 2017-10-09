package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.parsergen.grammar.*;
import name.martingeisse.parsergen.grammar.info.GrammarInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
final class StateBuilder {

	private final GrammarInfo grammarInfo;
	private final Set<StateElement> elements = new HashSet<>();

	public StateBuilder(GrammarInfo grammarInfo) {
		this.grammarInfo = grammarInfo;
	}

	public void addElementClosure(StateElement rootElement) {
		if (elements.add(rootElement)) {
			ImmutableList<Symbol> remainingRightSide = rootElement.getRemainingRightSide();
			if (!remainingRightSide.isEmpty()) {
				Symbol currentRightSideSymbol = remainingRightSide.get(0);
				if (currentRightSideSymbol instanceof Nonterminal) {
					Nonterminal currentRightSideNonterminal = (Nonterminal)currentRightSideSymbol;

					List<Symbol> furtherRightSideSymbols = new ArrayList<>(remainingRightSide);
					furtherRightSideSymbols.remove(0);

					Set<Terminal> localFollowSet;
					localFollowSet = grammarInfo.determineFirstSetForSentence(furtherRightSideSymbols);
					if (grammarInfo.isSentenceVanishable(furtherRightSideSymbols)) {
						localFollowSet = new HashSet<>(localFollowSet);
						localFollowSet.add(rootElement.getFollowTerminal());
					}

					Rule rule = grammarInfo.getGrammar().getRuleFor(currentRightSideNonterminal);
					for (Alternative alternative : rule.getAlternatives()) {
						for (Terminal followTerminal : localFollowSet) {
							addElementClosure(new StateElement(currentRightSideNonterminal, alternative.getExpansionSymbols(), followTerminal));
						}
					}

				}
			}
		}
	}

	public State build() {
		return new State(ImmutableSet.copyOf(elements));
	}

}
