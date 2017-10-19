package name.martingeisse.mapag.grammar;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.validation.GrammarValidator;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class GrammarCanonicalizer {

	private final name.martingeisse.mapag.grammar.extended.Grammar inputGrammar;
	private Map<String, TerminalDefinition> terminalDefinitions;
	private Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternatives;
	private Map<String, NonterminalDefinition> nonterminalDefinitions;
	private name.martingeisse.mapag.grammar.canonical.Grammar outputGrammar;

	public GrammarCanonicalizer(name.martingeisse.mapag.grammar.extended.Grammar inputGrammar) {
		this.inputGrammar = ParameterUtil.ensureNotNull(inputGrammar, "inputGrammar");
	}

	public void run() {
		if (outputGrammar != null) {
			throw new IllegalStateException("this canonicalizer has already run");
		}

		// make sure the input grammar is valid (don't trust the caller to do the validation)
		new GrammarValidator(inputGrammar).validate();

		// define terminals using defaults
		this.terminalDefinitions = new HashMap<>();
		for (TerminalDeclaration terminalDeclaration : inputGrammar.getTerminalDeclarations()) {
			TerminalDefinition terminalDefinition = new TerminalDefinition(
					terminalDeclaration.getName(), null, Associativity.NONASSOC);
			terminalDefinitions.put(terminalDeclaration.getName(), terminalDefinition);
		}

		// change definition based on the precedence table
		int precedenceIndex = 0;
		for (PrecedenceTable.Entry entry : inputGrammar.getPrecedenceTable().getEntries()) {
			for (String name : entry.getTerminalNames()) {
				TerminalDefinition terminalDefinition = new TerminalDefinition(name, precedenceIndex, entry.getAssociativity());
				terminalDefinitions.put(name, terminalDefinition);
			}
			precedenceIndex++;
		}

		// build nonterminal alternatives
		this.nonterminalAlternatives = new HashMap<>();
		for (Production production : inputGrammar.getProductions()) {
			String leftHandSide = production.getLeftHandSide();
			for (name.martingeisse.mapag.grammar.extended.Alternative inputAlternative : production.getAlternatives()) {
				name.martingeisse.mapag.grammar.canonical.Alternative convertedAlternative = convertAlternative(leftHandSide, inputAlternative);
				if (nonterminalAlternatives.get(leftHandSide) == null) {
					nonterminalAlternatives.put(leftHandSide, new ArrayList<>());
				}
				nonterminalAlternatives.get(leftHandSide).add(convertedAlternative);
			}
		}

		// build nonterminal definitions
		nonterminalDefinitions = new HashMap<>();
		for (Map.Entry<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternativesEntry : nonterminalAlternatives.entrySet()) {
			String nonterminalName = nonterminalAlternativesEntry.getKey();
			ImmutableList<name.martingeisse.mapag.grammar.canonical.Alternative> alternatives = ImmutableList.copyOf(nonterminalAlternativesEntry.getValue());
			NonterminalDefinition nonterminalDefinition = new NonterminalDefinition(nonterminalName, alternatives);
			nonterminalDefinitions.put(nonterminalName, nonterminalDefinition);
		}

		// build the output grammar
		this.outputGrammar = new name.martingeisse.mapag.grammar.canonical.Grammar(
				inputGrammar.getPackageName(),
				inputGrammar.getClassName(),
				terminalDefinitions.values(),
				nonterminalDefinitions.values(),
				inputGrammar.getStartNonterminalName()
		);

	}

	private name.martingeisse.mapag.grammar.canonical.Alternative convertAlternative(String nonterminalName, name.martingeisse.mapag.grammar.extended.Alternative inputAlternative) {
		// TODO check
//		List<Alternative> alternatives = nonterminalAlternatives.get(nonterminalName);
//		if (alternatives == null) {
//			alternatives = new ArrayList<>();
//			nonterminalAlternatives.put(nonterminalName, alternatives);
//		}
//		alternatives.add(alternative);
		throw new RuntimeException();
	}

	private String determineEffectivePrecedenceTerminal(Alternative alternative) {
		switch (alternative.getPrecedenceSpecificationType()) {

			case UNDEFINED:
				return null;

			case EXPLICIT:
				return alternative.getPrecedenceSpecification();

			case IMPLICIT:
				return determineImplicitPrecedenceTerminal(alternative.getExpression());

			default:
				throw new RuntimeException("unknown precedence specification type: " + alternative.getPrecedenceSpecificationType());

		}

	}

	public name.martingeisse.mapag.grammar.canonical.Grammar getResult() {
		return outputGrammar;
	}

}
