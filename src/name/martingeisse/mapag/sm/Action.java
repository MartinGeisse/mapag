package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;

/**
 * Note: null is used to represent "syntax error" / unused entries since it's compatible with each subclass.
 */
public abstract class Action {

	private Action() {
	}

	public static final class Shift extends Action {

		private final State nextState;

		public Shift(State nextState) {
			this.nextState = nextState;
		}

		public State getNextState() {
			return nextState;
		}
	}

	public static final class Reduce extends Action {

		private final String nonterminal;
		private final Alternative alternative;

		public Reduce(String nonterminal, Alternative alternative) {
			this.nonterminal = nonterminal;
			this.alternative = alternative;
		}

		public String getNonterminal() {
			return nonterminal;
		}

		public Alternative getAlternative() {
			return alternative;
		}

		public Action checkForAcceptingImplicitStartNonterminal() {
			if (nonterminal.equals(SpecialSymbols.IMPLICIT_START_NONTERMINAL_NAME)) {
				return Accept.INSTANCE;
			} else {
				return this;
			}
		}

	}

	public static final class Accept extends Action {

		public static final Accept INSTANCE = new Accept();

		private Accept() {
		}

	}

}
