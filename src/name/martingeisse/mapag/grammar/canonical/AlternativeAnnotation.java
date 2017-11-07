package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 * Note: the list of expression names (if present) contains an empty string for each unnamed expression since
 * {@link ImmutableList} cannot contain null.
 */
public final class AlternativeAnnotation {

	/**
	 * Empty annotation that can be used as a default so we don't have to deal with null everywhere.
	 */
	public static final AlternativeAnnotation EMPTY = new AlternativeAnnotation(null, null);

	private final String alternativeName;
	private final ImmutableList<String> expressionNames;

	public AlternativeAnnotation(String alternativeName, ImmutableList<String> expressionNames) {
		this.alternativeName = alternativeName;
		this.expressionNames = expressionNames;
		ParameterUtil.ensureNoNullElement(expressionNames, "expressionNames");
	}

	public String getAlternativeName() {
		return alternativeName;
	}

	public ImmutableList<String> getExpressionNames() {
		return expressionNames;
	}

	/**
	 * Validates the usage of this annotation for construction of an alternative. This cannot validate anything
	 * outside the alternative, so if only performs very basic checks.
 	 */

	void validateConstruction(Alternative parentAlternative) {
		if (expressionNames != null && expressionNames.size() != parentAlternative.getExpansion().size()) {
			throw new IllegalArgumentException("list of symbols and list of expression names have different length");
		}
	}

}
