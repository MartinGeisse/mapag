package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: Even though this class is immutable, it does not define a value object. Especially, equals() and hashCode() are
 * those of class Object, i.e. based on object identity. The reason is that even if this were a value object, different
 * parts of the parser generator have different assumptions of what "equal" means. Furthermore, there isn't really a
 * situation where two distinct but equal instances of this class would exist. Instances are created from the grammar
 * file and anything that appears in different places in this file is not equal in any meaningful sense.
 */
public final class Alternative {

	private final ImmutableList<String> expansion;
	private final AlternativeConflictResolver conflictResolver;
	private final AlternativeAnnotation annotation;

	public Alternative(ImmutableList<String> expansion, AlternativeConflictResolver conflictResolver, AlternativeAnnotation annotation) {
		ParameterUtil.ensureNotNull(expansion, "expansion");
		ParameterUtil.ensureNoNullOrEmptyElement(expansion, "expansion");
		this.expansion = expansion;
		this.conflictResolver = conflictResolver;
		this.annotation = ParameterUtil.ensureNotNull(annotation, "annotation");
		annotation.validateConstruction(this);
	}

	public ImmutableList<String> getExpansion() {
		return expansion;
	}

	public AlternativeConflictResolver getConflictResolver() {
		return conflictResolver;
	}

	public AlternativeAnnotation getAnnotation() {
		return annotation;
	}

	public Alternative vanishSymbol(String nonterminalToVanish) {
		// TODO test annotation handling
		ImmutableList<String> expressionNames = annotation.getExpressionNames();
		List<String> remainingSymbols = new ArrayList<>();
		List<String> remainingExpressionNames = new ArrayList<>();
		for (int i = 0; i < expansion.size(); i++) {
			String symbol = expansion.get(i);
			if (symbol.equals(nonterminalToVanish)) {
				continue;
			}
			remainingSymbols.add(symbol);
			if (expressionNames != null) {
				remainingExpressionNames.add(expressionNames.get(i));
			}
		}
		ParameterUtil.ensureNotNullOrEmpty(nonterminalToVanish, "nonterminalToVanish");
		return new Alternative(
			ImmutableList.copyOf(remainingSymbols),
			conflictResolver,
			new AlternativeAnnotation(
				annotation.getAlternativeName(),
				expressionNames == null ? null : ImmutableList.copyOf(remainingExpressionNames)
			)
		);
	}

	@Override
	public String toString() {
		return StringUtils.join(expansion, ' ') + ' ' + conflictResolver + ' ' + annotation;
	}

}
