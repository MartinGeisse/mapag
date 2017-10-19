package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.*;
import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.START_NONTERMINAL_NAME;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class FirstSetHelperTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullGrammar() {
		FirstSetHelper.runFor(null, ImmutableSet.of());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullVanishableNonterminals() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		FirstSetHelper.runFor(grammar, null);
	}

	@DataProvider
	public static Object[][] getTestData() {
		return new Object[][]{
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
										new Alternative(ImmutableList.of(), null)
								))
						),
						ImmutableSet.of("dummyStart"),
						ImmutableMap.of("dummyStart", ImmutableSet.of()),
				},
		};
	}

	@Test
	@UseDataProvider("getTestData")
	public void testWithTestData(ImmutableList<NonterminalDefinition> nonterminals,
								 ImmutableSet<String> vanishableNonterminals,
								 ImmutableMap<String, ImmutableSet<String>> expectedResult) {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, nonterminals, START_NONTERMINAL_NAME);
		ImmutableMap<String, ImmutableSet<String>> result =  FirstSetHelper.runFor(grammar, vanishableNonterminals);
		Assert.assertEquals(expectedResult, result);
	}

	// TODO

}
