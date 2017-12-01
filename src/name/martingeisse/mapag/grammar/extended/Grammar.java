package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

public final class Grammar {

	private final ImmutableList<TerminalDeclaration> terminalDeclarations;
	private final PrecedenceTable precedenceTable;
	private final String startNonterminalName;
	private final ImmutableList<Production> productions;

	public Grammar(ImmutableList<TerminalDeclaration> terminalDeclarations, PrecedenceTable precedenceTable, String startNonterminalName, ImmutableList<Production> productions) {
		this.terminalDeclarations = ParameterUtil.ensureNotNull(terminalDeclarations, "terminalDeclarations");
		ParameterUtil.ensureNoNullElement(terminalDeclarations, "terminalDeclarations");
		this.precedenceTable = (precedenceTable == null ? new PrecedenceTable(ImmutableList.of()) : precedenceTable);
		this.startNonterminalName = ParameterUtil.ensureNotNullOrEmpty(startNonterminalName, "startNonterminalName");
		this.productions = ParameterUtil.ensureNotNull(productions, "productions");
		ParameterUtil.ensureNoNullElement(productions, "productions");
	}

	public ImmutableList<TerminalDeclaration> getTerminalDeclarations() {
		return terminalDeclarations;
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

}
