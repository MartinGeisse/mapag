package name.martingeisse.mapag.grammar.canonicalization;

import java.util.*;

/**
 *
 */
public final class SyntheticNonterminalNameGenerator {

	private final Set<String> knownSymbols = new HashSet<>();
	private final Map<String, Integer> syntheticNameCounters = new HashMap<>();
	private String syntheticNamePrefix;
	private final Stack<String> savedSyntheticNamePrefixes = new Stack<>();

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

	public void save() {
		savedSyntheticNamePrefixes.push(syntheticNamePrefix);
	}

	public void restore() throws EmptyStackException {
		syntheticNamePrefix = savedSyntheticNamePrefixes.pop();
	}

	public void absolute() {
		syntheticNamePrefix = "";
	}

	public void extend(String extension) {
		if (extension == null) {
			throw new IllegalArgumentException("extension cannot be null");
		}
		syntheticNamePrefix = syntheticNamePrefix + extension + '/';
	}

	public String createSyntheticName(String suggestedName) {
		if (suggestedName != null) {
			String syntheticName = syntheticNamePrefix + suggestedName;
			if (knownSymbols.add(syntheticName)) {
				return syntheticName;
			}
		}
		String prefix = syntheticNamePrefix;
		if (suggestedName != null) {
			prefix = prefix + suggestedName + '/';
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
