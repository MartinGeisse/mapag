package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class PrecedenceTable {

	private final ImmutableList<Entry> entries;

	public PrecedenceTable(ImmutableList<Entry> entries) {
		ParameterUtil.ensureNotNull(entries, "entries");
		ParameterUtil.ensureNoNullElement(entries, "entries");
		this.entries = entries;
	}

	public ImmutableList<Entry> getEntries() {
		return entries;
	}

	public static final class Entry {

		private final String terminalName;
		private final Associativity associativity;

		public Entry(String terminalName, Associativity associativity) {
			ParameterUtil.ensureNotNullOrEmpty(terminalName, "terminalName");
			ParameterUtil.ensureNotNull(associativity, "associativity");
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
