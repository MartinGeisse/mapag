package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
final class TestUtil {

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

}
