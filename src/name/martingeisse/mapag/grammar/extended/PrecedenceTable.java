package name.martingeisse.mapag.grammar.extended;

import name.martingeisse.mapag.grammar.Associativity;

import java.util.List;

/**
 *
 */
public final class PrecedenceTable {

	private final List<Entry> entries;

	public PrecedenceTable(List<Entry> entries) {
		this.entries = entries;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public static class Entry {

		private final String terminalName;
		private final Associativity associativity;

		public Entry(String terminalName, Associativity associativity) {
			this.terminalName = terminalName;
			this.associativity = associativity;
		}

		public String getTerminalName() {
			return terminalName;
		}

		public Associativity getAssociativity() {
			return associativity;
		}

	}

}
