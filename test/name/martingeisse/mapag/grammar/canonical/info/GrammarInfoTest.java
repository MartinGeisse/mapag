package name.martingeisse.mapag.grammar.canonical.info;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import org.junit.Assert;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.*;

/**
 *
 */
public class GrammarInfoTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		new GrammarInfo(null);
	}

	@Test
	public void testNormalUsage() {
		Grammar grammar = new Grammar(PACKAGE_NAME, CLASS_NAME, TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		GrammarInfo grammarInfo = new GrammarInfo(grammar);
		Assert.assertEquals(grammar, grammarInfo.getGrammar());

		Assert.assertEquals(ImmutableSet.of("nt1"), grammarInfo.getVanishableNonterminals());
		Assert.assertTrue(grammarInfo.isSentenceVanishable(ImmutableList.of()));
		Assert.assertTrue(grammarInfo.isSentenceVanishable(ImmutableList.of("nt1")));
		Assert.assertTrue(grammarInfo.isSentenceVanishable(ImmutableList.of("nt1", "nt1")));
		Assert.assertFalse(grammarInfo.isSentenceVanishable(ImmutableList.of("nt2")));
		Assert.assertFalse(grammarInfo.isSentenceVanishable(ImmutableList.of("foo")));
		Assert.assertFalse(grammarInfo.isSentenceVanishable(ImmutableList.of("nt1", "nt2")));
		Assert.assertFalse(grammarInfo.isSentenceVanishable(ImmutableList.of("nt1", "foo")));

		ImmutableMap expectedFirstSets = ImmutableMap.of(
				"nt1", ImmutableSet.of("foo"),
				"nt2", ImmutableSet.of("foo"),
				"dummyStart", ImmutableSet.of("foo")
		);
		Assert.assertEquals(expectedFirstSets, grammarInfo.getFirstSets());
		Assert.assertEquals(ImmutableSet.of(), grammarInfo.determineFirstSetForSentence(ImmutableList.of()));
		Assert.assertEquals(ImmutableSet.of("foo"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("foo")));
		Assert.assertEquals(ImmutableSet.of("foo"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("foo", "bar")));
		Assert.assertEquals(ImmutableSet.of("foo"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("nt1")));
		Assert.assertEquals(ImmutableSet.of("foo"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("nt1", "nt1")));
		Assert.assertEquals(ImmutableSet.of("foo", "bar"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("nt1", "bar")));
		Assert.assertEquals(ImmutableSet.of("bar"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("bar", "nt1")));
		Assert.assertEquals(ImmutableSet.of("foo"), grammarInfo.determineFirstSetForSentence(ImmutableList.of("nt2", "bar")));

	}

}
