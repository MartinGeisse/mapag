package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableList;
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

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class VanishableNonterminalsHelperTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullInput() {
		VanishableNonterminalsHelper.runFor(null);
	}

	@DataProvider
	public static Object[][] getTestData() {
		return new Object[][]{

			//
			// some cases without vanishable nonterminals
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of(),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("nt1", ImmutableList.of(
						new Alternative(ImmutableList.of("foo", "bar"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("foo", "baz"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("nt2", ImmutableList.of(
						new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("nt1", "nt2", "foo"), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of(),
			},

			//
			// some cases with vanishable nonterminals
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of("dummyStart"),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of("dummyStart"),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("baz"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of("abc"),
			},

			//
			// some cases with directly and indirectly vanishable nonterminals
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("abc"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of("abc", "dummyStart"),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("abc", "def", "abc", "def"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("def", "def"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("def", ImmutableList.of(
						new Alternative(ImmutableList.of("bar"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of("abc", "def", "dummyStart"),
			},

			//
			// don't vanish a symbol through direct or indirect recursion
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("abc"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative(ImmutableList.of("abc"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of(),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("abc"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative(ImmutableList.of("def"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("def", ImmutableList.of(
						new Alternative(ImmutableList.of("abc"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of(),
			},

			//
			// test case where a vanishable symbol gets vanished in another symbol's right-hand side, but there
			// are other symbols left so the second once cannot vanish too
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative(ImmutableList.of("def", "foo"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative(ImmutableList.of("foo", "def"), null, AlternativeAnnotation.EMPTY)
					)),
					new NonterminalDefinition("def", ImmutableList.of(
						new Alternative(ImmutableList.of("foo"), null, AlternativeAnnotation.EMPTY),
						new Alternative(ImmutableList.of(), null, AlternativeAnnotation.EMPTY)
					))
				),
				ImmutableSet.of("def"),
			},

		};
	}

	@Test
	@UseDataProvider("getTestData")
	public void testWithTestData(ImmutableList<NonterminalDefinition> nonterminals, ImmutableSet<String> expectedResult) {
		Grammar grammar = new Grammar(TERMINALS, nonterminals, START_NONTERMINAL_NAME);
		ImmutableSet<String> result = VanishableNonterminalsHelper.runFor(grammar);
		Assert.assertEquals(expectedResult, result);
	}

}
