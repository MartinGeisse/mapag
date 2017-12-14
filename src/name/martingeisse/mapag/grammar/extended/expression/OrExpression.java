package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public final class OrExpression extends Expression {

	private final Expression leftOperand;
	private final Expression rightOperand;

	public OrExpression(Expression leftOperand, Expression rightOperand) {
		this(null, leftOperand, rightOperand);
	}

	private OrExpression(String name, Expression leftOperand, Expression rightOperand) {
		super(name);
		ParameterUtil.ensureNotNull(leftOperand, "leftOperand");
		ParameterUtil.ensureNotNull(rightOperand, "rightOperand");
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	@Override
	public Expression withName(String name) {
		return new OrExpression(name, leftOperand, rightOperand);
	}

	public Expression getLeftOperand() {
		return leftOperand;
	}

	public Expression getRightOperand() {
		return rightOperand;
	}

	@Override
	public String toString() {
		return "(" + leftOperand + " | " + rightOperand + ')';
	}

	@Override
	protected boolean subclassEquals(Object obj) {
		OrExpression other = (OrExpression) obj;
		return leftOperand.equals(other.leftOperand) && rightOperand.equals(other.rightOperand);
	}

	@Override
	protected void buildSubclassHashCode(HashCodeBuilder builder) {
		builder.append(leftOperand).append(rightOperand);
	}

}
