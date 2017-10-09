package name.martingeisse.mapag.grammar;

import java.util.List;

/**
 *
 */
public final class Grammar {

	private final String packageName;
	private final String className;
	private final List<TerminalDefinition> terminalDefinitions;
	private final List<NonterminalDefinition> nonterminalDefinitions;
	private final String startNonterminalName;
	private final List<Production> productions;

	public Grammar(String packageName, String className, List<TerminalDefinition> terminalDefinitions, List<NonterminalDefinition> nonterminalDefinitions, String startNonterminalName, List<Production> productions) {
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

	public List<TerminalDefinition> getTerminalDefinitions() {
		return terminalDefinitions;
	}

	public List<NonterminalDefinition> getNonterminalDefinitions() {
		return nonterminalDefinitions;
	}

	public String getStartNonterminalName() {
		return startNonterminalName;
	}

	public List<Production> getProductions() {
		return productions;
	}

}
