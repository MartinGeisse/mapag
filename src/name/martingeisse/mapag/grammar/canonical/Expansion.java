package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public final class Expansion {

	private final ImmutableList<ExpansionElement> elements;

	public Expansion(ImmutableList<ExpansionElement> elements) {
		this.elements = ParameterUtil.ensureNotNull(elements, "elements");
	}

	public ImmutableList<ExpansionElement> getElements() {
		return elements;
	}

	public ImmutableList<String> getSymbols() {
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for (ExpansionElement element : elements) {
			builder.add(element.getSymbol());
		}
		return builder.build();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public Expansion vanishSymbol(String symbol) {
		ImmutableList.Builder<ExpansionElement> builder = ImmutableList.builder();
		for (ExpansionElement element : elements) {
			if (!element.getSymbol().equals(symbol)) {
				builder.add(element);
			}
		}
		return new Expansion(builder.build());
	}

	@Override
	public String toString() {
		return StringUtils.join(' ', elements);
	}

}
