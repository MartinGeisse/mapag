package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PrecedenceTableEntryTest {

	private static final ImmutableSet<String> TERMINAL_NAMES = ImmutableSet.of("abc", "def");

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminalNames() {
		new PrecedenceTable.Entry(null, Associativity.NONASSOC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyTerminalNames() {
		new PrecedenceTable.Entry(ImmutableSet.of(), Associativity.NONASSOC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTerminalNamesContainsEmpty() {
		new PrecedenceTable.Entry(ImmutableSet.of("abc", "", "foo"), Associativity.NONASSOC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAssociativity() {
		new PrecedenceTable.Entry(ImmutableSet.of("foo"), null);
	}

	@Test
	public void testConstructorGetter() {
		PrecedenceTable.Entry entry1 = new PrecedenceTable.Entry(TERMINAL_NAMES, Associativity.NONASSOC);
		Assert.assertEquals(TERMINAL_NAMES, entry1.getTerminalNames());
		Assert.assertEquals(Associativity.NONASSOC, entry1.getAssociativity());

		PrecedenceTable.Entry entry2 = new PrecedenceTable.Entry(TERMINAL_NAMES, Associativity.RIGHT);
		Assert.assertEquals(TERMINAL_NAMES, entry2.getTerminalNames());
		Assert.assertEquals(Associativity.RIGHT, entry2.getAssociativity());
	}

}
