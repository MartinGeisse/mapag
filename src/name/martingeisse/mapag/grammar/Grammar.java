package name.martingeisse.mapag.grammar;

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
	private final List<TerminalDefinition> terminalDefinitions;
	private final List<NonterminalDefinition> nonterminalDefinitions;
	private final PrecedenceTable precedenceTable;
	private final String startNonterminalName;
	private final List<Production> productions;

	public Grammar(String packageName, String className, List<TerminalDefinition> terminalDefinitions, List<NonterminalDefinition> nonterminalDefinitions, PrecedenceTable precedenceTable, String startNonterminalName, List<Production> productions) {
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

	public List<TerminalDefinition> getTerminalDefinitions() {
		return terminalDefinitions;
	}

	public List<NonterminalDefinition> getNonterminalDefinitions() {
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
