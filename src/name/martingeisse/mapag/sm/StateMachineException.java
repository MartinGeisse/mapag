package name.martingeisse.mapag.sm;

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
			super(type + " conflict in state " + state + " on terminal " + terminal);
			this.state = state;
			this.terminal = terminal;
		}

		public State getState() {
			return state;
		}

		public String getTerminal() {
			return terminal;
		}

	}

	public static final class ShiftReduceConflict extends Conflict {

		public ShiftReduceConflict(State state, String terminal) {
			super("shift/reduce", state, terminal);
		}

	}

	public static final class ReduceReduceConflict extends Conflict {

		public ReduceReduceConflict(State state, String terminal) {
			super("reduce/reduce", state, terminal);
		}

	}

}
