package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class Grammar {

	private final String packageName;
	private final String className;
	private final ImmutableMap<String, TerminalDefinition> terminalDefinitions;
	private final ImmutableMap<String, NonterminalDefinition> nonterminalDefinitions;
	private final String startNonterminalName;

	public Grammar(String packageName, String className, Collection<TerminalDefinition> terminalDefinitions, Collection<NonterminalDefinition> nonterminalDefinitions, String startNonterminalName) {

		this.packageName = ParameterUtil.ensureNotNull(packageName, "packageName");
		this.className = ParameterUtil.ensureNotNullOrEmpty(className, "className");

		ParameterUtil.ensureNotNull(terminalDefinitions, "terminalDefinitions");
		ParameterUtil.ensureNoNullElement(terminalDefinitions, "terminalDefinitions");
		this.terminalDefinitions = mapByName(terminalDefinitions);

		ParameterUtil.ensureNotNull(nonterminalDefinitions, "nonterminalDefinitions");
		ParameterUtil.ensureNoNullElement(nonterminalDefinitions, "nonterminalDefinitions");
		this.nonterminalDefinitions = mapByName(nonterminalDefinitions);

		this.startNonterminalName = ParameterUtil.ensureNotNullOrEmpty(startNonterminalName, "startNonterminalName");
	}

	private static <T extends SymbolDefinition> ImmutableMap<String, T> mapByName(Collection<T> collection) {
		Map<String, T> result = new HashMap<>();
		for (T element : collection) {
			if (result.put(element.getName(), element) != null) {
				throw new IllegalArgumentException("duplicate name: " + element.getName());
			}
		}
		return ImmutableMap.copyOf(result);
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
