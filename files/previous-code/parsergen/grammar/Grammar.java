package name.martingeisse.parsergen.grammar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public final class Grammar {

	private final ImmutableSet<Terminal> alphabet;
	private final Nonterminal startSymbol;
	private final ImmutableList<Rule> rules;

	public Grammar(ImmutableSet<Terminal> alphabet, Nonterminal startSymbol, ImmutableList<Rule> rules) {
		this.alphabet = alphabet;
		this.startSymbol = startSymbol;
		this.rules = rules;
		{
			Set nonterminals = new HashSet();
			for (Rule rule : rules) {
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

	public ImmutableList<Rule> getRules() {
		return rules;
	}

	public Grammar vanishNonterminal(Nonterminal nonterminalToVanish) {
		List<Rule> modifiedRules = new ArrayList<>();
		for (Rule rule : rules) {
			if (!rule.getNonterminal().equals(nonterminalToVanish)) {
				modifiedRules.add(rule.vanishNonterminal(nonterminalToVanish));
			}
		}
		return new Grammar(alphabet, startSymbol, ImmutableList.copyOf(modifiedRules));
	}

	public Rule getRuleFor(Nonterminal nonterminal) {
		for (Rule rule : rules) {
			if (rule.getNonterminal().equals(nonterminal)) {
				return rule;
			}
		}
		return null;
	}

}
