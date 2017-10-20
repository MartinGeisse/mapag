package name.martingeisse.mapag.grammar.extended.expression;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public abstract class Expression {

	/**
	 * Converts this expression into an expression at is, at its root, an n-ary OR expression. The resulting expression
	 * is implicit; this method just returns the list of operands. Since this expression isn't impossible to match in
	 * principle, the resulting list is never empty.
	 *
	 * This method does some simple normalizations to streamline the results (e.g. flattening a one-element sequence)
	 * but doesn't try too hard: For example, it won't factor out an OR sub-expression nested in a sequence that
	 * contains other elements.
	 *
	 * This method is needed to build the alternatives for the synthetic nonterminals generated from complex nested
	 * expressions.
	 */
	public ImmutableList<Expression> determineOrOperands() {
		return ImmutableList.of(this);
	}

}
