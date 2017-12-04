package name.martingeisse.mapag.grammar.extended.expression;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 * TODO test this class
 */
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

}
