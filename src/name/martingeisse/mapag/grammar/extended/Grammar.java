package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class Grammar {

	private final String packageName;
	private final String className;
	private final ImmutableList<TerminalDeclaration> terminalDeclarations;
	private final ImmutableList<NonterminalDeclaration> nonterminalDeclarations;
	private final PrecedenceTable precedenceTable;
	private final String startNonterminalName;
	private final ImmutableList<Production> productions;

	public Grammar(String packageName, String className, ImmutableList<TerminalDeclaration> terminalDeclarations, ImmutableList<NonterminalDeclaration> nonterminalDeclarations, PrecedenceTable precedenceTable, String startNonterminalName, ImmutableList<Production> productions) {
		this.packageName = ParameterUtil.ensureNotNull(packageName, "packageName");
		this.className = ParameterUtil.ensureNotNullOrEmpty(className, "className");
		this.terminalDeclarations = ParameterUtil.ensureNotNull(terminalDeclarations, "terminalDeclarations");
		ParameterUtil.ensureNoNullElement(terminalDeclarations, "terminalDeclarations");
		this.nonterminalDeclarations = ParameterUtil.ensureNotNull(nonterminalDeclarations, "nonterminalDeclarations");
		ParameterUtil.ensureNoNullElement(nonterminalDeclarations, "nonterminalDeclarations");
		this.precedenceTable = (precedenceTable == null ? new PrecedenceTable(ImmutableList.of()) : precedenceTable);
		this.startNonterminalName = ParameterUtil.ensureNotNullOrEmpty(startNonterminalName, "startNonterminalName");
		this.productions = ParameterUtil.ensureNotNull(productions, "productions");
		ParameterUtil.ensureNoNullElement(productions, "productions");
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public ImmutableList<TerminalDeclaration> getTerminalDeclarations() {
		return terminalDeclarations;
	}

	public ImmutableList<NonterminalDeclaration> getNonterminalDeclarations() {
		return nonterminalDeclarations;
	}

	public PrecedenceTable getPrecedenceTable() {
		return precedenceTable;
	}

	public String getStartNonterminalName() {
		return startNonterminalName;
	}

	public ImmutableList<Production> getProductions() {
		return productions;
	}

	public void validate() {

		Set<String> terminalNames = new HashSet<>();
		for (TerminalDeclaration terminalDeclaration : terminalDeclarations) {
			if (!terminalNames.add(terminalDeclaration.getName())) {
				throw new IllegalStateException("redeclaration of terminal: " + terminalDeclaration.getName());
			}
		}

		Set<String> nonterminalNames = new HashSet<>();
		for (NonterminalDeclaration nonterminalDeclaration : nonterminalDeclarations) {
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

		Map<String, PrecedenceTable.Entry> precedenceTableEntriesByName = new HashMap<>();
		for (PrecedenceTable.Entry entry : precedenceTable.getEntries()) {
			String name = entry.getTerminalName();
			if (!terminalNames.contains(name)) {
				throw new IllegalStateException("unknown terminal name in precedence table: " + name);
			}
			if (precedenceTableEntriesByName.put(name, entry) != null) {
				throw new IllegalStateException("terminal appears twice in precedence table: " + name);
			}
		}

		if (!nonterminalNames.contains(startNonterminalName)) {
			throw new IllegalStateException("start symbol was not declared as a nonterminal: " + startNonterminalName);
		}

		boolean foundProductionForStartSymbol = false;
		for (Production production : productions) {
			String leftHandSide = production.getLeftHandSide();
			if (!nonterminalNames.contains(leftHandSide)) {
				throw new IllegalStateException("left-hand symbol in production was not declared as a nonterminal: " + leftHandSide);
			}
			if (leftHandSide.equals(startNonterminalName)) {
				foundProductionForStartSymbol = true;
			}
			// TODO check expressions, but first move this code to a separate validator so we can pass state easier
		}
		if (!foundProductionForStartSymbol) {
			throw new IllegalStateException("no production found for start symbol");
		}

	}

}
