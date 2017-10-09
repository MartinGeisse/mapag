package name.martingeisse.mapag.grammar.builder;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class RuleBuilder {

	private final Nonterminal nonterminal;
	private final ImmutableList<AlternativeBuilder> alternatives;

	public RuleBuilder(Nonterminal nonterminal, ImmutableList<AlternativeBuilder> alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = alternatives;
		if (alternatives.isEmpty()) {
			throw new IllegalArgumentException("trying to build a rule without alternatives");
		}
	}

	public RuleBuilder(Nonterminal nonterminal, List<AlternativeBuilder> alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = ImmutableList.copyOf(alternatives);
		if (alternatives.isEmpty()) {
			throw new IllegalArgumentException("trying to build a rule without alternatives");
		}
	}

	public RuleBuilder(Nonterminal nonterminal, AlternativeBuilder... alternatives) {
		this.nonterminal = nonterminal;
		this.alternatives = ImmutableList.copyOf(alternatives);
		if (alternatives.length == 0) {
			throw new IllegalArgumentException("trying to build a rule without alternatives");
		}
	}

	/**
	 * Builds a rule with a single alternative containing the specified expansion symbols in sequence.
	 */
	public static RuleBuilder fromSingleAlternative(Nonterminal nonterminal, Symbol... expansionSymbols) {
		return new RuleBuilder(nonterminal, ImmutableList.of(new AlternativeBuilder(expansionSymbols)));
	}

	/**
	 * Builds a rule with multiple alternatives, all containing a single symbol. Can be used e.g. for the rule for
	 * an "operator" nonterminal.
	 */
	public static RuleBuilder fromSingleSymbolAlternatives(Nonterminal nonterminal, Symbol... expansionSymbols) {
		List<AlternativeBuilder> alternatives = new ArrayList<>();
		for (Symbol expansionSymbol : expansionSymbols) {
			alternatives.add(new AlternativeBuilder(expansionSymbol));
		}
		return new RuleBuilder(nonterminal, alternatives);
	}

	public Nonterminal getNonterminal() {
		return nonterminal;
	}

	public ImmutableList<AlternativeBuilder> getAlternatives() {
		return alternatives;
	}

	public boolean isImmediatelyVanishable() {
		for (AlternativeBuilder alternative : alternatives) {
			if (alternative.getExpansionSymbols().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public RuleBuilder vanishNonterminal(Nonterminal nonterminalToVanish) {
		if (nonterminalToVanish.equals(this.nonterminal)) {
			throw new IllegalArgumentException("cannot vanish a nonterminal from its own rule");
		}
		List<AlternativeBuilder> modifiedAlternatives = new ArrayList<>();
		for (AlternativeBuilder alternative : this.alternatives) {
			modifiedAlternatives.add(alternative.vanishNonterminal(nonterminalToVanish));
		}
		return new RuleBuilder(this.nonterminal, modifiedAlternatives);
	}

}
