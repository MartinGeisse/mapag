package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.*;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class StateBuilderTest {

	private static final String START_NONTERMINAL_NAME = "startSymbol";

	private static final TerminalDefinition TERMINAL_1 = new TerminalDefinition("t1", null, Associativity.NONASSOC);
	private static final TerminalDefinition TERMINAL_2 = new TerminalDefinition("t2", null, Associativity.NONASSOC);
	private static final TerminalDefinition TERMINAL_3 = new TerminalDefinition("t3", 3, Associativity.RIGHT);

	private static final NonterminalDefinition START_SYMBOL_1 = new NonterminalDefinition("startSymbol", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t1"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition START_SYMBOL_2 = new NonterminalDefinition("startSymbol", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t1"), null),
		new Alternative("a2", TestUtil.expansion("t2"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition START_SYMBOL_3 = new NonterminalDefinition("startSymbol", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t3"), null),
		new Alternative("a2", TestUtil.expansion("nt1"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition NONTERMINAL_1 = new NonterminalDefinition("nt1", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t1"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition NONTERMINAL_2 = new NonterminalDefinition("nt2", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t2"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition NONTERMINAL_3 = new NonterminalDefinition("nt3", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t1", "nt1", "t2", "t2"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition NONTERMINAL_4 = new NonterminalDefinition("nt4", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t1", "nt1", "t2", "t3"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	private static final NonterminalDefinition NONTERMINAL_5 = new NonterminalDefinition("nt5", ImmutableList.of(
		new Alternative("a1", TestUtil.expansion("t1", "nt1", "t3", "t3"), null)
	), NonterminalDefinition.PsiStyle.NORMAL);

	//
	// ----------------------------------------------------------------------------------------------------------------
	//

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new StateBuilder(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testCantWorkWithoutRootElements() {
		Grammar grammar = new Grammar(ImmutableSet.of(TERMINAL_1), ImmutableSet.of(START_SYMBOL_1), START_NONTERMINAL_NAME);
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
				ImmutableSet.of(new StateElement("dummy", new Alternative("a1", TestUtil.expansion("startSymbol"), null), 0, "%eof")),
				ImmutableSet.of(new StateElement("startSymbol", START_SYMBOL_1.getAlternatives().get(0), 0, "%eof")),
			},

			// START_SYMBOL_2, but starting from a dummy symbol that has this on the RHS
			{
				ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
				ImmutableList.of(START_SYMBOL_2),
				ImmutableSet.of(new StateElement("dummy", new Alternative("a1", TestUtil.expansion("startSymbol"), null), 0, "%eof")),
				ImmutableSet.of(
					new StateElement("startSymbol", START_SYMBOL_2.getAlternatives().get(0), 0, "%eof"),
					new StateElement("startSymbol", START_SYMBOL_2.getAlternatives().get(1), 0, "%eof")
				),
			},

			// START_SYMBOL_3 with a dummy symbol (double-indirection)
			{
				ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
				ImmutableList.of(START_SYMBOL_3, NONTERMINAL_1),
				ImmutableSet.of(new StateElement("dummy", new Alternative("a1", TestUtil.expansion("startSymbol"), null), 0, "%eof")),
				ImmutableSet.of(
					new StateElement("startSymbol", START_SYMBOL_3.getAlternatives().get(0), 0, "%eof"),
					new StateElement("startSymbol", START_SYMBOL_3.getAlternatives().get(1), 0, "%eof"),
					new StateElement("nt1", NONTERMINAL_1.getAlternatives().get(0), 0, "%eof")
				),
			},

			// next state does not include an element for which no nonterminal was reached
			{
				ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
				ImmutableList.of(START_SYMBOL_3, NONTERMINAL_1, NONTERMINAL_2),
				ImmutableSet.of(new StateElement("dummy", new Alternative("a1", TestUtil.expansion("startSymbol"), null), 0, "%eof")),
				ImmutableSet.of(
					new StateElement("startSymbol", START_SYMBOL_3.getAlternatives().get(0), 0, "%eof"),
					new StateElement("startSymbol", START_SYMBOL_3.getAlternatives().get(1), 0, "%eof"),
					new StateElement("nt1", NONTERMINAL_1.getAlternatives().get(0), 0, "%eof")
				),
			},

			// this test includes the same element twice, starting from two different previous elements
			{
				ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
				ImmutableList.of(START_SYMBOL_3, NONTERMINAL_1, NONTERMINAL_3, NONTERMINAL_4),
				ImmutableSet.of(
					new StateElement("nt3", NONTERMINAL_3.getAlternatives().get(0), 1, "%eof"),
					new StateElement("nt4", NONTERMINAL_4.getAlternatives().get(0), 1, "%eof")
				),
				ImmutableSet.of(
					new StateElement("nt1", NONTERMINAL_1.getAlternatives().get(0), 0, "t2")
				),
			},

			// similar, but this time it's not the same element twice, but two elements from different previous
			// elements that are equal except for the lookahead terminal
			{
				ImmutableList.of(TERMINAL_1, TERMINAL_2, TERMINAL_3),
				ImmutableList.of(START_SYMBOL_3, NONTERMINAL_1, NONTERMINAL_3, NONTERMINAL_5),
				ImmutableSet.of(
					new StateElement("nt3", NONTERMINAL_3.getAlternatives().get(0), 1, "%eof"),
					new StateElement("nt5", NONTERMINAL_5.getAlternatives().get(0), 1, "%eof")
				),
				ImmutableSet.of(
					new StateElement("nt1", NONTERMINAL_1.getAlternatives().get(0), 0, "t2"),
					new StateElement("nt1", NONTERMINAL_1.getAlternatives().get(0), 0, "t3")
				),
			},

		};
	}

	@Test
	@UseDataProvider("getTestData")
	public void testWithData(ImmutableList<TerminalDefinition> terminals,
							 ImmutableList<NonterminalDefinition> nonterminals,
							 ImmutableSet<StateElement> rootElements,
							 ImmutableSet<StateElement> additionalElements) {

		Grammar grammar = new Grammar(terminals, nonterminals, START_NONTERMINAL_NAME);
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
