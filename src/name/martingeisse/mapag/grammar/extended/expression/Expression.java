package name.martingeisse.mapag.grammar.extended.expression;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public abstract class Expression {

	private final String name;

	public Expression(String name) {
		this.name = name;
	}

	/**
	 * Returns a new expression with the same meaning as this one but with the specified name. This expression must not
	 * have a name, otherwise this method throws an {@link IllegalStateException}.
	 */
	public abstract Expression withName(String name);

	// helper method for withName()
	final void checkNoName(String newName) {
		if (name != null) {
			throw new IllegalStateException("cannot use names " + name + " and " + newName + " for the same expression");
		}
	}

	public String getName() {
		return name;
	}

	/**
	 * Converts this expression into an expression at is, at its root, an n-ary OR expression. The resulting expression
	 * is implicit; this method just returns the list of operands. Since this expression isn't impossible to match in
	 * principle, the resulting list is never empty.
	 * <p>
	 * This method does some simple normalizations to streamline the results (e.g. flattening a one-element sequence)
	 * but doesn't try too hard: For example, it won't factor out an OR sub-expression nested in a sequence that
	 * contains other elements.
	 * <p>
	 * This method is needed to build the alternatives for the synthetic nonterminals generated from complex nested
	 * expressions.
	 */
	public ImmutableList<Expression> determineOrOperands() {
		return ImmutableList.of(this);
	}

}
