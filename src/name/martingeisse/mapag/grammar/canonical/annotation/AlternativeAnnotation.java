package name.martingeisse.mapag.grammar.canonical.annotation;

/**
 *
 */
public final class AlternativeAnnotation {

	/**
	 * Empty annotation that can be used as a default so we don't have to deal with null everywhere.
	 */
	public static final AlternativeAnnotation EMPTY = new AlternativeAnnotation(null);

	private final String name;

	public AlternativeAnnotation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public AlternativeAnnotation forModifiedRightHandSide() {
		// right now, the annotation doesn't store any information that is linked to the right-hand side
		return this;
	}

}
