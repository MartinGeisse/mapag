package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;

import java.util.List;

/**
 *
 */
public final class Grammar {

	private final String packageName;
	private final String className;
	private final ImmutableMap<String, TerminalDefinition> terminalDefinitions;
	private final ImmutableMap<String, NonterminalDefinition> nonterminalDefinitions;
	private final String startNonterminalName;

	public Grammar(String packageName, String className, ImmutableMap<String, TerminalDefinition> terminalDefinitions, ImmutableMap<String, NonterminalDefinition> nonterminalDefinitions, String startNonterminalName) {
		this.packageName = packageName;
		this.className = className;
		this.terminalDefinitions = terminalDefinitions;
		this.nonterminalDefinitions = nonterminalDefinitions;
		this.startNonterminalName = startNonterminalName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public ImmutableMap<String, TerminalDefinition> getTerminalDefinitions() {
		return terminalDefinitions;
	}
	public boolean isTerminal(String name) {
		return terminalDefinitions.containsKey(name);
	}

	public boolean sentenceContainsTerminals(List<String> sentence) {
		for (String symbol : sentence) {
			if (isTerminal(symbol)) {
				return true;
			}
		}
		return false;
	}

	public ImmutableMap<String, NonterminalDefinition> getNonterminalDefinitions() {
		return nonterminalDefinitions;
	}

	public boolean isNonterminal(String name) {
		return nonterminalDefinitions.containsKey(name);
	}

	public boolean sentenceContainsNonterminals(List<String> sentence) {
		for (String symbol : sentence) {
			if (isNonterminal(symbol)) {
				return true;
			}
		}
		return false;
	}

	public String getStartNonterminalName() {
		return startNonterminalName;
	}

}
