package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode()
 * are those of class Object, i.e. based on object identity. The reason is that even if this were a value object,
 * different parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't
 * really a situation where two distinct but equal instances of this class would exist. Instances are created from
 * the grammar file and anything that appears in different places in this file is not equal in any meaningful sense.
 */
public final class Grammar {

	private final ImmutableMap<String, TerminalDefinition> terminalDefinitions;
	private final ImmutableMap<String, NonterminalDefinition> nonterminalDefinitions;
	private final String startNonterminalName;

	public Grammar(Collection<TerminalDefinition> terminalDefinitions, Collection<NonterminalDefinition> nonterminalDefinitions, String startNonterminalName) {

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
