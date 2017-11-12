package name.martingeisse.mapag.grammar.canonicalization;

import name.martingeisse.mapag.grammar.extended.expression.Expression;

import java.util.*;

/**
 *
 */
public final class SyntheticNonterminalNameGenerator {

	private final Set<String> knownSymbols = new HashSet<>();
	private final Map<String, Integer> syntheticNameCounters = new HashMap<>();
	private String syntheticNamePrefix;

	public void registerKnownSymbol(String symbol) {
		knownSymbols.add(symbol);
	}

	public void registerKnownSymbols(Collection<String> symbols) {
		knownSymbols.addAll(symbols);
	}

	public void prepare(String nonterminalName, String alternativeName) {
		if (alternativeName == null) {
			this.syntheticNamePrefix = nonterminalName + "/";
		} else {
			this.syntheticNamePrefix = nonterminalName + '/' + alternativeName + '/';
		}
	}

	// TODO test name collision resolution
	public String createSyntheticName(Expression expression) {
		if (expression.getName() != null) {
			String syntheticName = syntheticNamePrefix + expression.getName();
			if (knownSymbols.add(syntheticName)) {
				return syntheticName;
			}
		}
		String prefix = syntheticNamePrefix;
		if (expression.getName() != null) {
			prefix = prefix + expression.getName() + '/';
		}
		while (true) {
			int syntheticNameCounter = syntheticNameCounters.getOrDefault(prefix, 0);
			syntheticNameCounter++;
			syntheticNameCounters.put(prefix, syntheticNameCounter);
			String syntheticName = prefix + syntheticNameCounter;
			if (knownSymbols.add(syntheticName)) {
				return syntheticName;
			}
		}
	}

}
