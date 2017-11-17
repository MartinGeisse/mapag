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

	public Alternative(String name, Expression expression, String precedenceDefiningTerminal, ResolveBlock resolveBlock) {
		if (name != null && name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be empty"); // TODO test this
		}
		this.name = name;
		this.expression = ParameterUtil.ensureNotNull(expression, "expression");
		if (precedenceDefiningTerminal != null && precedenceDefiningTerminal.isEmpty()) {
			throw new IllegalArgumentException("precedenceDefiningTerminal cannot be the empty string");
		}
		if (precedenceDefiningTerminal != null && resolveBlock != null) {
			throw new IllegalArgumentException("cannot use both a precedence-defining terminal and a resolve block for the same alternative");
		}
		this.precedenceDefiningTerminal = precedenceDefiningTerminal;
		this.resolveBlock = resolveBlock;
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
		builder.append('}');
		return builder.toString();
	}

}
