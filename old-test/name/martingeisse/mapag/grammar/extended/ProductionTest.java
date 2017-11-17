package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 *
 */
public class ProductionTest {

	private static final Alternative ALTERNATIVE = new Alternative(null, new SymbolReference("foo"), null);
	private static final ImmutableList<Alternative> ALTERNATIVES = ImmutableList.of(ALTERNATIVE);

	@Test(expected = IllegalArgumentException.class)
	public void testNullLeftHandSide() {
		new Production(null, ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyLeftHandSide() {
		new Production("", ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEofSymbolOnLeftHandSide() {
		new Production(SpecialSymbols.EOF_SYMBOL_NAME, ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testErrorSymbolOnLeftHandSide() {
		new Production(SpecialSymbols.ERROR_SYMBOL_NAME, ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRootSymbolOnLeftHandSide() {
		new Production(SpecialSymbols.ROOT_SYMBOL_NAME, ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOtherSpecialSymbolOnLeftHandSide() {
		new Production("%whatever", ALTERNATIVES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullRightHandSide() {
		new Production("nt1", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyRightHandSide() {
		new Production("nt1", ImmutableList.of());
	}

	@Test
	public void testConstructorGetter() {
		Production production = new Production("nt1", ALTERNATIVES);
		Assert.assertSame("nt1", production.getLeftHandSide());
		Assert.assertSame(ALTERNATIVES, production.getAlternatives());
	}

}
