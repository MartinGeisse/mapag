package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.NonterminalAnnotation;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import name.martingeisse.mapag.grammar.extended.validation.GrammarValidator;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class GrammarCanonicalizer {

	private final name.martingeisse.mapag.grammar.extended.Grammar inputGrammar;
	private Map<String, TerminalDefinition> terminalDefinitions;
	private Map<String, NonterminalDefinition> nonterminalDefinitions;
	private name.martingeisse.mapag.grammar.canonical.Grammar outputGrammar;

	public GrammarCanonicalizer(name.martingeisse.mapag.grammar.extended.Grammar inputGrammar) {
		this.inputGrammar = ParameterUtil.ensureNotNull(inputGrammar, "inputGrammar");
	}

	public GrammarCanonicalizer run() {
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

		// build nonterminal definitions
		ProductionCanonicalizer productionCanonicalizer = new ProductionCanonicalizer(inputGrammar.getProductions());
		productionCanonicalizer.run();
		nonterminalDefinitions = new HashMap<>();
		for (Map.Entry<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternativesEntry : productionCanonicalizer.getNonterminalAlternatives().entrySet()) {
			String nonterminalName = nonterminalAlternativesEntry.getKey();
			ImmutableList<name.martingeisse.mapag.grammar.canonical.Alternative> alternatives = ImmutableList.copyOf(nonterminalAlternativesEntry.getValue());
			NonterminalAnnotation annotation = productionCanonicalizer.getNonterminalAnnotations().get(nonterminalName);
			if (annotation == null) {
				annotation = NonterminalAnnotation.EMPTY;
			}
			NonterminalDefinition nonterminalDefinition = new NonterminalDefinition(nonterminalName, alternatives, annotation);
			nonterminalDefinitions.put(nonterminalName, nonterminalDefinition);
		}

		// build the output grammar
		this.outputGrammar = new name.martingeisse.mapag.grammar.canonical.Grammar(
			terminalDefinitions.values(),
			nonterminalDefinitions.values(),
			inputGrammar.getStartNonterminalName()
		);

		return this;
	}

	public name.martingeisse.mapag.grammar.canonical.Grammar getResult() {
		return outputGrammar;
	}

}
