package name.martingeisse.parsergen.sm;

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

}
