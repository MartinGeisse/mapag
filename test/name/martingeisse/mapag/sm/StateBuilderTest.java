package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class StateBuilderTest {

	private static final String PACKAGE_NAME = "foo.bar";
	private static final String CLASS_NAME = "MyClass";
	private static final String START_NONTERMINAL_NAME = "startSymbol";

	private static final TerminalDefinition TERMINAL_1 = new TerminalDefinition("t1", null, Associativity.NONASSOC);
	private static final TerminalDefinition TERMINAL_2 = new TerminalDefinition("t2", null, Associativity.NONASSOC);
	private static final TerminalDefinition TERMINAL_3 = new TerminalDefinition("t3", 3, Associativity.RIGHT);

	private static final NonterminalDefinition START_SYMBOL_1 = new NonterminalDefinition("startSymbol", ImmutableList.of(
			new Alternative(ImmutableList.of("t1"), null)
	));

	private static final NonterminalDefinition START_SYMBOL_2 = new NonterminalDefinition("startSymbol", ImmutableList.of(
			new Alternative(ImmutableList.of("t1"), null),
			new Alternative(ImmutableList.of("t2"), null)
	));

	private static final NonterminalDefinition START_SYMBOL_3 = new NonterminalDefinition("startSymbol", ImmutableList.of(
			new Alternative(ImmutableList.of("t3"), null),
			new Alternative(ImmutableList.of("nt1"), null)
	));

	private static final NonterminalDefinition NONTERMINAL_1 = new NonterminalDefinition("nt1", ImmutableList.of(
			new Alternative(ImmutableList.of("t1"), null)
	));





	//
	// ----------------------------------------------------------------------------------------------------------------
	//

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new StateBuilder(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testCantWorkWithoutRootElements() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, ImmutableSet.of(TERMINAL_1), ImmutableSet.of(START_SYMBOL_1), START_NONTERMINAL_NAME);
		GrammarInfo grammarInfo = new GrammarInfo(grammar);
		new StateBuilder(grammarInfo).build();
	}

	@DataProvider
	public static Object[][] getTestData() {
		return new Object[][]{

				// just START_SYMBOL_1
				{
						ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
						ImmutableList.of(START_SYMBOL_1),
						ImmutableSet.of(new StateElement("startSymbol", START_SYMBOL_1.getAlternatives().get(0), 0, "%eof")),
						ImmutableSet.of(),
				},

				// START_SYMBOL_1, but starting from a dummy symbol that has this on the RHS
				{
						ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
						ImmutableList.of(START_SYMBOL_1),
						ImmutableSet.of(new StateElement("dummy", new Alternative(ImmutableList.of("startSymbol"), null), 0, "%eof")),
						ImmutableSet.of(new StateElement("startSymbol", START_SYMBOL_1.getAlternatives().get(0), 0, "%eof")),
				},

				// START_SYMBOL_2, but starting from a dummy symbol that has this on the RHS
				{
						ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
						ImmutableList.of(START_SYMBOL_2),
						ImmutableSet.of(new StateElement("dummy", new Alternative(ImmutableList.of("startSymbol"), null), 0, "%eof")),
						ImmutableSet.of(
								new StateElement("startSymbol", START_SYMBOL_2.getAlternatives().get(0), 0, "%eof"),
								new StateElement("startSymbol", START_SYMBOL_2.getAlternatives().get(1), 0, "%eof")
						),
				},

				// START_SYMBOL_3 with a dummy symbol (double-indirection)
				{
						ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
						ImmutableList.of(START_SYMBOL_3, NONTERMINAL_1),
						ImmutableSet.of(new StateElement("dummy", new Alternative(ImmutableList.of("startSymbol"), null), 0, "%eof")),
						ImmutableSet.of(
								new StateElement("startSymbol", START_SYMBOL_3.getAlternatives().get(0), 0, "%eof"),
								new StateElement("startSymbol", START_SYMBOL_3.getAlternatives().get(1), 0, "%eof"),
								new StateElement("nt1", NONTERMINAL_1.getAlternatives().get(0), 0, "%eof")
						),
				},


				// TODO
		};
	}

	@Test
	@UseDataProvider("getTestData")
	public void testWithData(ImmutableList<TerminalDefinition> terminals,
							 ImmutableList<NonterminalDefinition> nonterminals,
							 ImmutableSet<StateElement> rootElements,
							 ImmutableSet<StateElement> additionalElements) {

		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, terminals, nonterminals, START_NONTERMINAL_NAME);
		GrammarInfo grammarInfo = new GrammarInfo(grammar);

		StateBuilder builder = new StateBuilder(grammarInfo);
		Assert.assertTrue(builder.isEmpty());
		for (StateElement rootElement : rootElements) {
			builder.addElementClosure(rootElement);
			Assert.assertFalse(builder.isEmpty());
		}
		State actualState = builder.build();

		State expectedState = new State(ImmutableSet.<StateElement>builder().addAll(rootElements).addAll(additionalElements).build());
		Assert.assertEquals(expectedState, actualState);
	}

}
