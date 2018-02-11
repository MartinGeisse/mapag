package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

public final class Repetition extends Expression {

	private final Expression elementExpression;
	private final Expression separatorExpression;
	private final boolean emptyAllowed;

	public Repetition(Expression elementExpression, Expression separatorExpression, boolean emptyAllowed) {
		this(null, elementExpression, separatorExpression, emptyAllowed);
	}

	private Repetition(String name, Expression elementExpression, Expression separatorExpression, boolean emptyAllowed) {
		super(name);
		this.elementExpression = ParameterUtil.ensureNotNull(elementExpression, "elementExpression");
		this.separatorExpression = separatorExpression;
		this.emptyAllowed = emptyAllowed;
	}

	public Expression getElementExpression() {
		return elementExpression;
	}

	public Expression getSeparatorExpression() {
		return separatorExpression;
	}

	public boolean isEmptyAllowed() {
		return emptyAllowed;
	}

	@Override
	public Expression withName(String name) {
		return new Repetition(name, elementExpression, separatorExpression, emptyAllowed);
	}

	@Override
	public String toString() {
		if (separatorExpression == null) {
			return "(" + elementExpression + (isEmptyAllowed() ? ")*" : ")+");
		} else {
			return "(" + elementExpression + ", " + separatorExpression + (isEmptyAllowed() ? ")*" : ")+");
		}
	}

	@Override
	protected boolean subclassEquals(Object obj) {
		Repetition other = (Repetition) obj;
		return elementExpression.equals(other.elementExpression)
			&& Objects.equals(separatorExpression, other.separatorExpression)
			&& emptyAllowed == other.emptyAllowed;
	}

	@Override
	protected void buildSubclassHashCode(HashCodeBuilder builder) {
		builder.append(elementExpression).append(separatorExpression).append(emptyAllowed);
	}

}
