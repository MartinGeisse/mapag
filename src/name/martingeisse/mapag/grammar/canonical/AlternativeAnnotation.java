package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 * Note: the list of expression names (if present) contains an empty string for each unnamed expression since
 * {@link ImmutableList} cannot contain null.
 *
 * TODO: If, in the end, this class doesn't contain too much information, consider merging it back into {@link Alternative}.
 * Possibly provide static factory methods in Alternative for more readable code and shorter parameter lists. Consider
 * providing an AlternativeBuilder.
 */
public final class AlternativeAnnotation {

	private final String alternativeName;
	private final ImmutableList<String> expressionNames;

	public AlternativeAnnotation(String alternativeName, ImmutableList<String> expressionNames) {
		this.alternativeName = ParameterUtil.ensureNotNullOrEmpty(alternativeName, "alternativeName");
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
