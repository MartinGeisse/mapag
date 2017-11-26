package name.martingeisse.mapag.grammar.canonical.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class NonterminalDefinitionValidatorTest {

	private static final Map<String, TerminalDefinition> TERMINALS;

	static {
		Map<String, TerminalDefinition> terminals = new HashMap<>();
		terminals.put("a", new TerminalDefinition("a", null, Associativity.NONASSOC));
		terminals.put("b", new TerminalDefinition("b", null, Associativity.NONASSOC));
		terminals.put("c", new TerminalDefinition("c", 0, Associativity.LEFT));
		terminals.put("d", new TerminalDefinition("d", 1, Associativity.NONASSOC));
		terminals.put("e", new TerminalDefinition("e", 2, Associativity.RIGHT));
		terminals.put("f", new TerminalDefinition("f", 2, Associativity.RIGHT));
		TERMINALS = ImmutableMap.copyOf(terminals);
	}

	private static final Map<String, NonterminalDefinition> SIMPLE_NONTERMINALS = ImmutableMap.of(
		"x", new NonterminalDefinition("x", ImmutableList.of(new Alternative("a1", TestUtil.expansion(), null, false)), NonterminalDefinition.PsiStyle.NORMAL),
		"y", new NonterminalDefinition("y", ImmutableList.of(new Alternative("a2", TestUtil.expansion(), null, false)), NonterminalDefinition.PsiStyle.NORMAL)
	);

	@Test
	public void testEmptyEmpty() {
		new NonterminalDefinitionValidator(ImmutableMap.of(), ImmutableMap.of()).validate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyNull() {
		new NonterminalDefinitionValidator(ImmutableMap.of(), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullEmpty() {
		new NonterminalDefinitionValidator(null, ImmutableMap.of());
	}

	@Test
	public void testEmptyNonterminals() {
		new NonterminalDefinitionValidator(TERMINALS, ImmutableMap.of()).validate();
	}

	@Test
	public void testSimpleValidCase() {
		new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS).validate();
	}

	@Test
	public void testValidAlternatives() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion(), null, false));
		validator.validate(new Alternative("a1", TestUtil.expansion(), new AlternativeConflictResolver("a", null), false));
		validator.validate(new Alternative("a1", TestUtil.expansion(), new AlternativeConflictResolver("d", null), false));
		validator.validate(new Alternative("a1", TestUtil.expansion("a", "b"), null, false));
		validator.validate(new Alternative("a1", TestUtil.expansion("a", "b"), new AlternativeConflictResolver("d", null), false));
		validator.validate(new Alternative("a1", TestUtil.expansion("a", "x"), null, false));
		validator.validate(new Alternative("a1", TestUtil.expansion("a", "x"), new AlternativeConflictResolver("d", null), false));
		validator.validate(new Alternative("a1", TestUtil.expansion("y"), null, false));
		validator.validate(new Alternative("a1", TestUtil.expansion("y"), new AlternativeConflictResolver("d", null), false));
		validator.validate(new Alternative("a1", TestUtil.expansion("y"), new AlternativeConflictResolver(null, ImmutableMap.of("d", ConflictResolution.SHIFT)), false));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownSymbolInExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion("a", "u"), null, false));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownPrecedenceTerminalWithEmptyExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion(), new AlternativeConflictResolver("u", null), false));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownPrecedenceTerminalWithNonemptyExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion("a"), new AlternativeConflictResolver("u", null), false));
	}

	@Test(expected = IllegalStateException.class)
	public void testNonterminalUsedForPrecedence() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion("a"), new AlternativeConflictResolver("x", null), false));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownResolveMapKeyWithEmptyExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion(), new AlternativeConflictResolver(null, ImmutableMap.of("u", ConflictResolution.SHIFT)), false));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownResolveMapKeyWithNonemptyExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion("a"), new AlternativeConflictResolver(null, ImmutableMap.of("u", ConflictResolution.SHIFT)), false));
	}

	@Test(expected = IllegalStateException.class)
	public void testNonterminalUsedForResolveMapKey() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative("a1", TestUtil.expansion("a"), new AlternativeConflictResolver(null, ImmutableMap.of("x", ConflictResolution.SHIFT)), false));
	}

}
