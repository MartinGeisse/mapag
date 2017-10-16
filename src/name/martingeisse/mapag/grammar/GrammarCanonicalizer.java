package name.martingeisse.mapag.grammar;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import name.martingeisse.mapag.grammar.extended.validation.GrammarValidator;

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
	private Map<String, List<Alternative>> nonterminalAlternatives;
	private Map<String, NonterminalDefinition> nonterminalDefinitions;
	private name.martingeisse.mapag.grammar.canonical.Grammar outputGrammar;

	public GrammarCanonicalizer(name.martingeisse.mapag.grammar.extended.Grammar inputGrammar) {
		this.inputGrammar = inputGrammar;
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
			TerminalDefinition terminalDefinition = new TerminalDefinition(entry.getTerminalName(), precedenceIndex, entry.getAssociativity());
			terminalDefinitions.put(entry.getTerminalName(), terminalDefinition);
			precedenceIndex++;
		}

		// build nonterminal alternatives
		// TODO check
		this.nonterminalAlternatives = new HashMap<>();
		for (Production production : inputGrammar.getProductions()) {
			addToplevelAlternatives(production);
		}

		// build nonterminal definitions TODO check
		nonterminalDefinitions = new HashMap<>();
		for (Map.Entry<String, List<Alternative>> nonterminalAlternativesEntry : nonterminalAlternatives.entrySet()) {
			String nonterminalName = nonterminalAlternativesEntry.getKey();
			NonterminalDefinition nonterminalDefinition = new NonterminalDefinition(nonterminalName, ImmutableList.copyOf(nonterminalAlternativesEntry.getValue()));
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

	private void addToplevelAlternatives(Production production) {
		// TODO
	}

	private void addAlternative(String nonterminalName, Alternative alternative) {
		// TODO check
		List<Alternative> alternatives = nonterminalAlternatives.get(nonterminalName);
		if (alternatives == null) {
			alternatives = new ArrayList<>();
			nonterminalAlternatives.put(nonterminalName, alternatives);
		}
		alternatives.add(alternative);
	}

	public name.martingeisse.mapag.grammar.canonical.Grammar getResult() {
		return outputGrammar;
	}

}
