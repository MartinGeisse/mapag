package name.martingeisse.mapag.grammar.expression;

import name.martingeisse.mapag.grammar.info.AlternativeInfo;

import java.util.List;

/**
 *
 */
public abstract class Expression {

	public abstract void contributeAlternativesTo(List<AlternativeInfo> alternatives);

}
