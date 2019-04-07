package name.martingeisse.mapag.codegen.highlevel;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateElement;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Generates, for each state, a string describing the next expected input. This is used for error messages. The expected
 * input is a description of the terminals and nonterminals that can be consumed in that state, with some removal of
 * redundant symbols.
 * <p>
 * To work correctly, the code generation properties must contain a readable name for each "significant" nonterminal.
 * That name will be used for the error message, and symbols from the content start of that nonterminal are suppressed.
 * Non-significant nonterminals will not be listed -- their content start is listed instead.
 * <p>
 * In the above description, "content start" is somewhat similar to the first-set of a nonterminal, but also includes
 * nonterminals.
 * <p>
 * As a special case, if a symbol appears both as content of a significant nonterminal and as other content, that symbol
 * is still listed -- it is only suppressed as part of the significant nonterminal's content, not in its other role.
 * <p>
 * Another special case is nonterminals that can be expanded to the empty string. If such a nonterminal appears as
 * possible input, the content following that nonterminal is included too.
 * <p>
 * A third special case is a significant nonterminal that starts / can start with another significant nonterminal,
 * possibly itself. In that case, the "sub-nonterminal" is not included from that role (but may be included from
 * different roles). While this may seem to cause problems with recursively defined nonterminals, it doesn't: A state
 * element from a recursively defined nonterminal is only part of the current state if one of the nonterminals from the
 * recursion loop appears somewhere outside that loop, thus it is included in the error message from there.
 * <p>
 * The documentation produced by this class does not correspond direclty to the set of possible transitions if one or
 * more alternatices are marked with %reduceOnError. The additional transitions produced by that keyword are part
 * of the run-time state machine, but they are not documented since they are an error mitigating mechanism.
 */
public class StateInputExpectationBuilder {

	private final GrammarInfo grammarInfo;
	private final StateMachine stateMachine;
	private final Map<String, String> symbolNaming;
	private Map<State, String> expectations;

	public StateInputExpectationBuilder(GrammarInfo grammarInfo, StateMachine stateMachine, Map<String, String> symbolNaming) {
		this.grammarInfo = grammarInfo;
		this.stateMachine = stateMachine;
		this.symbolNaming = symbolNaming;
	}

	public Map<State, String> getExpectations() {
		return expectations;
	}

	public void build() {
		expectations = new HashMap<>();
		for (State state : stateMachine.getStates()) {
			expectations.put(state, build(state));
		}
	}

	//
	// main expectation building logic
	//

	private String build(State state) {
		Set<StateElement> elements = new HashSet<>(state.getElements());
		closeUnderEpsilonSkip(elements);
		Set<String> allSignificantNonterminals = determineSignificantNonterminalsInStateElements(elements);
		removeContentStartForSignificantNonterminals(elements, allSignificantNonterminals);
		Set<String> nonRedundantSignificantNonterminals = determineSignificantNonterminalsInStateElements(elements);
		Set<String> nonRedundantAllowedTerminals = getAllowedTerminals(elements);
		return buildDescription(nonRedundantSignificantNonterminals, nonRedundantAllowedTerminals);
	}

	//
	// epsilon-skip closure
	//

	private void closeUnderEpsilonSkip(Set<StateElement> elements) {
		closeUnderEpsilonSkip(elements, ImmutableSet.copyOf(elements));
	}

	private void closeUnderEpsilonSkip(Set<StateElement> elements, Set<StateElement> uncheckedElements) {
		Set<StateElement> newElements = new HashSet<>();
		for (StateElement element : uncheckedElements) {
			if (!element.isAtEnd()) { // actually redundant because the element's follow-terminal is never a vanishable nonterminal
				if (grammarInfo.getVanishableNonterminals().contains(element.getNextSymbol())) {
					newElements.add(element.getShifted());
				}
			}
		}
		if (newElements.isEmpty()) {
			return;
		}
		elements.addAll(newElements);
		closeUnderEpsilonSkip(elements, newElements);
	}

	//
	// detection of signigicant nonterminals (including redundant ones) and removing their content start
	//

	private Set<String> determineSignificantNonterminalsInStateElements(Set<StateElement> elements) {
		Set<String> result = new HashSet<>();
		for (StateElement element : elements) {
			String symbol = element.getNextSymbol();
			if (grammarInfo.getGrammar().getNonterminalDefinitions().keySet().contains(symbol) && symbolNaming.containsKey(symbol)) {
				result.add(symbol);
			}
		}
		return result;
	}

	private void removeContentStartForSignificantNonterminals(Set<StateElement> elements, Set<String> allSignificantNonterminals) {
		elements.removeIf(element -> allSignificantNonterminals.contains(element.getLeftSide()) && element.getPosition() == 0);
	}

	//
	// detection of consumable terminals
	//

	private Set<String> getAllowedTerminals(Set<StateElement> elements) {
		Set<String> result = new HashSet<>();
		for (StateElement element : elements) {
			String symbol = element.getNextSymbol();
			if (grammarInfo.getGrammar().getTerminalDefinitions().get(symbol) != null) {
				result.add(symbol);
			}
		}
		return result;
	}

	//
	// output string formatting
	//

	private String buildDescription(Set<String> nonterminals, Set<String> terminals) {
		List<String> nonterminalList = convertSymbolsToDisplayNames(nonterminals);
		nonterminalList.sort(null);
		List<String> terminalList = convertSymbolsToDisplayNames(terminals);
		terminalList.sort(null);
		List<String> allList = new ArrayList<>(nonterminalList);
		allList.addAll(terminalList);
		return StringUtils.join(allList, ' ');
	}

	private List<String> convertSymbolsToDisplayNames(Collection<String> symbols) {
		List<String> list = new ArrayList<>();
		for (String symbol : symbols) {
			String displayName = symbolNaming.get(symbol);
			list.add(displayName == null ? symbol : displayName);
		}
		return list;
	}

}
