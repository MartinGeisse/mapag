package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.AlternativeAttributes;
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
 * TODO tests are currently brokwn
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
						new name.martingeisse.mapag.grammar.extended.Alternative(null, new EmptyExpression(), "xyz", null, false, false)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion(), new AlternativeAttributes("xyz", null, false, false))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null, new SymbolReference("foo"), "xyz", null, false, false)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo"), new AlternativeAttributes("xyz", null, false, false))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), "xyz", null, false, false)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), new AlternativeAttributes("xyz", null, false, false))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), null, null, false, false
						),
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz")), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("baz", "bazz"), new AlternativeAttributes("xyz", null, false, false))
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")), null, null, false, false
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("baz"), new SymbolReference("bazz")), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"), AlternativeAttributes.EMPTY)
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("baz", "bazz"), new AlternativeAttributes("xyz", null, false, false))
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
							new EmptyExpression(), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion(), new AlternativeAttributes("xyz", null, false, false))
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "bar"),
							new AlternativeAttributes("xyz", null, false, false))
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
							new OptionalExpression(new SymbolReference("foo")), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("foo", "it"), AlternativeAttributes.EMPTY)
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(new SymbolReference("foo"), new OptionalExpression(new SymbolReference("bar"))), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "nt1/1"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("bar", "it"), AlternativeAttributes.EMPTY)
					)
				)
			},
			{
				ImmutableList.of(
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new OptionalExpression(new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar"))), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/2"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("foo", "bar"), AlternativeAttributes.EMPTY)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("nt1/1", "it"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/2"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("foo", "bar"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("baz", "bazz"), AlternativeAttributes.EMPTY)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("nt1/1", "it"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "nt1/1", "baz"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "bar", "element"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/2", "ddd"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb", "ccc"), AlternativeAttributes.EMPTY)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/2", "previous", "nt1/1", "element"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("foo", "nt1/1", "baz"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("bar", "element"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "bar", "element"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/2", "ddd"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb", "ccc"), AlternativeAttributes.EMPTY)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("nt1/1", "element"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/2", "previous", "nt1/1", "element"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/1", "ddd"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("ccc"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "nt1/1", "fff"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb", "ccc"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("ddd", "eee"), AlternativeAttributes.EMPTY)
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
							), "xyz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeAttributes("xyz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("aaa", "bbb"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("ccc", "ddd"), AlternativeAttributes.EMPTY)
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
							new OneOrMoreExpression(new SymbolReference("aaa")), "xxx", null, false, false
						),
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new OptionalExpression(new SymbolReference("ccc")),
								new SymbolReference("ddd")
							),
							"zzz", null, false, false
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SymbolReference("bbb"), "yyy", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeAttributes("xxx", null, false, false)),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("nt1/2", "ddd"), new AlternativeAttributes("zzz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("aaa", "element"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "aaa", "element"), AlternativeAttributes.EMPTY)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("ccc", "it"), AlternativeAttributes.EMPTY)
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb"), new AlternativeAttributes("yyy", null, false, false))
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
							new OneOrMoreExpression(new SymbolReference("aaa")), "xxx", null, false, false
						)
					)),
					new Production("nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SymbolReference("bbb"), "yyy", null, false, false
						)
					)),
					new Production("nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.extended.Alternative(null,
							new SequenceExpression(
								new OptionalExpression(new SymbolReference("ccc")),
								new SymbolReference("ddd")
							),
							"zzz", null, false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("nt1/1"), new AlternativeAttributes("xxx", null, false, false)),
						new name.martingeisse.mapag.grammar.canonical.Alternative("a2", TestUtil.expansion("nt1/2", "ddd"), new AlternativeAttributes("zzz", null, false, false))
					),
					"nt1/1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("start", TestUtil.expansionWithNames("aaa", "element"), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("next", TestUtil.expansionWithNames("nt1/1", "previous", "aaa", "element"), AlternativeAttributes.EMPTY)
					),
					"nt1/2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("absent", TestUtil.expansion(), AlternativeAttributes.EMPTY),
						new name.martingeisse.mapag.grammar.canonical.Alternative("present", TestUtil.expansionWithNames("ccc", "it"), AlternativeAttributes.EMPTY)
					),
					"nt2", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion("bbb"), new AlternativeAttributes("yyy", null, false, false))
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
							)), false, false
						)
					))
				),
				ImmutableMap.of(
					"nt1", ImmutableList.of(
						new name.martingeisse.mapag.grammar.canonical.Alternative("a1", TestUtil.expansion(),
							new AlternativeAttributes(null, ImmutableMap.of(
								"aaa", ConflictResolution.REDUCE,
								"bbb", ConflictResolution.SHIFT,
								"ccc", ConflictResolution.SHIFT
							), false, false)
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
				Assert.assertEquals(expectedAlternative.getAttributes(), actualAlternative.getAttributes());
			}
		}

	}

}
