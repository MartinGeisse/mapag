package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableSet;

import java.io.PrintWriter;

/**
 *
 */
public class StateMachineException extends RuntimeException {

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
			out.println("reduce/reduce conflict on terminal " + getTerminal());
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

}
