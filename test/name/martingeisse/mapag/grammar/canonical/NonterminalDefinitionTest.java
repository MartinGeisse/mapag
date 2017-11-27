package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class NonterminalDefinitionTest {

	private static final Alternative ALTERNATIVE_1 = new Alternative("a1", TestUtil.expansion("foo", "bar"), AlternativeAttributes.EMPTY);
	private static final Alternative ALTERNATIVE_2 = new Alternative("a2", TestUtil.expansion("a", "b"), AlternativeAttributes.EMPTY);
	private static final ImmutableList<Alternative> ALTERNATIVES = ImmutableList.of(ALTERNATIVE_1, ALTERNATIVE_2);

	@Test
	public void testConstructorGetter() {
		NonterminalDefinition nonterminalDefinition = new NonterminalDefinition("nt", ALTERNATIVES, NonterminalDefinition.PsiStyle.NORMAL);
		Assert.assertEquals("nt", nonterminalDefinition.getName());
		Assert.assertEquals(ALTERNATIVES, nonterminalDefinition.getAlternatives());
		Assert.assertEquals(NonterminalDefinition.PsiStyle.NORMAL, nonterminalDefinition.getPsiStyle());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new NonterminalDefinition(null, ALTERNATIVES, NonterminalDefinition.PsiStyle.NORMAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new NonterminalDefinition("", ALTERNATIVES, NonterminalDefinition.PsiStyle.NORMAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAlternatives() {
		new NonterminalDefinition("nt", null, NonterminalDefinition.PsiStyle.NORMAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyAlternatives() {
		new NonterminalDefinition("nt", ImmutableList.of(), NonterminalDefinition.PsiStyle.NORMAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPercentSignInNameNotAllowed() {
		new NonterminalDefinition("%foo", ALTERNATIVES, NonterminalDefinition.PsiStyle.NORMAL);
	}

	@Test
	public void testDifferentPsiStyle() {
		NonterminalDefinition nonterminalDefinition = new NonterminalDefinition("nt", ALTERNATIVES, NonterminalDefinition.PsiStyle.OPTIONAL);
		Assert.assertEquals("nt", nonterminalDefinition.getName());
		Assert.assertEquals(ALTERNATIVES, nonterminalDefinition.getAlternatives());
		Assert.assertEquals(NonterminalDefinition.PsiStyle.OPTIONAL, nonterminalDefinition.getPsiStyle());
	}

}
