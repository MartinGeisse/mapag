package name.martingeisse.mapag.grammar.extended.validation;

import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import name.martingeisse.mapag.grammar.extended.expression.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(DataProviderRunner.class)
public class ExpressionValidatorImplTest {

	private static final ImmutableSet<String> KNOWN_SYMBOLS = ImmutableSet.of("foo", "bar");

	@Test(expected = IllegalArgumentException.class)
	public void testNullKnownSymbols() {
		new ExpressionValidatorImpl(null);
	}

	@Test
	public void testEmptyKnownSymbols() {
		new ExpressionValidatorImpl(ImmutableSet.of());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testKnownSymbolsContainsEmpty() {
		new ExpressionValidatorImpl(ImmutableSet.of("foo", "", "bar"));
	}

	@DataProvider
	public static Object[][] getTestInvalidExpressionData() {
		return new Object[][]{
			{
				new SymbolReference("abc"),
			},
			{
				new OptionalExpression(new SymbolReference("abc")),
			},
			{
				new Repetition(new SymbolReference("abc"), null, false),
			},
			{
				new Repetition(new SymbolReference("abc"), null, true),
			},
			{
				new Repetition(new SymbolReference("abc"), new SymbolReference("foo"), false),
			},
			{
				new Repetition(new SymbolReference("abc"), new SymbolReference("foo"), true),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("abc"), false),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("abc"), true),
			},
			{
				new OrExpression(new SymbolReference("abc"), new SymbolReference("foo")),
			},
			{
				new OrExpression(new SymbolReference("foo"), new SymbolReference("abc")),
			},
			{
				new SequenceExpression(new SymbolReference("abc"), new SymbolReference("foo")),
			},
			{
				new SequenceExpression(new SymbolReference("foo"), new SymbolReference("abc")),
			},
			{
				new Repetition(new SymbolReference("abc"), null, false),
			},
			{
				new Repetition(new SymbolReference("abc"), null, true),
			},
			{
				new Repetition(new SymbolReference("abc"), new SymbolReference("foo"), false),
			},
			{
				new Repetition(new SymbolReference("abc"), new SymbolReference("foo"), true),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("abc"), false),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("abc"), true),
			}
		};
	}

	@Test(expected = IllegalStateException.class)
	@UseDataProvider("getTestInvalidExpressionData")
	public void testInvalidExpression(Expression expression) {
		new ExpressionValidatorImpl(KNOWN_SYMBOLS).validateExpression(expression, ErrorReporter.ForExpressions.EXCEPTION_THROWER);
	}

	@DataProvider
	public static Object[][] getTestValidExpressionData() {
		return new Object[][]{
			{
				new SymbolReference("foo"),
			},
			{
				new OptionalExpression(new SymbolReference("foo")),
			},
			{
				new Repetition(new SymbolReference("foo"), null, false),
			},
			{
				new Repetition(new SymbolReference("foo"), null, true),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("bar"), false),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("bar"), true),
			},
			{
				new OrExpression(new SymbolReference("foo"), new SymbolReference("bar")),
			},
			{
				new SequenceExpression(new SymbolReference("foo"), new SymbolReference("bar")),
			},
			{
				new EmptyExpression()
			},
			{
				new Repetition(new SymbolReference("foo"), null, false),
			},
			{
				new Repetition(new SymbolReference("foo"), null, true),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("bar"), false),
			},
			{
				new Repetition(new SymbolReference("foo"), new SymbolReference("bar"), true),
			}
		};
	}

	@Test
	@UseDataProvider("getTestValidExpressionData")
	public void testValidExpression(Expression expression) {
		new ExpressionValidatorImpl(KNOWN_SYMBOLS).validateExpression(expression, ErrorReporter.ForExpressions.EXCEPTION_THROWER);
	}

}
