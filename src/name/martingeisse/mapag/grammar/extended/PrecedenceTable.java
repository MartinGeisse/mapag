package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.SpecialSymbols;
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

		private final ImmutableSet<String> terminalNames;
		private final Associativity associativity;

		public Entry(ImmutableSet<String> terminalNames, Associativity associativity) {
			ParameterUtil.ensureNotNullOrEmpty(terminalNames, "terminalNames");
			ParameterUtil.ensureNoNullOrEmptyElement(terminalNames, "terminalNames");
			ParameterUtil.ensureNotNull(associativity, "associativity");
			for (String terminalName : terminalNames) {
				if (terminalName.startsWith("%")) { // TODO test this
					throw new IllegalArgumentException("cannot define precedence for special symbol " + terminalName);
				}
			}
			this.terminalNames = terminalNames;
			this.associativity = associativity;
		}

		public ImmutableSet<String> getTerminalNames() {
			return terminalNames;
		}

		public Associativity getAssociativity() {
			return associativity;
		}

	}

}
