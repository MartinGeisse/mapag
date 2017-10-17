package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class NonterminalDefinitionTest {

	// TODO precedence
	private static final Alternative ALTERNATIVE_1 = new Alternative(ImmutableList.of("foo", "bar"), null);
	private static final Alternative ALTERNATIVE_2 = new Alternative(ImmutableList.of("a", "b"), null);
	private static final ImmutableList<Alternative> ALTERNATIVES = ImmutableList.of(ALTERNATIVE_1, ALTERNATIVE_2);

	@Test
	public void testConstructorGetter() {
		NonterminalDefinition nonterminalDefinition = new NonterminalDefinition("nt", ALTERNATIVES);
		Assert.assertEquals("nt", nonterminalDefinition.getName());
		Assert.assertEquals(ALTERNATIVES, nonterminalDefinition.getAlternatives());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		new NonterminalDefinition(null, ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAlternatives() {
		new NonterminalDefinition("nt", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyAlternatives() {
		new NonterminalDefinition("nt", ImmutableList.of());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPercentSignInNameNotAllowed() {
		new NonterminalDefinition("%foo", ALTERNATIVES);
	}

}
