package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class AlternativeConflictResolver {

	private final String effectivePrecedenceTerminal;
	private final ImmutableMap<String, ConflictResolution> terminalToResolution;

	public AlternativeConflictResolver(String effectivePrecedenceTerminal, ImmutableMap<String, ConflictResolution> terminalToResolution) {
		ParameterUtil.ensureNotEmpty(effectivePrecedenceTerminal, "effectivePrecedenceTerminal");
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
