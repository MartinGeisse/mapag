package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.AlternativeConflictResolver;
import name.martingeisse.mapag.grammar.canonical.TestUtil;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveBlock;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
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
	public void testConstructorNullTerminals() {
		new ProductionCanonicalizer(null, ImmutableList.of());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullProductions() {
		new ProductionCanonicalizer(ImmutableList.of(), null);
	}

	@Test
	public void testConstructorEmptyAllowed() {
		ProductionCanonicalizer productionCanonicalizer = new ProductionCanonicalizer(ImmutableList.of(), ImmutableList.of());
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
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion(), new AlternativeConflictResolver("xyz", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null, new SymbolReference("foo"), "xyz", null)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo"), new AlternativeConflictResolver("xyz", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), "xyz", null)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), new AlternativeConflictResolver("xyz", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), null, null
						),
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz")), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("baz", "bazz"), new AlternativeConflictResolver("xyz", null))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), null, null
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz")), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), null)
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("baz", "bazz"), new AlternativeConflictResolver("xyz", null))
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
							new EmptyExpression(), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion(), new AlternativeConflictResolver("xyz", null))
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), new AlternativeConflictResolver("xyz", null))
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
							new OptionalExpression(new SymbolReference("foo")), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("foo", "it"), null)
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new OptionalExpression(new SymbolReference("bar"))), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "nt1/1"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("bar", "it"), null)
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OptionalExpression(new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar"))), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/2"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("foo", "bar"), null)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("nt1/1", "it"), null)
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OptionalExpression(
								new OrExpression(
									new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")),
									new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz"))
								)
							)
							, "xyz", null)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/2"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("foo", "bar"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("baz", "bazz"), null)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("nt1/1", "it"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "nt1/1", "baz"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "bar", "element"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/2", "ddd"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb", "ccc"), null)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/2", "previous", "nt1/1", "element"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "nt1/1", "baz"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("bar", "element"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "bar", "element"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/2", "ddd"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb", "ccc"), null)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("nt1/1", "element"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/2", "previous", "nt1/1", "element"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/1", "ddd"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("ccc"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/1", "fff"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb", "ccc"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("ddd", "eee"), null)
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
							), "xyz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeConflictResolver("xyz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "bbb"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("ccc", "ddd"), null)
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
							new OneOrMoreExpression(new SymbolReference("aaa")), "xxx", null
						),
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new OptionalExpression(new SymbolReference("ccc")),
								new SymbolReference("ddd")
							),
							"zzz", null
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SymbolReference("bbb"), "yyy", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeConflictResolver("xxx", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("nt1/2", "ddd"), new AlternativeConflictResolver("zzz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("aaa", "element"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "aaa", "element"), null)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("ccc", "it"), null)
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb"), new AlternativeConflictResolver("yyy", null))
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
							new OneOrMoreExpression(new SymbolReference("aaa")), "xxx", null
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SymbolReference("bbb"), "yyy", null
						)
					)),
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new OptionalExpression(new SymbolReference("ccc")),
								new SymbolReference("ddd")
							),
							"zzz", null
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeConflictResolver("xxx", null)),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("nt1/2", "ddd"), new AlternativeConflictResolver("zzz", null))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("aaa", "element"), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "aaa", "element"), null)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), null),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("ccc", "it"), null)
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb"), new AlternativeConflictResolver("yyy", null))
					)
				)
			},

			//
			// resolve blocks
			//

			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null, new EmptyExpression(), null,
							new ResolveBlock(ImmutableList.of(
								new ResolveDeclaration(ConflictResolution.REDUCE, ImmutableList.of("aaa")),
								new ResolveDeclaration(ConflictResolution.SHIFT, ImmutableList.of("bbb", "ccc"))
							))
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion(),
							new AlternativeConflictResolver(null, ImmutableMap.of(
								"aaa", ConflictResolution.REDUCE,
								"bbb", ConflictResolution.SHIFT,
								"ccc", ConflictResolution.SHIFT
							))
						)
					)
				)
			},

		};
	}

	@Test
	@UseDataProvider("getTestCanonicalizationData")
	public void testCanonicalization(ImmutableList<Production> inputProductions, Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> expectedOutputAlternatives) {

		// the first parameter is an empty list because we don't care about name collisions between the generated
		// nonterminals and any terminals
		ProductionCanonicalizer productionCanonicalizer = new ProductionCanonicalizer(ImmutableList.of(), inputProductions);
		productionCanonicalizer.run();

		// cannot use equals() on the whole collection since we want to compare output alternatives by data, but their
		// equals() compares identity
		Assert.assertEquals(expectedOutputAlternatives.keySet(), productionCanonicalizer.getNonterminalAlternatives().keySet());
		for (String key : expectedOutputAlternatives.keySet()) {
			List<name.martingeisse.mapag.grammar.canonical.Alternative> expectedAlternativeList = expectedOutputAlternatives.get(key);
			List<name.martingeisse.mapag.grammar.canonical.Alternative> actualAlternativeList = productionCanonicalizer.getNonterminalAlternatives().get(key);
			Assert.assertEquals(expectedAlternativeList.size(), actualAlternativeList.size());
			for (int i = 0; i < expectedAlternativeList.size(); i++) {
				name.martingeisse.mapag.grammar.canonical.Alternative expectedAlternative = expectedAlternativeList.get(i);
				name.martingeisse.mapag.grammar.canonical.Alternative actualAlternative = actualAlternativeList.get(i);
				Assert.assertEquals(expectedAlternative.getExpansion(), actualAlternative.getExpansion());
				Assert.assertEquals(expectedAlternative.getConflictResolver(), actualAlternative.getConflictResolver());
			}
		}

	}

}
