package name.martingeisse.mapag.sm;

import name.martingeisse.parsergen.grammar.Alternative;
import name.martingeisse.parsergen.grammar.Nonterminal;

/**
 * Note: null is used to represent "syntax error" / unused entries since it's compatible with each subclass.
 */
public abstract class Action {

	private Action() {
	}

	public static final class ShiftAction extends Action {

		private final State nextState;

		public ShiftAction(State nextState) {
			this.nextState = nextState;
		}

		public State getNextState() {
			return nextState;
		}
	}

	public static final class ReduceAction extends Action {

		private final Nonterminal nonterminal;
		private final Alternative alternative;

		public ReduceAction(Nonterminal nonterminal, Alternative alternative) {
			this.nonterminal = nonterminal;
			this.alternative = alternative;
		}

		public Nonterminal getNonterminal() {
			return nonterminal;
		}

		public Alternative getAlternative() {
			return alternative;
		}

	}

	public static final class AcceptAction extends Action {

		public static final AcceptAction INSTANCE = new AcceptAction();

		private AcceptAction() {
		}

	}

}
