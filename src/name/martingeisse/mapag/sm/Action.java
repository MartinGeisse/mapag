package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.grammar.canonical.info.AlternativeInfo;

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

		private final String nonterminal;
		private final AlternativeInfo alternativeInfo;

		public ReduceAction(String nonterminal, AlternativeInfo alternativeInfo) {
			this.nonterminal = nonterminal;
			this.alternativeInfo = alternativeInfo;
		}

		public String getNonterminal() {
			return nonterminal;
		}

		public AlternativeInfo getAlternativeInfo() {
			return alternativeInfo;
		}
	}

	public static final class AcceptAction extends Action {

		public static final AcceptAction INSTANCE = new AcceptAction();

		private AcceptAction() {
		}

	}

}
