package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.util.ParameterUtil;

public final class ResolveDeclaration {

	private final ConflictResolution conflictResolution;
	private final ImmutableList<String> terminals;

	public ResolveDeclaration(ConflictResolution conflictResolution, ImmutableList<String> terminals) {
		this.conflictResolution = ParameterUtil.ensureNotNull(conflictResolution, "conflictResolution");
		this.terminals = ParameterUtil.ensureNotNull(terminals, "terminals");
		ParameterUtil.ensureNoNullOrEmptyElement(terminals, "terminals");
	}

	public ConflictResolution getConflictResolution() {
		return conflictResolution;
	}

	public ImmutableList<String> getTerminals() {
		return terminals;
	}

	@Override
	public String toString() {
		return conflictResolution.getKeyword() + ' ' + terminals;
	}

}
