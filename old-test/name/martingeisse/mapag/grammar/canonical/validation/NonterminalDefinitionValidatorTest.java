package name.martingeisse.mapag.grammar.canonical.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.Associativity;
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
		"x", new NonterminalDefinition("x", ImmutableList.of(new Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null))), NonterminalAnnotation.EMPTY),
		"y", new NonterminalDefinition("y", ImmutableList.of(new Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a2", null))), NonterminalAnnotation.EMPTY)
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
		validator.validate(new Alternative(ImmutableList.of(), null, new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of(), "a", new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of(), "d", new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of("a", "b"), null, new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of("a", "b"), "d", new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of("a", "x"), null, new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of("a", "x"), "d", new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of("y"), null, new AlternativeAnnotation("a1", null)));
		validator.validate(new Alternative(ImmutableList.of("y"), "d", new AlternativeAnnotation("a1", null)));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownSymbolInExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative(ImmutableList.of("a", "u"), null, new AlternativeAnnotation("a1", null)));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownPrecedenceTerminalWithEmptyExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative(ImmutableList.of(), "u", new AlternativeAnnotation("a1", null)));
	}

	@Test(expected = IllegalStateException.class)
	public void testUnknownPrecedenceTerminalWithNonemptyExpansion() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative(ImmutableList.of("a"), "u", new AlternativeAnnotation("a1", null)));
	}

	@Test(expected = IllegalStateException.class)
	public void testNonterminalUsedForPrecedence() {
		NonterminalDefinitionValidator validator = new NonterminalDefinitionValidator(TERMINALS, SIMPLE_NONTERMINALS);
		validator.validate(new Alternative(ImmutableList.of("a"), "x", new AlternativeAnnotation("a1", null)));
	}

}
