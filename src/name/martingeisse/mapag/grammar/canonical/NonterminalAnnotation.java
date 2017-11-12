package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class NonterminalAnnotation {

	/**
	 * Empty annotation that can be used as a default so we don't have to deal with null everywhere.
	 */
	public static final NonterminalAnnotation EMPTY = new NonterminalAnnotation(PsiStyle.NORMAL);

	private final PsiStyle psiStyle;

	public NonterminalAnnotation(PsiStyle psiStyle) {
		this.psiStyle = ParameterUtil.ensureNotNull(psiStyle, "psiStyle");
	}

	public PsiStyle getPsiStyle() {
		return psiStyle;
	}

	@Override
	public String toString() {
		return "[" + psiStyle + "]";
	}

	public enum PsiStyle {
		NORMAL,
		OPTIONAL,
		ZERO_OR_MORE,
		ONE_OR_MORE
	}

}
