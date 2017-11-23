package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class TestUtil {

	// prevent instantiation
	private TestUtil() {
	}

	/**
	 * Creates an {@link Expansion} object containing the specified symbols, without any expression names.
	 */
	public static Expansion expansion(String... symbols) {
		List<ExpansionElement> expansionElements = new ArrayList<>();
		for (String symbol : symbols) {
			expansionElements.add(new ExpansionElement(symbol, null));
		}
		return new Expansion(ImmutableList.copyOf(expansionElements));
	}

	/**
	 * Creates an {@link Expansion} object containing the specified symbols and expression names. The argument must
	 * contain alternating symbols and names.
	 */
	public static Expansion expansionWithNames(String... symbolsAndNames) {
		if (symbolsAndNames.length % 2 != 0) {
			throw new IllegalArgumentException("odd number of strings in the argument");
		}
		List<ExpansionElement> expansionElements = new ArrayList<>();
		for (int i = 0; i < symbolsAndNames.length; i += 2) {
			expansionElements.add(new ExpansionElement(symbolsAndNames[i], symbolsAndNames[i + 1]));
		}
		return new Expansion(ImmutableList.copyOf(expansionElements));
	}

}
