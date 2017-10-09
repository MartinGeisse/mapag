package name.martingeisse.parsergen.grammar;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class Rule {

	private final Nonterminal nonterminal;
	private final ImmutableList<Alternative> alternatives;

	public Rule(Nonterminal nonterminal, ImmutableList<Alternative> alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = alternatives;
		if (alternatives.isEmpty()) {
			throw new IllegalArgumentException("trying to build a rule without alternatives");
		}
	}

	public Rule(Nonterminal nonterminal, List<Alternative> alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = ImmutableList.copyOf(alternatives);
		if (alternatives.isEmpty()) {
			throw new IllegalArgumentException("trying to build a rule without alternatives");
		}
	}

	public Rule(Nonterminal nonterminal, Alternative... alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = ImmutableList.copyOf(alternatives);
		if (alternatives.length == 0) {
			throw new IllegalArgumentException("trying to build a rule without alternatives");
		}
	}

	/**
	 * Builds a rule with a single alternative containing the specified expansion symbols in sequence.
	 */
	public static Rule fromSingleAlternative(Nonterminal nonterminal, Symbol... expansionSymbols) {
		return new Rule(nonterminal, ImmutableList.of(new Alternative(expansionSymbols)));
	}

	/**
	 * Builds a rule with multiple alternatives, all containing a single symbol. Can be used e.g. for the rule for
	 * an "operator" nonterminal.
	 */
	public static Rule fromSingleSymbolAlternatives(Nonterminal nonterminal, Symbol... expansionSymbols) {
		List<Alternative> alternatives = new ArrayList<>();
		for (Symbol expansionSymbol : expansionSymbols) {
			alternatives.add(new Alternative(expansionSymbol));
		}
		return new Rule(nonterminal, alternatives);
	}

	public Nonterminal getNonterminal() {
		return nonterminal;
	}

	public ImmutableList<Alternative> getAlternatives() {
		return alternatives;
	}

	public boolean isImmediatelyVanishable() {
		for (Alternative alternative : alternatives) {
			if (alternative.getExpansionSymbols().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public Rule vanishNonterminal(Nonterminal nonterminalToVanish) {
		if (nonterminalToVanish.equals(this.nonterminal)) {
			throw new IllegalArgumentException("cannot vanish a nonterminal from its own rule");
		}
		List<Alternative> modifiedAlternatives = new ArrayList<>();
		for (Alternative alternative : this.alternatives) {
			modifiedAlternatives.add(alternative.vanishNonterminal(nonterminalToVanish));
		}
		return new Rule(this.nonterminal, modifiedAlternatives);
	}

}
