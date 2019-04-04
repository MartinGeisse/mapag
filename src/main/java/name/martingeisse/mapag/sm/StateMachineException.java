package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.util.SelfDescribingRuntimeException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.PrintWriter;

/**
 *
 */
public abstract class StateMachineException extends SelfDescribingRuntimeException {

	public StateMachineException() {
	}

	public StateMachineException(String message) {
		super(message);
	}

	public StateMachineException(String message, Throwable cause) {
		super(message, cause);
	}

	public StateMachineException(Throwable cause) {
		super(cause);
	}

	public static abstract class Conflict extends StateMachineException {

		private final State state;
		private final String terminal;

		private Conflict(String type, State state, String terminal) {
			super(type + " conflict");
			this.state = state;
			this.terminal = terminal;
		}

		public State getState() {
			return state;
		}

		public String getTerminal() {
			return terminal;
		}

		public abstract void describe(PrintWriter out);

	}

	public static final class ShiftReduceConflict extends Conflict {

		private final ImmutableSet<StateElement> elementsThatWantToShift;
		private final ImmutableSet<StateElement> elementsThatWantToReduce;

		public ShiftReduceConflict(State state, String terminal, ImmutableSet<StateElement> elementsThatWantToShift, ImmutableSet<StateElement> elementsThatWantToReduce) {
			super("shift/reduce", state, terminal);
			this.elementsThatWantToShift = elementsThatWantToShift;
			this.elementsThatWantToReduce = elementsThatWantToReduce;
		}

		public ImmutableSet<StateElement> getElementsThatWantToShift() {
			return elementsThatWantToShift;
		}

		public ImmutableSet<StateElement> getElementsThatWantToReduce() {
			return elementsThatWantToReduce;
		}

		public void describe(PrintWriter out) {
			out.println("shift/reduce conflict on terminal " + getTerminal());
			out.println();
			out.println("state elements that want to shift:");
			for (StateElement stateElement : elementsThatWantToShift) {
				out.println("    " + stateElement);
			}
			out.println();
			out.println("state elements that want to reduce:");
			for (StateElement stateElement : elementsThatWantToReduce) {
				out.println("    " + stateElement);
			}
			out.println();
			out.println("---------------------------------------------------------------------------------");
			out.println("complete state:");
			out.println(getState());
			out.println();
		}

	}

	public static final class ReduceReduceConflict extends Conflict {

		private final ImmutableSet<StateElement> elementsThatWantToReduce;

		public ReduceReduceConflict(State state, String terminal, ImmutableSet<StateElement> elementsThatWantToReduce) {
			super("reduce/reduce", state, terminal);
			this.elementsThatWantToReduce = elementsThatWantToReduce;
		}

		public ImmutableSet<StateElement> getElementsThatWantToReduce() {
			return elementsThatWantToReduce;
		}

		public void describe(PrintWriter out) {
			out.print("reduce/reduce conflict on terminal " + getTerminal());
			out.println();
			out.println();
			out.println("state elements that want to reduce:");
			for (StateElement stateElement : elementsThatWantToReduce) {
				out.println("    " + stateElement);
			}
			out.println();
			out.println("---------------------------------------------------------------------------------");
			out.println("complete state:");
			out.println(getState());
			out.println();
		}

	}

	public static final class ConflictResolutionDisagreement extends Conflict {

		private final ImmutableSet<StateElement> elementsThatWantToShiftNormally;
		private final ImmutableSet<StateElement> elementsThatWantToShiftAsResolution;
		private final ImmutableSet<StateElement> elementsThatWantToReduceAsResolution;

		public ConflictResolutionDisagreement(State state, String terminal, ImmutableSet<StateElement> elementsThatWantToShiftNormally, ImmutableSet<StateElement> elementsThatWantToShiftAsResolution, ImmutableSet<StateElement> elementsThatWantToReduceAsResolution) {
			super("conflict resolution disagreement", state, terminal);
			this.elementsThatWantToShiftNormally = elementsThatWantToShiftNormally;
			this.elementsThatWantToShiftAsResolution = elementsThatWantToShiftAsResolution;
			this.elementsThatWantToReduceAsResolution = elementsThatWantToReduceAsResolution;
		}

		public ImmutableSet<StateElement> getElementsThatWantToShiftNormally() {
			return elementsThatWantToShiftNormally;
		}

		public ImmutableSet<StateElement> getElementsThatWantToShiftAsResolution() {
			return elementsThatWantToShiftAsResolution;
		}

		public ImmutableSet<StateElement> getElementsThatWantToReduceAsResolution() {
			return elementsThatWantToReduceAsResolution;
		}

		public void describe(PrintWriter out) {
			out.print("conflict resolution disagreement on terminal " + getTerminal());
			out.println();
			out.println();
			out.println("state elements that want to shift normally:");
			for (StateElement stateElement : elementsThatWantToShiftNormally) {
				out.println("    " + stateElement);
			}
			out.println("state elements that wanted to reduce, but now want others to shift as conflict resolution:");
			for (StateElement stateElement : elementsThatWantToShiftAsResolution) {
				out.println("    " + stateElement);
			}
			out.println("state elements that wanted to reduce, and still want to reduce as conflict resolution:");
			for (StateElement stateElement : elementsThatWantToReduceAsResolution) {
				out.println("    " + stateElement);
			}
			out.println();
			out.println("---------------------------------------------------------------------------------");
			out.println("complete state:");
			out.println(getState());
			out.println();
		}

	}

	public static final class OnErrorReduceReduceConflict extends Conflict {

		private final ImmutableSet<Pair<String, Alternative>> nonterminalsAndAlternativesThatWantToReduce;

		public OnErrorReduceReduceConflict(State state, ImmutableSet<Pair<String, Alternative>> nonterminalsAndAlternativesThatWantToReduce) {
			super("onError reduce/reduce", state, null);
			this.nonterminalsAndAlternativesThatWantToReduce = nonterminalsAndAlternativesThatWantToReduce;
		}

		public ImmutableSet<Pair<String, Alternative>> getNonterminalsAndAlternativesThatWantToReduce() {
			return nonterminalsAndAlternativesThatWantToReduce;
		}

		public void describe(PrintWriter out) {
			out.print("onError reduce/reduce conflict on terminal " + getTerminal());
			out.println();
			out.println();
			out.println("nonterminals and alternatives that want to reduce:");
			for (Pair<String, Alternative> nonterminalAndAlternative : nonterminalsAndAlternativesThatWantToReduce) {
				out.println("    " + nonterminalAndAlternative.getLeft() + " / " + nonterminalAndAlternative.getRight());
			}
			out.println();
			out.println("---------------------------------------------------------------------------------");
			out.println("complete state:");
			out.println(getState());
			out.println();
		}

	}

}
