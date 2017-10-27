package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.Associativity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class GrammarBuilder {

	private String packageName;
	private String className;
	private List<TerminalDefinition> terminalDefinitions = new ArrayList<>();
	private Map<String, List<Alternative>> nonterminalDefinitions = new HashMap<>();
	private String startNonterminalName;

	public String getPackageName() {
		return packageName;
	}

	public GrammarBuilder setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public String getClassName() {
		return className;
	}

	public GrammarBuilder setClassName(String className) {
		this.className = className;
		return this;
	}

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

		public ProductionBuilder addAlternative(String... expansion) {
			alternatives.add(new Alternative(ImmutableList.copyOf(expansion), null));
			return this;
		}

		public ProductionBuilder addAlternativeWithPrecedence(String effectivePrecedenceTerminal, String... expansion) {
			alternatives.add(new Alternative(ImmutableList.copyOf(expansion), effectivePrecedenceTerminal));
			return this;
		}

	}

	public Grammar build() {
		List<NonterminalDefinition> nonterminalDefinitions = new ArrayList<>();
		for (Map.Entry<String, List<Alternative>> entry : this.nonterminalDefinitions.entrySet()) {
			nonterminalDefinitions.add(new NonterminalDefinition(entry.getKey(), ImmutableList.copyOf(entry.getValue())));
		}
		return new Grammar(packageName, className, terminalDefinitions, nonterminalDefinitions, startNonterminalName);
	}
}