package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public final class OptionalExpression extends Expression {

	private final Expression operand;

	public OptionalExpression(Expression operand) {
		this(null, operand);
	}

	private OptionalExpression(String name, Expression operand) {
		super(name);
		ParameterUtil.ensureNotNull(operand, "operand");
		this.operand = operand;
	}

	@Override
	public Expression withName(String name) {
		return new OptionalExpression(name, operand);
	}

	public Expression getOperand() {
		return operand;
	}

	@Override
	public String toString() {
		return "(" + operand.toString() + ")?";
	}

	@Override
	protected boolean subclassEquals(Object obj) {
		return operand.equals(((OptionalExpression) obj).getOperand());
	}

	@Override
	protected void buildSubclassHashCode(HashCodeBuilder builder) {
		builder.append(operand);
	}

}
