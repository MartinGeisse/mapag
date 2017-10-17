package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class GrammarValidator {

	private final Grammar grammar;
	private Set<String> terminalNames;
	private Set<String> nonterminalNames;
	private Map<String, PrecedenceTable.Entry> precedenceTableEntriesByName;

	public GrammarValidator(Grammar grammar) {
		this.grammar = grammar;
	}

	public void validate() {

		terminalNames = new HashSet<>();
		for (TerminalDeclaration terminalDeclaration : grammar.getTerminalDeclarations()) {
			if (!terminalNames.add(terminalDeclaration.getName())) {
				throw new IllegalStateException("redeclaration of terminal: " + terminalDeclaration.getName());
			}
		}

		nonterminalNames = new HashSet<>();
		for (NonterminalDeclaration nonterminalDeclaration : grammar.getNonterminalDeclarations()) {
			if (!nonterminalNames.add(nonterminalDeclaration.getName())) {
				throw new IllegalStateException("redeclaration of nonterminal: " + nonterminalDeclaration.getName());
			}
		}

		{
			Set<String> nameIntersection = new HashSet<>(terminalNames);
			nameIntersection.retainAll(nonterminalNames);
			if (!nameIntersection.isEmpty()) {
				throw new IllegalStateException("redeclaration of terminals as nonterminals: " + nameIntersection);
			}
		}

		precedenceTableEntriesByName = new HashMap<>();
		for (PrecedenceTable.Entry entry : grammar.getPrecedenceTable().getEntries()) {
			for (String name : entry.getTerminalNames()) {
				if (!terminalNames.contains(name)) {
					throw new IllegalStateException("unknown terminal name in precedence table: " + name);
				}
				if (precedenceTableEntriesByName.put(name, entry) != null) {
					throw new IllegalStateException("terminal appears twice in precedence table: " + name);
				}
			}
		}

		if (!nonterminalNames.contains(grammar.getStartNonterminalName())) {
			throw new IllegalStateException("start symbol was not declared as a nonterminal: " + grammar.getStartNonterminalName());
		}

		boolean foundProductionForStartSymbol = false;
		for (Production production : grammar.getProductions()) {
			String leftHandSide = production.getLeftHandSide();
			if (!nonterminalNames.contains(leftHandSide)) {
				throw new IllegalStateException("left-hand symbol in production was not declared as a nonterminal: " + leftHandSide);
			}
			if (leftHandSide.equals(grammar.getStartNonterminalName())) {
				foundProductionForStartSymbol = true;
			}
			// TODO validateExpression(production.getRightHandSide());
		}
		if (!foundProductionForStartSymbol) {
			throw new IllegalStateException("no production found for start symbol");
		}

	}

}
