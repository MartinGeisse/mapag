package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PrecedenceTableTest {

	private static final PrecedenceTable.Entry ENTRY_1 = new PrecedenceTable.Entry(ImmutableSet.of("foo"), Associativity.NONASSOC);
	private static final PrecedenceTable.Entry ENTRY_2 = new PrecedenceTable.Entry(ImmutableSet.of("bar"), Associativity.RIGHT);

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
		ImmutableList<PrecedenceTable.Entry> list = ImmutableList.of(ENTRY_1, ENTRY_2);
		PrecedenceTable precedenceTable = new PrecedenceTable(list);
		Assert.assertSame(list, precedenceTable.getEntries());
	}

}
