package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class NonterminalAnnotation {

	/**
	 * Empty annotation that can be used as a default so we don't have to deal with null everywhere.
	 */
	public static final NonterminalAnnotation EMPTY = new NonterminalAnnotation(ClassType.NORMAL);

	private final ClassType classType;

	public NonterminalAnnotation(ClassType classType) {
		this.classType = ParameterUtil.ensureNotNull(classType, "classType");
	}

	public ClassType getClassType() {
		return classType;
	}

	@Override
	public String toString() {
		return "[" + classType + "]";
	}

	public enum ClassType {
		NORMAL,
		OPTIONAL,
		COLLECTION
	}

}
