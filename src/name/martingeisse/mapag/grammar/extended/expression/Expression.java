package name.martingeisse.mapag.grammar.extended.expression;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;

/**
 *
 */
public abstract class Expression {

	/**
	 * Determines the implicit precedence defining terminal of this expression:
	 *
	 * - If this expression has its implicit precedence defined by a single terminal, then that is returned.
	 *   If such a terminal exists, it is the last terminal in the expression (possibly followed by nonterminals).
	 *
	 * - If this expression contains an OR-expression not followed by any further terminals, and all ORed operands
	 *   have equal precedence, then that precedence is used.
	 *
	 * - If this expression does not contain any terminals, returns null.
	 *
	 * - If this expression contains an OR-expression whose operands have different precedence, this method returns
	 *   null.
	 *
	 * - If the precedence would have to be defined by a repetition or optional expression, this method returns null.
	 *
	 * - If there are any other ambiguities, undefined-nesses, or similar, this method returns null.
	 *
	 * The key observation behind all this is that precedences are only used to resolve shift/reduce conflicts. If the
	 * implicit precedence of an expression cannot reliably determined, this method returns null, and the corresponding
	 * shift/reduce conflict doesn't get resolved -- instead, it gets reported and no code gets generated. Precedence
	 * does not affect reduce/reduce conflicts, and if no conflict exists in the first place, then precedence has no
	 * effect. Returning null therefore means erring on the safe side. If a shift/reduce conflict persists because
	 * we cannot determine precedence implicitly, the user can still assign an explicit precedence.
	 *
	 * Note that there is an ambiguity in which terminal gets returned if more than one terminal share the same
	 * precedence (and therefore have the same associativity). The choice is arbitrary since it has no effect on the
	 * generated parser. This method could even, in theory, return a terminal that doesn't even appear in this
	 * expression as long as the precedence and associativity are correct -- though this is avoided for clarity.
	 */
	public abstract String determinePrecedenceDefiningTerminal(ImmutableSet<String> terminals, PrecedenceTable precedenceTable);

}
