package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.ResolveBlock;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.*;

/**
 *
 */
public class BootstrapBase {

	public static SymbolReference symbol(String name) {
		return new SymbolReference(name);
	}

	public static Expression sequence(Expression... expressions) {
		if (expressions.length == 0) {
			return new EmptyExpression();
		} else if (expressions.length == 1) {
			return expressions[0];
		} else {
			return sequence(0, expressions);
		}
	}

	public static Expression sequence(int i, Expression... expressions) {
		if (i == expressions.length) {
			return new EmptyExpression();
		} else if (i == expressions.length - 1) {
			return expressions[i];
		} else {
			return new SequenceExpression(expressions[i], sequence(i + 1, expressions));
		}
	}

	public static Expression or(Expression... expressions) {
		return or(0, expressions);
	}

	public static Expression or(int i, Expression... expressions) {
		if (i == expressions.length) {
			return new EmptyExpression();
		} else if (i == expressions.length - 1) {
			return expressions[i];
		} else {
			return new OrExpression(expressions[i], or(i + 1, expressions));
		}
	}

	public static OptionalExpression optional(Expression... expressions) {
		return new OptionalExpression(sequence(expressions));
	}

	public static ZeroOrMoreExpression zeroOrMore(Expression... expressions) {
		return new ZeroOrMoreExpression(sequence(expressions));
	}

	public static OneOrMoreExpression oneOrMore(Expression... expressions) {
		return new OneOrMoreExpression(sequence(expressions));
	}

	public static Alternative alternative(String name, Expression expression) {
		return new Alternative(name, expression, null, null, false, false);
	}

	public static Alternative alternativeWithPrecedence(String name, Expression expression, String precedenceDefiningTerminal) {
		return new Alternative(name, expression, precedenceDefiningTerminal, null, false, false);
	}

	public static Alternative alternativeWithResolution(String name, Expression expression, ImmutableList<String> shiftTerminals, ImmutableList<String> reduceTerminals) {
		ResolveDeclaration shiftDeclaration = new ResolveDeclaration(ConflictResolution.SHIFT, shiftTerminals);
		ResolveDeclaration reduceDeclaration = new ResolveDeclaration(ConflictResolution.REDUCE, reduceTerminals);
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of(shiftDeclaration, reduceDeclaration));
		return new Alternative(name, expression, null, resolveBlock, false, false);
	}

	public static Alternative alternativeWithReduceOnError(String name, Expression expression) {
		return new Alternative(name, expression, null, null, true, false);
	}

	public static Alternative alternativeWithReduceOnEofOnly(String name, Expression expression) {
		return new Alternative(name, expression, null, null, false, true);
	}

}
