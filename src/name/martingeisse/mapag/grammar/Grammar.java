package name.martingeisse.mapag.grammar;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public final class Grammar {

	private final String packageName;
	private final String className;
	private final ImmutableList<TerminalDefinition> terminalDefinitions;
	private final ImmutableList<NonterminalDefinition> nonterminalDefinitions;
	private final String startNonterminalName;
	private final ImmutableList<Production> productions;

	public Grammar(String packageName, String className, ImmutableList<TerminalDefinition> terminalDefinitions, ImmutableList<NonterminalDefinition> nonterminalDefinitions, String startNonterminalName, ImmutableList<Production> productions) {
		this.packageName = packageName;
		this.className = className;
		this.terminalDefinitions = terminalDefinitions;
		this.nonterminalDefinitions = nonterminalDefinitions;
		this.startNonterminalName = startNonterminalName;
		this.productions = productions;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public ImmutableList<TerminalDefinition> getTerminalDefinitions() {
		return terminalDefinitions;
	}

	public ImmutableList<NonterminalDefinition> getNonterminalDefinitions() {
		return nonterminalDefinitions;
	}

	public String getStartNonterminalName() {
		return startNonterminalName;
	}

	public ImmutableList<Production> getProductions() {
		return productions;
	}

}
