package name.martingeisse.mapag.codegen;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.Action;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.util.Comparators;
import name.martingeisse.mapag.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Note: this encoder ignores the original order of things in the grammar, so simple re-ordering does not change the
 * output. This excludes precedence, of course, since the order is significant here.
 */
public final class StateMachineEncoder {

	private final ImmutableList<String> terminals;
	private final ImmutableList<String> nonterminals;
	private final ImmutableList<State> states;
	private final ImmutableList<Pair<String, Alternative>> alternatives;

	public StateMachineEncoder(GrammarInfo grammarInfo, StateMachine stateMachine) {
		this.terminals = list(grammarInfo.getGrammar().getTerminalDefinitions().keySet());
		this.nonterminals = list(grammarInfo.getGrammar().getNonterminalDefinitions().keySet());
		this.states = list(stateMachine.getStates(), Comparators.stateComparator);

		List<Pair<String, Alternative>> alternatives = new ArrayList<>();
		for (NonterminalDefinition nonterminalDefinition : grammarInfo.getGrammar().getNonterminalDefinitions().values()) {
			for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
				alternatives.add(new Pair<>(nonterminalDefinition.getName(), alternative));
			}
		}
		this.alternatives = ImmutableList.copyOf(alternatives);
	}

	private <T> ImmutableList<T> list(Collection<T> input, Comparator<T> comparator) {
		List<T> list = new ArrayList<>(input);
		list.sort(comparator);
		return ImmutableList.copyOf(list);
	}

	private <T, C extends Comparable<C>> ImmutableList<T> list(Collection<T> input, Function<T, C> mapper) {
		return list(input, Comparator.comparing(mapper));
	}

	private <T extends Comparable<T>> ImmutableList<T> list(Collection<T> input) {
		return list(input, (Comparator<T>) null);
	}

	public int getTerminalIndex(String terminal) {
		int index = terminals.indexOf(terminal);
		if (index < 0) {
			throw new IllegalArgumentException("unknown terminal: " + terminal);
		}
		return index;
	}

	public int getNonterminalIndex(String nonterminal) {
		int index = nonterminals.indexOf(nonterminal);
		if (index < 0) {
			throw new IllegalArgumentException("unknown nonterminal: " + nonterminal);
		}
		return index;
	}

	public int getSymbolIndex(String symbol) {
		if (symbol.equals(SpecialSymbols.EOF_SYMBOL_NAME)) {
			return 0;
		}
		if (symbol.equals(SpecialSymbols.ERROR_SYMBOL_NAME)) {
			return 1;
		}
		int index = terminals.indexOf(symbol);
		if (index >= 0) {
			return 2 + index;
		}
		index = nonterminals.indexOf(symbol);
		if (index >= 0) {
			return 2 + terminals.size() + index;
		}
		throw new IllegalArgumentException("unknown symbol: " + symbol);
	}

	public int getStateIndex(State state) {
		int index = states.indexOf(state);
		if (index < 0) {
			throw new IllegalArgumentException("unknown state: " + state);
		}
		return index;
	}

	public int getAlternativeIndex(String nonterminal, Alternative alternative) {
		int index = alternatives.indexOf(new Pair<>(nonterminal, alternative));
		if (index < 0) {
			throw new IllegalArgumentException("unknown alternative for nonterminal " + nonterminal + ": " + alternative);
		}
		return index;
	}

	public int getShiftActionCode(Action.Shift shift) {
		return getStateIndex(shift.getNextState()) + 1;
	}

	public int getReduceActionCode(Action.Reduce reduce) {
		return -1 - getAlternativeIndex(reduce.getNonterminal(), reduce.getAlternative());
	}

	public int getAcceptActionCode() {
		return Integer.MIN_VALUE;
	}

	public int getErrorActionCode() {
		return 0;
	}

	public int getActionCode(Action action) {
		if (action == null) {
			return getErrorActionCode();
		} else if (action instanceof Action.Shift) {
			return getShiftActionCode((Action.Shift) action);
		} else if (action instanceof Action.Reduce) {
			return getReduceActionCode((Action.Reduce) action);
		} else if (action instanceof Action.Accept) {
			return getAcceptActionCode();
		} else {
			throw new IllegalArgumentException("unknown action: " + action);
		}
	}

	public void dump() {

		System.out.println("special symbols: ");
		System.out.println("0: " + SpecialSymbols.EOF_SYMBOL_NAME);
		System.out.println("1: " + SpecialSymbols.ERROR_SYMBOL_NAME);
		System.out.println();

		int symbolCode = 2;
		System.out.println("terminals: ");
		for (String terminal : terminals) {
			System.out.println(symbolCode + ": " + terminal);
			symbolCode++;
		}
		System.out.println();

		System.out.println("nonterminals: ");
		for (String nonterminal : nonterminals) {
			System.out.println(symbolCode + ": " + nonterminal);
			symbolCode++;
		}
		System.out.println();

		int alternativeCode = 0;
		System.out.println("alternatives: ");
		for (Pair<String, Alternative> entry : alternatives) {
			System.out.println(alternativeCode + ": " + entry.getLeft() + " ::= " + entry.getRight());
			alternativeCode++;
		}
		System.out.println();

		System.out.println("----------------------------------------------------------------------");
		int stateCode = 0;
		for (State state : states) {
			System.out.println();
			System.out.println("state " + stateCode + ": ");
			System.out.println(state);
			stateCode++;
		}

	}

}
