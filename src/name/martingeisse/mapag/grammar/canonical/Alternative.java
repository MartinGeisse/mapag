package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ListUtil;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode()
 * are those of class Object, i.e. based on object identity. The reason is that even if this were a value object,
 * different parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't
 * really a situation where two distinct but equal instances of this class would exist. Instances are created from
 * the grammar file and anything that appears in different places in this file is not equal in any meaningful sense.
 */
public final class Alternative {

	private final ImmutableList<String> expansion;
	private final String effectivePrecedenceTerminal;
	private final AlternativeAnnotation annotation;

	public Alternative(ImmutableList<String> expansion, String effectivePrecedenceTerminal, AlternativeAnnotation annotation) {
		ParameterUtil.ensureNotNull(expansion, "expansion");
		ParameterUtil.ensureNoNullOrEmptyElement(expansion, "expansion");
		this.expansion = expansion;
		if (effectivePrecedenceTerminal != null && effectivePrecedenceTerminal.isEmpty()) {
			throw new IllegalArgumentException("effectivePrecedenceTerminal cannot be the empty string");
		}
		this.effectivePrecedenceTerminal = effectivePrecedenceTerminal;
		this.annotation = ParameterUtil.ensureNotNull(annotation, "annotation");
	}

	public ImmutableList<String> getExpansion() {
		return expansion;
	}

	public String getEffectivePrecedenceTerminal() {
		return effectivePrecedenceTerminal;
	}

	public AlternativeAnnotation getAnnotation() {
		return annotation;
	}

	public Alternative vanishSymbol(String nonterminalToVanish) {
		// TODO test annotation handling
		ParameterUtil.ensureNotNullOrEmpty(nonterminalToVanish, "nonterminalToVanish");
		return new Alternative(
			ListUtil.withElementsRemoved(expansion, symbol -> symbol.equals(nonterminalToVanish)),
			effectivePrecedenceTerminal,
			annotation.forModifiedRightHandSide());
	}

	@Override
	public String toString() {
		return StringUtils.join(expansion, ' ') + " %precedence " + (effectivePrecedenceTerminal == null ? "%undefined" : effectivePrecedenceTerminal);
	}

}
