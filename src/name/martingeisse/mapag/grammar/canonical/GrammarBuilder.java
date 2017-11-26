package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class GrammarBuilder {

	private List<TerminalDefinition> terminalDefinitions = new ArrayList<>();
	private Map<String, List<Alternative>> nonterminalDefinitions = new HashMap<>();
	private String startNonterminalName;

	public String getStartNonterminalName() {
		return startNonterminalName;
	}

	public GrammarBuilder setStartNonterminalName(String startNonterminalName) {
		this.startNonterminalName = startNonterminalName;
		return this;
	}

	public GrammarBuilder addTerminals(String... names) {
		addTerminals(null, Associativity.NONASSOC, names);
		return this;
	}

	public GrammarBuilder addTerminals(Integer precedenceIndex, Associativity associativity, String... names) {
		for (String name : names) {
			terminalDefinitions.add(new TerminalDefinition(name, precedenceIndex, associativity));
		}
		return this;
	}

	public ProductionBuilder createNonterminal(String name) {
		List<Alternative> alternatives = new ArrayList<>();
		nonterminalDefinitions.put(name, alternatives);
		return new ProductionBuilder(alternatives);
	}

	public static final class ProductionBuilder {

		private final List<Alternative> alternatives;

		public ProductionBuilder(List<Alternative> alternatives) {
			this.alternatives = alternatives;
		}

		public ProductionBuilder addAlternative(String... expansionSymbols) {
			alternatives.add(new Alternative("a" + alternatives.size(), buildExpansion(expansionSymbols), null, false));
			return this;
		}

		public ProductionBuilder addAlternativeWithPrecedence(String effectivePrecedenceTerminal, String... expansionSymbols) {
			AlternativeConflictResolver conflictResolver = new AlternativeConflictResolver(effectivePrecedenceTerminal, null);
			alternatives.add(new Alternative("a" + alternatives.size(), buildExpansion(expansionSymbols), conflictResolver, false));
			return this;
		}

		public ProductionBuilder addAlternativeWithResolutionMap(ImmutableMap<String, ConflictResolution> conflictResolutionMap, String... expansionSymbols) {
			AlternativeConflictResolver conflictResolver = new AlternativeConflictResolver(null, conflictResolutionMap);
			alternatives.add(new Alternative("a" + alternatives.size(), buildExpansion(expansionSymbols), conflictResolver, false));
			return this;
		}

		private Expansion buildExpansion(String... symbols) {
			List<ExpansionElement> elements = new ArrayList<>();
			for (String symbol : symbols) {
				elements.add(new ExpansionElement(symbol, null));
			}
			return new Expansion(ImmutableList.copyOf(elements));
		}

	}

	public Grammar build() {
		List<NonterminalDefinition> nonterminalDefinitions = new ArrayList<>();
		for (Map.Entry<String, List<Alternative>> entry : this.nonterminalDefinitions.entrySet()) {
			nonterminalDefinitions.add(new NonterminalDefinition(entry.getKey(), ImmutableList.copyOf(entry.getValue()), NonterminalDefinition.PsiStyle.NORMAL));
		}
		return new Grammar(terminalDefinitions, nonterminalDefinitions, startNonterminalName);
	}
}
