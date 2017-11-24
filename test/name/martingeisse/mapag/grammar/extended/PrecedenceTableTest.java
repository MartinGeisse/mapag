package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PrecedenceTableTest {

	private static final PrecedenceTable.Entry ENTRY_1 = new PrecedenceTable.Entry(ImmutableList.of("foo"), Associativity.NONASSOC);
	private static final PrecedenceTable.Entry ENTRY_2 = new PrecedenceTable.Entry(ImmutableList.of("bar"), Associativity.RIGHT);

	@Test(expected = IllegalArgumentException.class)
	public void testNullArgument() {
		new PrecedenceTable(null);
	}

	@Test
	public void testEmptyArgument() {
		new PrecedenceTable(ImmutableList.of());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testArgumentContainsEofSymbol() {
		new PrecedenceTable(ImmutableList.of(new PrecedenceTable.Entry(ImmutableList.of(SpecialSymbols.EOF_SYMBOL_NAME), Associativity.LEFT)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testArgumentContainsErrorSymbol() {
		new PrecedenceTable(ImmutableList.of(new PrecedenceTable.Entry(ImmutableList.of(SpecialSymbols.ERROR_SYMBOL_NAME), Associativity.LEFT)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testArgumentContainsRootSymbol() {
		new PrecedenceTable(ImmutableList.of(new PrecedenceTable.Entry(ImmutableList.of(SpecialSymbols.ROOT_SYMBOL_NAME), Associativity.LEFT)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testArgumentContainsBadCharacterSymbol() {
		new PrecedenceTable(ImmutableList.of(new PrecedenceTable.Entry(ImmutableList.of(SpecialSymbols.BAD_CHARACTER_SYMBOL_NAME), Associativity.LEFT)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testArgumentContainsOtherSpecialSymbol() {
		new PrecedenceTable(ImmutableList.of(new PrecedenceTable.Entry(ImmutableList.of("%whatever"), Associativity.LEFT)));
	}

	@Test
	public void testConstructorGetter() {
		ImmutableList<PrecedenceTable.Entry> list = ImmutableList.of(ENTRY_1, ENTRY_2);
		PrecedenceTable precedenceTable = new PrecedenceTable(list);
		Assert.assertSame(list, precedenceTable.getEntries());
	}

}
