package name.martingeisse.mapag.grammar.canonical.validation;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import org.junit.Assert;
import org.junit.Test;

import static name.martingeisse.mapag.grammar.canonical.TestGrammarObjects.*;

/**
 *
 */
public class GrammarValidatorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new GrammarValidator(null);
	}

	@Test
	public void testValidAndSubValidatorCalls() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, START_NONTERMINAL_NAME);
		MyGrammarValidator validator = new MyGrammarValidator(grammar);
		validator.validate();
		Assert.assertEquals(1, validator.validateAssociativityConsistencyCallCount);
		Assert.assertEquals(1, validator.validateNonterminalDefinitionsCallCount);
	}

	@Test(expected = IllegalStateException.class)
	public void testTerminalNonterminalNameIntersection() {
		NonterminalDefinition conflictingNonterminal = new NonterminalDefinition(TERMINAL_1.getName(), NONTERMINAL_1.getAlternatives());
		ImmutableList<NonterminalDefinition> nonterminals = ImmutableList.of(NONTERMINAL_1, NONTERMINAL_2, NONTERMINAL_3, conflictingNonterminal);
		Grammar grammar = new Grammar(TERMINALS, nonterminals, START_NONTERMINAL_NAME);
		new MyGrammarValidator(grammar).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testUndefinedStartSymbol() {
		Grammar grammar = new Grammar(TERMINALS, NONTERMINALS, "anotherStart");
		MyGrammarValidator validator = new MyGrammarValidator(grammar);
		validator.validate();
	}

	private static class MyGrammarValidator extends GrammarValidator {

		int validateAssociativityConsistencyCallCount = 0;
		int validateNonterminalDefinitionsCallCount = 0;

		MyGrammarValidator(Grammar grammar) {
			super(grammar);
		}

		@Override
		protected void validateAssociativityConsistency() {
			validateAssociativityConsistencyCallCount++;
		}

		@Override
		protected void validateNonterminalDefinitions() {
			validateNonterminalDefinitionsCallCount++;
		}

	}

}
