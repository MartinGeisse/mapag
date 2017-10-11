package name.martingeisse.mapag.grammar.canonical;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public final class Alternative {

	private final ImmutableList<String> expansion;

	public Alternative(ImmutableList<String> expansion) {
		this.expansion = expansion;
	}

	public ImmutableList<String> getExpansion() {
		return expansion;
	}

}
