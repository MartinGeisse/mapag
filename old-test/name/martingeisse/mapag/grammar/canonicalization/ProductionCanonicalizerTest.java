package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.canonical.AlternativeAnnotation;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.expression.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class ProductionCanonicalizerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new ProductionCanonicalizer(null);
	}

	@Test
	public void testConstructorEmptyAllowed() {
		ProductionCanonicalizer productionCanonicalizer = new ProductionCanonicalizer(ImmutableList.of());
		productionCanonicalizer.run();
		Assert.assertTrue(productionCanonicalizer.getNonterminalAlternatives().isEmpty());
	}

	@DataProvider
	public static Object[][] getTestCanonicalizationData() {
		return new Object[][]{

			//
			// basic tests (productions / alternatives / sequences)
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null, new EmptyExpression(), "xyz", null)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), "xyz", new AlternativeAnnotation("a1", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null, new SymbolReference("foo"), "xyz")
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo"), "xyz", new AlternativeAnnotation("a1", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), "xyz")
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "bar"), "xyz", new AlternativeAnnotation("a1", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), null
						),
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz")), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "bar"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("baz", "bazz"), "xyz", new AlternativeAnnotation("a2", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), null
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz")), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "bar"), null, new AlternativeAnnotation("a1", null))
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("baz", "bazz"), "xyz", new AlternativeAnnotation("a1", null))
					)
				)
			},


			//
			// empty expression
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new EmptyExpression(), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), "xyz", new AlternativeAnnotation("a1", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("foo"),
									new EmptyExpression()
								), new SymbolReference("bar")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "bar"), "xyz", new AlternativeAnnotation("a1", null))
					)
				)
			},


			//
			// optionals
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OptionalExpression(new SymbolReference("foo")), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo"), null, new AlternativeAnnotation("a2", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new OptionalExpression(new SymbolReference("bar"))), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "nt1//1"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bar"), null, new AlternativeAnnotation("a2", null))
					)
				)
			},


			//
			// zero-or-more repetition
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("foo"),
									new ZeroOrMoreExpression(new SymbolReference("bar"))
								),
								new SymbolReference("baz")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "nt1//1", "baz"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1", "nt1//2"), null, new AlternativeAnnotation("a2", null))
					),
					"nt1//2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bar"), null, new AlternativeAnnotation("a1", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("aaa"),
									new ZeroOrMoreExpression(
										new SequenceExpression(
											new SymbolReference("bbb"),
											new SymbolReference("ccc")
										)
									)
								),
								new SymbolReference("ddd")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa", "nt1//1", "ddd"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1", "nt1//2"), null, new AlternativeAnnotation("a2", null))
					),
					"nt1//2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bbb", "ccc"), null, new AlternativeAnnotation("a1", null))
					)
				)
			},


			//
			// one-or-more repetition
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("foo"),
									new OneOrMoreExpression(new SymbolReference("bar"))
								),
								new SymbolReference("baz")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("foo", "nt1//1", "baz"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//2"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1", "nt1//2"), null, new AlternativeAnnotation("a2", null))
					),
					"nt1//2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bar"), null, new AlternativeAnnotation("a1", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("aaa"),
									new OneOrMoreExpression(
										new SequenceExpression(
											new SymbolReference("bbb"),
											new SymbolReference("ccc")
										)
									)
								),
								new SymbolReference("ddd")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa", "nt1//1", "ddd"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//2"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1", "nt1//2"), null, new AlternativeAnnotation("a2", null))
					),
					"nt1//2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bbb", "ccc"), null, new AlternativeAnnotation("a1", null))
					)
				)
			},


			//
			// nested OR expression (as opposed to top-level "alternatives")
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("aaa"),
									new OrExpression(
										new SymbolReference("bbb"),
										new SymbolReference("ccc")
									)
								),
								new SymbolReference("ddd")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa", "nt1//1", "ddd"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bbb"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("ccc"), null, new AlternativeAnnotation("a2", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new SequenceExpression(
									new SymbolReference("aaa"),
									new OrExpression(
										new SequenceExpression(new SymbolReference("bbb"), new SymbolReference("ccc")),
										new SequenceExpression(new SymbolReference("ddd"), new SymbolReference("eee"))
									)
								),
								new SymbolReference("fff")
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa", "nt1//1", "fff"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bbb", "ccc"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("ddd", "eee"), null, new AlternativeAnnotation("a2", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OrExpression(
								new SequenceExpression(new SymbolReference("aaa"), new SymbolReference("bbb")),
								new SequenceExpression(new SymbolReference("ccc"), new SymbolReference("ddd"))
							), "xyz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1"), "xyz", new AlternativeAnnotation("a1", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa", "bbb"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("ccc", "ddd"), null, new AlternativeAnnotation("a2", null))
					)
				)
			},


			//
			// merging multiple alternatives for the same nonterminal
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OneOrMoreExpression(new SymbolReference("aaa")), "xxx"
						),
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new OptionalExpression(new SymbolReference("ccc")),
								new SymbolReference("ddd")
							),
							"zzz"
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SymbolReference("bbb"), "yyy"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1"), "xxx", new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//3", "ddd"), "zzz", new AlternativeAnnotation("a2", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//2"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1", "nt1//2"), null, new AlternativeAnnotation("a2", null))
					),
					"nt1//2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa"), null, new AlternativeAnnotation("a1", null))
					),
					"nt1//3", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("ccc"), null, new AlternativeAnnotation("a2", null))
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bbb"), "yyy", new AlternativeAnnotation("a1", null))
					)
				)
			},

			//
			// merging multiple productions for the same nonterminal
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OneOrMoreExpression(new SymbolReference("aaa")), "xxx"
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SymbolReference("bbb"), "yyy"
						)
					)),
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new OptionalExpression(new SymbolReference("ccc")),
								new SymbolReference("ddd")
							),
							"zzz"
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1"), "xxx", new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//3", "ddd"), "zzz", new AlternativeAnnotation("a2", null))
					),
					"nt1//1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//2"), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("nt1//1", "nt1//2"), null, new AlternativeAnnotation("a2", null))
					),
					"nt1//2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("aaa"), null, new AlternativeAnnotation("a1", null))
					),
					"nt1//3", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("ccc"), null, new AlternativeAnnotation("a2", null))
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.of("bbb"), "yyy", new AlternativeAnnotation("a1", null))
					)
				)
			},

		};
	}

	@Test
	@UseDataProvider("getTestCanonicalizationData")
	public void testCanonicalization(ImmutableList<Production> inputProductions, Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> expectedOutputAlternatives) {
		ProductionCanonicalizer productionCanonicalizer = new ProductionCanonicalizer(inputProductions);
		productionCanonicalizer.run();

		// cannot use equals() on the whole collection since we want to compare output alternatives by data, but their
		// equals() compares identity
		Assert.assertEquals(expectedOutputAlternatives.keySet(), productionCanonicalizer.getNonterminalAlternatives().keySet());
		for (String key : expectedOutputAlternatives.keySet()) {
			List<name.martingeisse.mapag.grammar.canonical.Alternative> expectedAlternativeList = expectedOutputAlternatives.get(key);
			List<name.martingeisse.mapag.grammar.canonical.Alternative> actualAlternativeList = productionCanonicalizer.getNonterminalAlternatives().get(key);
			Assert.assertEquals(expectedAlternativeList.size(), actualAlternativeList.size());
			for (int i=0; i<expectedAlternativeList.size(); i++) {
				name.martingeisse.mapag.grammar.canonical.Alternative expectedAlternative = expectedAlternativeList.get(i);
				name.martingeisse.mapag.grammar.canonical.Alternative actualAlternative = actualAlternativeList.get(i);
				Assert.assertEquals(expectedAlternative.getExpansion(), actualAlternative.getExpansion());
				Assert.assertEquals(expectedAlternative.getEffectivePrecedenceTerminal(), actualAlternative.getEffectivePrecedenceTerminal());
			}
		}

	}

}
