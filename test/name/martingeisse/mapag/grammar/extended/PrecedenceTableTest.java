package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PrecedenceTableTest {

	private static final PrecedenceTable.Entry ENTRY_1 = new PrecedenceTable.Entry("foo", Associativity.NONASSOC);
	private static final PrecedenceTable.Entry ENTRY_2 = new PrecedenceTable.Entry("bar", Associativity.RIGHT);

	// cannot test with null element because ImmutableList prevents that
	@Test(expected = IllegalArgumentException.class)
	public void testNullArgument() {
		new PrecedenceTable(null);
	}

	@Test
	public void testEmptyArgument() {
		new PrecedenceTable(ImmutableList.of());
	}

	@Test
	public void testConstructorGetter() {
		ImmutableList list = ImmutableList.of(ENTRY_1, ENTRY_2);
		PrecedenceTable precedenceTable = new PrecedenceTable(list);
		Assert.assertSame(list, precedenceTable.getEntries());
	}

}
