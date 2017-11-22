package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveBlock;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.expression.SequenceExpression;
import name.martingeisse.mapag.grammar.extended.expression.SymbolReference;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ProductionValidatorImplTest {

	private static final ExpressionValidator NOP_EXPRESSION_VALIDATOR = e -> {
	};
	private static final ImmutableSet<String> TERMINALS = ImmutableSet.of("foo", "bar");
	private static final ImmutableSet<String> NONTERMINALS = ImmutableSet.of("abc", "def");
	private static final Alternative ALTERNATIVE_1 = new Alternative(null, new SymbolReference("foo"), null, null);
	private static final Alternative ALTERNATIVE_2 = new Alternative(null, new SequenceExpression(new SymbolReference("abc"), new SymbolReference("bar")), null, null);

	@Test(expected = IllegalArgumentException.class)
	public void testNullTerminals() {
		new ProductionValidatorImpl(null, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyTerminals() {
		new ProductionValidatorImpl(ImmutableSet.of(), NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTerminalsContainsEmpty() {
		new ProductionValidatorImpl(ImmutableSet.of("foo", "", "bar"), NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullNonterminals() {
		new ProductionValidatorImpl(TERMINALS, null, "abc", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyNonterminals() {
		new ProductionValidatorImpl(TERMINALS, ImmutableSet.of(), "abc", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonterminalsContainsEmpty() {
		new ProductionValidatorImpl(TERMINALS, ImmutableSet.of("abc", "", "def"), "abc", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullStartSymbol() {
		new ProductionValidatorImpl(TERMINALS, NONTERMINALS, null, NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyStartSymbol() {
		new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "", NOP_EXPRESSION_VALIDATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullExpressionValidator() {
		new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", null);
	}

	@Test
	public void testValid() {
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_1)));
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_2)));
		productionValidator.finish();
	}

	@Test(expected = IllegalStateException.class)
	public void testNoProductionForStartSymbol() {
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "def", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_1)));
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_2)));
		productionValidator.finish();
	}

	@Test
	public void testNoProductionForStartSymbolGetsCheckedInFinishMethod() {
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "def", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_1)));
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_2)));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownLeftHandSideInProduction() {
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("xyz", ImmutableList.of(ALTERNATIVE_1)));
	}

	@Test
	public void testExpressionValidatorCalled() {
		ExpressionValidator expressionValidator = new ExpressionValidator() {

			private int counter = 0;

			@Override
			public void validateExpression(Expression expression) {
				if (counter == 0) {
					Assert.assertSame(ALTERNATIVE_1.getExpression(), expression);
				} else if (counter == 1) {
					Assert.assertSame(ALTERNATIVE_2.getExpression(), expression);
				} else {
					Assert.fail("invalid counter value: " + counter);
				}
				counter++;
			}

		};
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", expressionValidator);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_1)));
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(ALTERNATIVE_2)));
		productionValidator.finish();
	}

	@Test
	public void testValidPrecedenceDefiningTerminal() {
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(new Alternative(null, new SymbolReference("foo"), "foo", null))));
		productionValidator.finish();
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownPrecedenceDefiningTerminal() {
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(new Alternative(null, new SymbolReference("foo"), "blub", null))));
		productionValidator.finish();
	}

	@Test
	public void testValidResolveBlock() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of(new ResolveDeclaration(ConflictResolution.SHIFT, ImmutableList.of("foo"))));
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(new Alternative(null, new SymbolReference("foo"), null, resolveBlock))));
		productionValidator.finish();
	}

	@Test(expected = IllegalStateException.class)
	public void testResolveBlockWithUnknownTerminal() {
		ResolveBlock resolveBlock = new ResolveBlock(ImmutableList.of(new ResolveDeclaration(ConflictResolution.SHIFT, ImmutableList.of("blub"))));
		ProductionValidator productionValidator = new ProductionValidatorImpl(TERMINALS, NONTERMINALS, "abc", NOP_EXPRESSION_VALIDATOR);
		productionValidator.validateProduction(new Production("abc", ImmutableList.of(new Alternative(null, new SymbolReference("foo"), null, resolveBlock))));
		productionValidator.finish();
	}

}
