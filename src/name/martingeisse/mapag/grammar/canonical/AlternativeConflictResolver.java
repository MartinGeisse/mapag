package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;

/**
 *
 */
public final class AlternativeConflictResolver {

	private final String effectivePrecedenceTerminal;
	private final ImmutableMap<String, ConflictResolution> terminalToResolution;

	public AlternativeConflictResolver(String effectivePrecedenceTerminal, ImmutableMap<String, ConflictResolution> terminalToResolution) {
		if (effectivePrecedenceTerminal != null && effectivePrecedenceTerminal.isEmpty()) {
			throw new IllegalArgumentException("precedence-defining terminal cannot be the empty string");
		}
		if (effectivePrecedenceTerminal != null && terminalToResolution != null) {
			throw new IllegalArgumentException("cannot use both a precedence-defining terminal and a conflict resolution map for the same alternative");
		}
		this.effectivePrecedenceTerminal = effectivePrecedenceTerminal;
		this.terminalToResolution = terminalToResolution;
	}

	public String getEffectivePrecedenceTerminal() {
		return effectivePrecedenceTerminal;
	}

	public ImmutableMap<String, ConflictResolution> getTerminalToResolution() {
		return terminalToResolution;
	}

}
