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
import name.martingeisse.mapag.grammar.canonical.annotation.AlternativeAnnotation;
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
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		FirstSetHelper.runFor(grammar, null);
	}

	@DataProvider
	public static Object[][] getTestData() {
		return new Object[][]{

				//
				// cases involving only a single nonterminal
				//

				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("dummyStart"),
						ImmutableMap.of("dummyStart", ImmutableSet.of()),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("foo", "bar"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("baz", "bar"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of("dummyStart", ImmutableSet.of("foo", "baz")),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("foo", "bar"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("baz", "bar"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("dummyStart"),
						ImmutableMap.of("dummyStart", ImmutableSet.of("foo", "baz")),
				},

				//
				// inheriting first-set symbols from nested nonterminals
				//

				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("nt1"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar"),
								"nt1", ImmutableSet.of("bar")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("nt1"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("nt2"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar", "baz"),
								"nt1", ImmutableSet.of("bar", "baz"),
								"nt2", ImmutableSet.of("baz")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("nt1"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("nt2"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar", "baz"),
								"nt1", ImmutableSet.of("bar", "baz"),
								"nt2", ImmutableSet.of("baz")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("nt1"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("nt2"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("foo")
						),
				},

				//
				// don't inherit the second symbol's first set if the first symbol is not vanishable
				//

				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar")
						),
				},


				//
				// inherit the second symbol's first set if the first symbol is vanishable
				//

				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2"), null, AlternativeAnnotation.EMPTY)
										)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("nt1"),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar")
						),
				},

				//
				// inherit the third symbol's first set only if both the first and second symbols are vanishable
				//

				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2", "nt3"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt3", ImmutableList.of(
									new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar"),
								"nt3", ImmutableSet.of("baz")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2", "nt3"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt3", ImmutableList.of(
									new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("nt1"),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar"),
								"nt3", ImmutableSet.of("baz")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2", "nt3"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt3", ImmutableList.of(
									new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("nt2"),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar"),
								"nt3", ImmutableSet.of("baz")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2", "nt3"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt3", ImmutableList.of(
									new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("nt1", "nt2"),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar", "baz"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar"),
								"nt3", ImmutableSet.of("baz")
						),
				},

				//
				// inherit first-set symbols that the second symbol inherited from a nested symbol IFF the first
				// symbol is vanishable
				//

				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("nt3"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt3", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of(),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar"),
								"nt3", ImmutableSet.of("bar")
						),
				},
				{
						ImmutableList.of(
								new NonterminalDefinition("dummyStart", ImmutableList.of(
									new Alternative(ImmutableList.of("nt1", "nt2"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt1", ImmutableList.of(
									new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
									new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt2", ImmutableList.of(
									new Alternative(ImmutableList.of("nt3"), null, AlternativeAnnotation.EMPTY)
								)),
								new NonterminalDefinition("nt3", ImmutableList.of(
									new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY)
								))
						),
						ImmutableSet.of("nt1"),
						ImmutableMap.of(
								"dummyStart", ImmutableSet.of("foo", "bar"),
								"nt1", ImmutableSet.of("foo"),
								"nt2", ImmutableSet.of("bar"),
								"nt3", ImmutableSet.of("bar")
						),
				},


		};
	}

	@Test
	@UseDataProvider("getTestData")
	public void testWithTestData(ImmutableList<NonterminalDefinition> nonterminals,
								 ImmutableSet<String> vanishableNonterminals,
								 ImmutableMap<String, ImmutableSet<String>> expectedResult) {
		Grammar grammar = new Grammar(TERMINALS, nonterminals, START_NONTERMINAL_NAME);
		ImmutableMap<String, ImmutableSet<String>> result = FirstSetHelper.runFor(grammar, vanishableNonterminals);
		Assert.assertEquals(expectedResult, result);
	}

}
