package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.util.ParameterUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

/**
 *
 */
public final class AlternativeAttributes {

	public static final AlternativeAttributes EMPTY = new AlternativeAttributes(null, null, false, false);

	private final String effectivePrecedenceTerminal;
	private final ImmutableMap<String, ConflictResolution> terminalToResolution;
	private final boolean reduceOnError;
	private final boolean reduceOnEofOnly;

	public AlternativeAttributes(String effectivePrecedenceTerminal, ImmutableMap<String, ConflictResolution> terminalToResolution, boolean reduceOnError, boolean reduceOnEofOnly) {
		ParameterUtil.ensureNotEmpty(effectivePrecedenceTerminal, "effectivePrecedenceTerminal");
		if (effectivePrecedenceTerminal != null && terminalToResolution != null) {
			throw new IllegalArgumentException("cannot use both a precedence-defining terminal and a conflict resolution map for the same alternative");
		}
		this.effectivePrecedenceTerminal = effectivePrecedenceTerminal;
		this.terminalToResolution = terminalToResolution;
		this.reduceOnError = reduceOnError;
		this.reduceOnEofOnly = reduceOnEofOnly;
	}

	public String getEffectivePrecedenceTerminal() {
		return effectivePrecedenceTerminal;
	}

	public ImmutableMap<String, ConflictResolution> getTerminalToResolution() {
		return terminalToResolution;
	}

	public boolean isReduceOnError() {
		return reduceOnError;
	}

	public boolean isReduceOnEofOnly() {
		return reduceOnEofOnly;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AlternativeAttributes) {
			AlternativeAttributes other = (AlternativeAttributes)obj;
			return new EqualsBuilder()
				.append(effectivePrecedenceTerminal, other.getEffectivePrecedenceTerminal())
				.append(terminalToResolution, other.getTerminalToResolution())
				.append(reduceOnError, other.isReduceOnError())
				.append(reduceOnEofOnly, other.isReduceOnEofOnly())
				.build();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(effectivePrecedenceTerminal).append(terminalToResolution).append(reduceOnError).append(reduceOnEofOnly).toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (effectivePrecedenceTerminal != null) {
			builder.append(" %precedence ").append(effectivePrecedenceTerminal);
		}
		if (terminalToResolution != null) {
			builder.append(" %resolve { ");
			for (Map.Entry<String, ConflictResolution> entry : terminalToResolution.entrySet()) {
				if (entry.getValue() == ConflictResolution.SHIFT) {
					builder.append("%shift ");
				} else if (entry.getValue() == ConflictResolution.REDUCE) {
					builder.append("%reduce ");
				} else {
					builder.append("??? ");
				}
				builder.append(entry.getKey()).append("; ");
			}
			builder.append('}');
		}
		if (reduceOnError) {
			builder.append(" %reduceOnError");
		}
		if (reduceOnEofOnly) {
			builder.append(" %eof");
		}
		return builder.toString();
	}

}
