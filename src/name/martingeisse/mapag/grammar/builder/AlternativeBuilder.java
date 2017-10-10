package name.martingeisse.mapag.grammar.builder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class AlternativeBuilder {

	private final List<Symbol> expansionSymbols;

	public AlternativeBuilder(List<Symbol> expansionSymbols) {
		this.expansionSymbols = expansionSymbols;
	}

	public AlternativeBuilder(Symbol... expansionSymbols) {
		this(List.copyOf(expansionSymbols));
	}

	public List<Symbol> getExpansionSymbols() {
		return expansionSymbols;
	}

	public boolean expansionContainsTerminals() {
		for (Symbol symbol : expansionSymbols) {
			if (symbol instanceof Terminal) {
				return true;
			}
		}
		return false;
	}

	public boolean expansionContainsNonterminals() {
		for (Symbol symbol : expansionSymbols) {
			if (symbol instanceof Nonterminal) {
				return true;
			}
		}
		return false;
	}

	public AlternativeBuilder vanishNonterminal(Nonterminal nonterminal) {
		List<Symbol> filteredSymbols = new ArrayList<>();
		for (Symbol symbol : expansionSymbols) {
			if (!symbol.equals(nonterminal)) {
				filteredSymbols.add(symbol);
			}
		}
		return new AlternativeBuilder(filteredSymbols);
	}

}
