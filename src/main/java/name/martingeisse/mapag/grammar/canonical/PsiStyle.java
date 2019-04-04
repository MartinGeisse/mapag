package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public abstract class PsiStyle {

	// prevent outside instantiation
	private PsiStyle() {
	}

	public boolean isDistinctSymbolPerAlternative() {
		return false;
	}

	// normal treatment for user-defined nonterminals
	public static final class Normal extends PsiStyle {

		public static final Normal INSTANCE = new Normal();

		public boolean isDistinctSymbolPerAlternative() {
			return true;
		}

	}

	// symbol generates an optional (AST node with zero or one children)
	public static final class Optional extends PsiStyle {

		private String operandSymbol;

		public Optional(String operandSymbol) {
			this.operandSymbol = ParameterUtil.ensureNotNullOrEmpty(operandSymbol, "operandSymbol");
		}

		public String getOperandSymbol() {
			return operandSymbol;
		}

	}

	// symbol generates a repetition (AST node with an arbitrary number of children)
	public static final class Repetition extends PsiStyle {

		private String elementSymbol;
		private String separatorSymbol;

		public Repetition(String elementSymbol, String separatorSymbol) {
			this.elementSymbol = ParameterUtil.ensureNotNullOrEmpty(elementSymbol, "elementSymbol");
			if (separatorSymbol != null && separatorSymbol.isEmpty()) {
				throw new IllegalArgumentException("separator symbol cannot be the empty string");
			}
			this.separatorSymbol = separatorSymbol;
		}

		public String getElementSymbol() {
			return elementSymbol;
		}

		public void setElementSymbol(String elementSymbol) {
			this.elementSymbol = elementSymbol;
		}

		public String getSeparatorSymbol() {
			return separatorSymbol;
		}

		public void setSeparatorSymbol(String separatorSymbol) {
			this.separatorSymbol = separatorSymbol;
		}

	}

}
