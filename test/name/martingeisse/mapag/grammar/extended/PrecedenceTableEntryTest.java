package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PrecedenceTableEntryTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminalName() {
		new PrecedenceTable.Entry(null, Associativity.NONASSOC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyTerminalName() {
		new PrecedenceTable.Entry("", Associativity.NONASSOC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAssociativity() {
		new PrecedenceTable.Entry("foo", null);
	}

	@Test
	public void testConstructorGetter() {
		PrecedenceTable.Entry entry1 = new PrecedenceTable.Entry("foo", Associativity.NONASSOC);
		Assert.assertEquals("foo", entry1.getTerminalName());
		Assert.assertEquals(Associativity.NONASSOC, entry1.getAssociativity());

		PrecedenceTable.Entry entry2 = new PrecedenceTable.Entry("bar", Associativity.RIGHT);
		Assert.assertEquals("bar", entry2.getTerminalName());
		Assert.assertEquals(Associativity.RIGHT, entry2.getAssociativity());
	}

}
