package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class NonterminalDefinitionTest {

	private static final Alternative ALTERNATIVE_1 = new Alternative(ImmutableList.of("foo", "bar"), null, new AlternativeAnnotation("a1", null));
	private static final Alternative ALTERNATIVE_2 = new Alternative(ImmutableList.of("a", "b"), null, new AlternativeAnnotation("a2", null));
	private static final ImmutableList<Alternative> ALTERNATIVES = ImmutableList.of(ALTERNATIVE_1, ALTERNATIVE_2);

	@Test
	public void testConstructorGetter() {
		NonterminalDefinition nonterminalDefinition = new NonterminalDefinition("nt", ALTERNATIVES, NonterminalAnnotation.EMPTY);
		Assert.assertEquals("nt", nonterminalDefinition.getName());
		Assert.assertEquals(ALTERNATIVES, nonterminalDefinition.getAlternatives());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new NonterminalDefinition(null, ALTERNATIVES, NonterminalAnnotation.EMPTY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyName() {
		new NonterminalDefinition("", ALTERNATIVES, NonterminalAnnotation.EMPTY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAlternatives() {
		new NonterminalDefinition("nt", null, NonterminalAnnotation.EMPTY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyAlternatives() {
		new NonterminalDefinition("nt", ImmutableList.of(), NonterminalAnnotation.EMPTY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPercentSignInNameNotAllowed() {
		new NonterminalDefinition("%foo", ALTERNATIVES, NonterminalAnnotation.EMPTY);
	}

}