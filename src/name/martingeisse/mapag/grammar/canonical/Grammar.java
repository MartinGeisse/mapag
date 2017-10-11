package name.martingeisse.mapag.grammar.canonical;

import name.martingeisse.mapag.grammar.extended.NonterminalDeclaration;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;

import java.util.List;

/**
 *
 */
public final class Grammar {

	public static final String IMPLICIT_START_NONTERMINAL_NAME = "%start";
	public static final String EOF_TOKEN_NAME = "%eof";
	public static final String ERROR_TOKEN_NAME = "%error";

	private final String packageName;
	private final String className;
	private final List<TerminalDeclaration> terminalDefinitions;
	private final List<NonterminalDeclaration> nonterminalDefinitions;
	private final PrecedenceTable precedenceTable;
	private final String startNonterminalName;
	private final List<Production> productions;

	public Grammar(String packageName, String className, List<TerminalDeclaration> terminalDefinitions, List<NonterminalDeclaration> nonterminalDefinitions, PrecedenceTable precedenceTable, String startNonterminalName, List<Production> productions) {
		this.packageName = packageName;
		this.className = className;
		this.terminalDefinitions = terminalDefinitions;
		this.nonterminalDefinitions = nonterminalDefinitions;
		this.precedenceTable = precedenceTable;
		this.startNonterminalName = startNonterminalName;
		this.productions = productions;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	public List<TerminalDeclaration> getTerminalDefinitions() {
		return terminalDefinitions;
	}

	public List<NonterminalDeclaration> getNonterminalDefinitions() {
		return nonterminalDefinitions;
	}

	public PrecedenceTable getPrecedenceTable() {
		return precedenceTable;
	}

	public String getStartNonterminalName() {
		return startNonterminalName;
	}

	public List<Production> getProductions() {
		return productions;
	}

}
