package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

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
		this.terminalDeclarations = ParameterUtil.ensureNotNullOrEmpty(terminalDeclarations, "terminalDeclarations");
		ParameterUtil.ensureNoNullElement(terminalDeclarations, "terminalDeclarations");
		this.nonterminalDeclarations = ParameterUtil.ensureNotNullOrEmpty(nonterminalDeclarations, "nonterminalDeclarations");
		ParameterUtil.ensureNoNullElement(nonterminalDeclarations, "nonterminalDeclarations");
		this.precedenceTable = (precedenceTable == null ? new PrecedenceTable(ImmutableList.of()) : precedenceTable);
		this.startNonterminalName = ParameterUtil.ensureNotNullOrEmpty(startNonterminalName, "startNonterminalName");
		this.productions = ParameterUtil.ensureNotNullOrEmpty(productions, "productions");
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

}
