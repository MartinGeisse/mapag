package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode()
 * are those of class Object, i.e. based on object identity. The reason is that even if this were a value object,
 * different parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't
 * really a situation where two distinct but equal instances of this class would exist. Instances are created from
 * the grammar file and anything that appears in different places in this file is not equal in any meaningful sense.
 */
public final class NonterminalDefinition extends SymbolDefinition {

	private final ImmutableList<Alternative> alternatives;
	private final PsiStyle psiStyle;

	public NonterminalDefinition(String name, ImmutableList<Alternative> alternatives, PsiStyle psiStyle) {
		super(name);
		this.alternatives = ParameterUtil.ensureNotNullOrEmpty(alternatives, "alternatives");
		ParameterUtil.ensureNoNullElement(alternatives, "alternatives");
		this.psiStyle = ParameterUtil.ensureNotNull(psiStyle, "psiStyle");
	}

	public ImmutableList<Alternative> getAlternatives() {
		return alternatives;
	}

	public PsiStyle getPsiStyle() {
		return psiStyle;
	}

	@Override
	public String toString() {
		return getName() + " ::= " + StringUtils.join(alternatives, " | ") + " [PSI style: " + psiStyle + "]";
	}

	public enum PsiStyle {

		// all user-defined nonterminal
		NORMAL,

		// implicitly defined using the question-mark operator
		OPTIONAL,

		// non-separated zero-or-more repetition
		ZERO_OR_MORE,

		// non-separated one-or-more repetition
		ONE_OR_MORE,

		// separated one-or-more repetition
		SEPARATED_ONE_OR_MORE,

		// helper for separated zero-or-more; works as an OPTIONAL that encloses a SEPARATED_ONE_OR_MORE
		OPTIONAL_SEPARATED_ONE_OR_MORE

	}

}
