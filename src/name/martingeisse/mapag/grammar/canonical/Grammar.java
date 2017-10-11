package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.util.ParameterUtil;

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
		this.packageName = ParameterUtil.ensureNotNullOrEmpty(packageName, "packageName");
		this.className = ParameterUtil.ensureNotNullOrEmpty(className, "className");
		this.terminalDefinitions = ParameterUtil.ensureNotNull(terminalDefinitions, "terminalDefinitions");
		ParameterUtil.ensureNoNullOrEmptyElement(terminalDefinitions.keySet(), "terminalDefinitions.keySet()");
		ParameterUtil.ensureNoNullElement(terminalDefinitions.values(), "terminalDefinitions.values()");
		this.nonterminalDefinitions = ParameterUtil.ensureNotNull(nonterminalDefinitions, "nonterminalDefinitions");
		ParameterUtil.ensureNoNullOrEmptyElement(nonterminalDefinitions.keySet(), "nonterminalDefinitions.keySet()");
		ParameterUtil.ensureNoNullElement(nonterminalDefinitions.values(), "nonterminalDefinitions.values()");
		this.startNonterminalName = ParameterUtil.ensureNotNullOrEmpty(startNonterminalName, "startNonterminalName");
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
		ParameterUtil.ensureNotNull(name, "name");
		return terminalDefinitions.containsKey(name);
	}

	public boolean sentenceContainsTerminals(List<String> sentence) {
		ParameterUtil.ensureNotNull(sentence, "sentence");
		for (String symbol : sentence) {
			ParameterUtil.ensureNotNull(symbol, "sentence element");
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
		ParameterUtil.ensureNotNull(name, "name");
		return nonterminalDefinitions.containsKey(name);
	}

	public boolean sentenceContainsNonterminals(List<String> sentence) {
		ParameterUtil.ensureNotNull(sentence, "sentence");
		for (String symbol : sentence) {
			ParameterUtil.ensureNotNull(symbol, "sentence element");
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
