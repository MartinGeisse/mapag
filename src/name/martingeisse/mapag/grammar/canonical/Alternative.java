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

	private final String name;
	private final Expansion expansion;
	private final AlternativeConflictResolver conflictResolver;

	public Alternative(String name, Expansion expansion, AlternativeConflictResolver conflictResolver) {
		this.name = ParameterUtil.ensureNotNullOrEmpty(name, "name");
		this.expansion = ParameterUtil.ensureNotNull(expansion, "expansion");
		this.conflictResolver = conflictResolver;
	}

	public String getName() {
		return name;
	}

	public Expansion getExpansion() {
		return expansion;
	}

	public AlternativeConflictResolver getConflictResolver() {
		return conflictResolver;
	}

	public Alternative vanishSymbol(String symbol) {
		ParameterUtil.ensureNotNullOrEmpty(symbol, "symbol");
		return new Alternative(name, expansion.vanishSymbol(symbol), conflictResolver);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (name != null) {
			builder.append(name);
		} else {
			builder.append('?');
		}
		builder.append(" ::= ");
		builder.append(expansion);
		builder.append(' ');
		builder.append(conflictResolver);
		return builder.toString();
	}

}
