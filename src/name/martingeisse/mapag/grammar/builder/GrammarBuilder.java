package name.martingeisse.mapag.grammar.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public final class GrammarBuilder {

	private final ImmutableSet<Terminal> alphabet;
	private final Nonterminal startSymbol;
	private final ImmutableList<RuleBuilder> rules;

	public GrammarBuilder(ImmutableSet<Terminal> alphabet, Nonterminal startSymbol, ImmutableList<RuleBuilder> rules) {
		this.alphabet = alphabet;
		this.startSymbol = startSymbol;
		this.rules = rules;
		{
			Set nonterminals = new HashSet();
			for (RuleBuilder rule : rules) {
				if (!nonterminals.add(rule.getNonterminal())) {
					throw new IllegalArgumentException("argument rule list contains duplicates for nonterminal " + rule.getNonterminal());
				}
			}
		}
	}

	public ImmutableSet<Terminal> getAlphabet() {
		return alphabet;
	}

	public Nonterminal getStartSymbol() {
		return startSymbol;
	}

	public ImmutableList<RuleBuilder> getRules() {
		return rules;
	}

	public GrammarBuilder vanishNonterminal(Nonterminal nonterminalToVanish) {
		List<RuleBuilder> modifiedRules = new ArrayList<>();
		for (RuleBuilder rule : rules) {
			if (!rule.getNonterminal().equals(nonterminalToVanish)) {
				modifiedRules.add(rule.vanishNonterminal(nonterminalToVanish));
			}
		}
		return new GrammarBuilder(alphabet, startSymbol, ImmutableList.copyOf(modifiedRules));
	}

	public RuleBuilder getRuleFor(Nonterminal nonterminal) {
		for (RuleBuilder rule : rules) {
			if (rule.getNonterminal().equals(nonterminal)) {
				return rule;
			}
		}
		return null;
	}

}
