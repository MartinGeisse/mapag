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
		NonterminalDefinition nonterminalDefinition = new NonterminalDefinition("nt", ALTERNATIVES, PsiStyle.Normal.INSTANCE);
		Assert.assertEquals("nt", nonterminalDefinition.getName());
		Assert.assertEquals(ALTERNATIVES, nonterminalDefinition.getAlternatives());
		Assert.assertEquals(PsiStyle.Normal.INSTANCE, nonterminalDefinition.getPsiStyle());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new NonterminalDefinition(null, ALTERNATIVES, PsiStyle.Normal.INSTANCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new NonterminalDefinition("", ALTERNATIVES, PsiStyle.Normal.INSTANCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAlternatives() {
		new NonterminalDefinition("nt", null, PsiStyle.Normal.INSTANCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyAlternatives() {
		new NonterminalDefinition("nt", ImmutableList.of(), PsiStyle.Normal.INSTANCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPercentSignInNameNotAllowed() {
		new NonterminalDefinition("%foo", ALTERNATIVES, PsiStyle.Normal.INSTANCE);
	}

	@Test
	public void testDifferentPsiStyle() {
		NonterminalDefinition nonterminalDefinition = new NonterminalDefinition("nt", ALTERNATIVES, new PsiStyle.Optional("foo"));
		Assert.assertEquals("nt", nonterminalDefinition.getName());
		Assert.assertEquals(ALTERNATIVES, nonterminalDefinition.getAlternatives());
		Assert.assertTrue(nonterminalDefinition.getPsiStyle() instanceof PsiStyle.Optional);
	}

}
