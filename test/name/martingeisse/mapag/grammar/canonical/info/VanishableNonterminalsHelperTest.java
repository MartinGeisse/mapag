package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.canonical.*;
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
						new Alternative("a1", TestUtil.expansion("foo"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of(),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("nt1", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo", "bar"), null, false),
						new Alternative("a2", TestUtil.expansion("foo", "baz"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("nt2", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("baz"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("nt1", "nt2", "foo"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of(),
			},

			//
			// some cases with vanishable nonterminals
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion(), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of("dummyStart"),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion(), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of("dummyStart"),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion("baz"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("bar"), null, false),
						new Alternative("a2", TestUtil.expansion(), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of("abc"),
			},

			//
			// some cases with directly and indirectly vanishable nonterminals
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion("abc"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("bar"), null, false),
						new Alternative("a2", TestUtil.expansion(), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of("abc", "dummyStart"),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion("abc", "def", "abc", "def"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("bar"), null, false),
						new Alternative("a2", TestUtil.expansion("def", "def"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("def", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("bar"), null, false),
						new Alternative("a2", TestUtil.expansion(), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of("abc", "def", "dummyStart"),
			},

			//
			// don't vanish a symbol through direct or indirect recursion
			//

			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion("abc"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("abc"), null, false),
						new Alternative("a2", TestUtil.expansion("foo"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
				),
				ImmutableSet.of(),
			},
			{
				ImmutableList.of(
					new NonterminalDefinition("dummyStart", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion("abc"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("def"), null, false),
						new Alternative("a2", TestUtil.expansion("foo"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("def", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("abc"), null, false),
						new Alternative("a2", TestUtil.expansion("foo"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
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
						new Alternative("a1", TestUtil.expansion("def", "foo"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("abc", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo", "def"), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL),
					new NonterminalDefinition("def", ImmutableList.of(
						new Alternative("a1", TestUtil.expansion("foo"), null, false),
						new Alternative("a2", TestUtil.expansion(), null, false)
					), NonterminalDefinition.PsiStyle.NORMAL)
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
