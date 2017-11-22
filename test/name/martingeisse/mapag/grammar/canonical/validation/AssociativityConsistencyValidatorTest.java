package name.martingeisse.mapag.grammar.canonical.validation;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class AssociativityConsistencyValidatorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		new AssociativityConsistencyValidator().validate(null);
	}

	@DataProvider
	public static Object[][] getConsistentTerminalDefinitions() {
		return new Object[][]{
			{
				ImmutableList.of(),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", null, Associativity.NONASSOC)),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", 0, Associativity.NONASSOC)),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", 0, Associativity.LEFT)),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", 0, Associativity.RIGHT)),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", 1, Associativity.NONASSOC)),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", 1, Associativity.LEFT)),
			},
			{
				ImmutableList.of(new TerminalDefinition("foo", 1, Associativity.RIGHT)),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.NONASSOC),
					new TerminalDefinition("bar", 0, Associativity.NONASSOC)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.LEFT),
					new TerminalDefinition("bar", 0, Associativity.LEFT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.RIGHT),
					new TerminalDefinition("bar", 0, Associativity.RIGHT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.NONASSOC),
					new TerminalDefinition("bar", 1, Associativity.LEFT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.NONASSOC),
					new TerminalDefinition("bar", 1, Associativity.RIGHT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.LEFT),
					new TerminalDefinition("bar", 1, Associativity.RIGHT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("a", 0, Associativity.LEFT),
					new TerminalDefinition("b", 1, Associativity.RIGHT),
					new TerminalDefinition("c", 0, Associativity.LEFT),
					new TerminalDefinition("d", 2, Associativity.NONASSOC),
					new TerminalDefinition("e", 1, Associativity.RIGHT),
					new TerminalDefinition("f", 1, Associativity.RIGHT),
					new TerminalDefinition("g", 0, Associativity.LEFT)
				),
			},
		};
	}

	@UseDataProvider("getConsistentTerminalDefinitions")
	@Test
	public void testConsistentAssociativity(ImmutableList<TerminalDefinition> terminalDefinitions) {
		new AssociativityConsistencyValidator().validate(terminalDefinitions);
	}

	@DataProvider
	public static Object[][] getInconsistentTerminalDefinitions() {
		return new Object[][]{
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.NONASSOC),
					new TerminalDefinition("bar", 0, Associativity.LEFT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.NONASSOC),
					new TerminalDefinition("bar", 1, Associativity.NONASSOC),
					new TerminalDefinition("baz", 0, Associativity.LEFT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("foo", 0, Associativity.NONASSOC),
					new TerminalDefinition("bar", 1, Associativity.LEFT),
					new TerminalDefinition("baz", 0, Associativity.LEFT)
				),
			},
			{
				ImmutableList.of(
					new TerminalDefinition("a", 0, Associativity.LEFT),
					new TerminalDefinition("b", 1, Associativity.RIGHT),
					new TerminalDefinition("c", 0, Associativity.RIGHT), // inconsistent entry
					new TerminalDefinition("d", 2, Associativity.NONASSOC),
					new TerminalDefinition("e", 1, Associativity.RIGHT),
					new TerminalDefinition("f", 1, Associativity.RIGHT),
					new TerminalDefinition("g", 0, Associativity.LEFT)
				),
			},
		};
	}

	@UseDataProvider("getInconsistentTerminalDefinitions")
	@Test(expected = IllegalStateException.class)
	public void testInconsistentAssociativity(ImmutableList<TerminalDefinition> terminalDefinitions) {
		new AssociativityConsistencyValidator().validate(terminalDefinitions);
	}

}
