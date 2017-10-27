package name.martingeisse.mapag.codegen;

import com.google.common.collect.ImmutableList;
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
 *
 * TODO this class says "reduction index" -- is this the alternative index?
 */
public final class StateMachineEncoder {

	private final ImmutableList<String> terminals;
	private final ImmutableList<String> nonterminals;
	private final ImmutableList<State> states;
	private final ImmutableList<Pair<String, Alternative>> reductions;

	public StateMachineEncoder(GrammarInfo grammarInfo, StateMachine stateMachine) {
		this.terminals = list(grammarInfo.getGrammar().getTerminalDefinitions().keySet());
		this.nonterminals = list(grammarInfo.getGrammar().getNonterminalDefinitions().keySet());
		this.states = list(stateMachine.getStates(), Comparators.stateComparator);

		List<Pair<String, Alternative>> reductions = new ArrayList<>();
		for (NonterminalDefinition nonterminalDefinition : grammarInfo.getGrammar().getNonterminalDefinitions().values()) {
			for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
				reductions.add(new Pair<>(nonterminalDefinition.getName(), alternative));
			}
		}
		this.reductions = ImmutableList.copyOf(reductions);
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
		int index = terminals.indexOf(symbol);
		if (index >= 0) {
			return index;
		}
		index = nonterminals.indexOf(symbol);
		if (index >= 0) {
			return terminals.size() + index;
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

	public int getReductionIndex(Pair<String, Alternative> reduction) {
		int index = reductions.indexOf(reduction);
		if (index < 0) {
			throw new IllegalArgumentException("unknown reduction: " + reduction);
		}
		return index;
	}

	public int getShiftActionCode(Action.Shift shift) {
		return getStateIndex(shift.getNextState()) + 1;
	}

	public int getErrorActionCode() {
		return 0;
	}

	public int getReduceActionCode(Action.Reduce reduce) {
		return -1 - getReductionIndex(new Pair<>(reduce.getNonterminal(), reduce.getAlternative()));
	}

}
