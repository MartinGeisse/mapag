package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public final class ExpansionElement {

	private final String symbol;
	private final String expressionName;

	public ExpansionElement(String symbol, String expressionName) {
		this.symbol = ParameterUtil.ensureNotNullOrEmpty(symbol, "symbol");
		this.expressionName = ParameterUtil.ensureNotEmpty(expressionName, "expressionName");
	}

	public String getSymbol() {
		return symbol;
	}

	public String getExpressionName() {
		return expressionName;
	}

	@Override
	public String toString() {
		if (expressionName == null) {
			return symbol;
		} else {
			return symbol + ':' + expressionName;
		}
	}

	// TODO test equals / hashcode

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ExpansionElement) {
			ExpansionElement other = (ExpansionElement) obj;
			return new EqualsBuilder()
				.append(symbol, other.getSymbol())
				.append(expressionName, other.getExpressionName())
				.build();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(symbol).append(expressionName).toHashCode();
	}

}
