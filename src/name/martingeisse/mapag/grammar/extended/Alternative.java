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

	public Alternative(String name, Expression expression, String precedenceDefiningTerminal) {
		if (name != null && name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be empty"); // TODO test this
		}
		this.name = name;
		this.expression = ParameterUtil.ensureNotNull(expression, "expression");
		if (precedenceDefiningTerminal != null && precedenceDefiningTerminal.isEmpty()) {
			throw new IllegalArgumentException("precedenceDefiningTerminal cannot be the empty string");
		}
		this.precedenceDefiningTerminal = precedenceDefiningTerminal;
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

	@Override
	public String toString() {
		if (precedenceDefiningTerminal == null) {
			return "{" + expression + '}';
		} else {
			return "{" + expression + " %precedence " + precedenceDefiningTerminal + '}';
		}
	}

}
