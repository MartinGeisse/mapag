package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class Alternative {

	private final String name;
	private final Expression expression;
	private final String precedenceDefiningTerminal;
	private final ResolveBlock resolveBlock;
	private final boolean reduceOnError;
	private final boolean reduceOnEofOnly;

	public Alternative(String name, Expression expression, String precedenceDefiningTerminal, ResolveBlock resolveBlock, boolean reduceOnError, boolean reduceOnEofOnly) {
		this.name = ParameterUtil.ensureNotEmpty(name, "name");
		this.expression = ParameterUtil.ensureNotNull(expression, "expression");
		ParameterUtil.ensureNotEmpty(precedenceDefiningTerminal, "precedenceDefiningTerminal");
		if (precedenceDefiningTerminal != null && resolveBlock != null) {
			throw new IllegalArgumentException("cannot use both a precedence-defining terminal and a resolve block for the same alternative");
		}
		this.precedenceDefiningTerminal = precedenceDefiningTerminal;
		this.resolveBlock = resolveBlock;
		this.reduceOnError = reduceOnError;
		this.reduceOnEofOnly = reduceOnEofOnly;
	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	public String getPrecedenceDefiningTerminal() {
		return precedenceDefiningTerminal;
	}

	public ResolveBlock getResolveBlock() {
		return resolveBlock;
	}

	public boolean isReduceOnError() {
		return reduceOnError;
	}

	public boolean isReduceOnEofOnly() {
		return reduceOnEofOnly;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		builder.append(expression);
		if (precedenceDefiningTerminal != null) {
			builder.append(" %precedence ").append(precedenceDefiningTerminal);
		}
		if (resolveBlock != null) {
			builder.append(" %resolve ").append(resolveBlock);
		}
		if (reduceOnError) {
			builder.append(" %reduceOnError");
		}
		if (reduceOnEofOnly) {
			builder.append(" %eof");
		}
		builder.append('}');
		return builder.toString();
	}

}
