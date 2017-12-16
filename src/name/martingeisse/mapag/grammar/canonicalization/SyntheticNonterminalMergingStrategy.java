package name.martingeisse.mapag.grammar.canonicalization;

import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.expression.OptionalExpression;
import name.martingeisse.mapag.grammar.extended.expression.Repetition;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import name.martingeisse.mapag.util.GenericIdentifierUtil;

/**
 * Determines whether distinct synthetic nonterminals should be merged, and if so, what name should be used for
 * the merged nonterminal. This prevents cases like the expression (someSymbol?), appearing at various places,
 * generating a new synthetic nonterminal each time. This reduces the number of symbols and thus PSI classes, and
 * can also prevent conflicts.
 * <p>
 * Only simple cases are recognized for now. Any case not recognized can always be resolved by introducing an
 * explicit new nonterminal. When this class gets expanded, it should still only return a name for an expression
 * that would be extracted to a synthetic nonterminal anyway, otherwise it generates additional, unnecessary
 * nonterminals.
 * <p>
 * Note: This is currently a simple class, but could be hidden behind a strategy interface easily.
 */
public class SyntheticNonterminalMergingStrategy {

	/**
	 * If the specified expression is determined feasible for merging, then this method returns the name for the
	 * resulting synthetic nonterminal. Since the purpose of merging is to make that nonterminal independent of the
	 * contexts it is used in, the context is not passed as arguments here.
	 * <p>
	 * Returns null if the expression is not feasible for merging. In that case, the resulting synthetic nonterminal
	 * will be assigned an automatically generated name based on its context.
	 */
	public String determineMergedName(Expression expression) {

		if (expression instanceof OptionalExpression) {
			String operandName = determineOperandName(((OptionalExpression) expression).getOperand());
			return operandName == null ? null : ("Optional" + operandName);
		}

		if (expression instanceof Repetition) {
			Repetition repetition = (Repetition) expression;
			return determineListName(repetition, repetition.isEmptyAllowed());
		}

		return null;
	}

	public String determineOptionalOperandName(OptionalExpression expression) {
		return determineOperandName(expression.getOperand());
	}

	private String determineListName(Repetition repetition, boolean emptyAllowed) {
		String prefix = (emptyAllowed ? "" : "Nonempty");
		String elementName = determineListElementName(repetition);
		if (elementName == null) {
			// element too complex
			return null;
		}
		if (repetition.getSeparatorExpression() == null) {
			// simple element without separator
			return prefix + "ListOf" + elementName;
		}
		String separatorName = determineListSeparatorName(repetition);
		if (separatorName == null) {
			// separator too complex
			return null;
		}
		// simple element with simple separator
		return prefix + separatorName + "SeparatedListOf" + elementName;

	}

	public String determineNonemptyListName(Repetition repetition) {
		return determineListName(repetition, false);
	}

	public String determineListElementName(Repetition repetition) {
		return determineOperandName(repetition.getElementExpression());
	}

	public String determineListSeparatorName(Repetition repetition) {
		return determineOperandName(repetition.getSeparatorExpression());
	}

	private String determineOperandName(Expression expression) {
		if (expression instanceof SymbolReference) {
			String symbol = ((SymbolReference) expression).getSymbolName();
			if (symbol.toUpperCase().equals(symbol) || symbol.toLowerCase().equals(symbol)) {
				// generate nice names for nonterminals which are originally in UPPER_SNAKE_CASE
				return GenericIdentifierUtil.toIdentifier(symbol, true);
			} else {
				return symbol;
			}
		} else {
			return null;
		}
	}
}
