package name.martingeisse.mapag.grammar.canonical;

import java.util.List;

/**
 *
 */
public final class Alternative {

	private final List<String> expansion;

	public Alternative(List<String> expansion) {
		this.expansion = expansion;
	}

	public List<String> getExpansion() {
		return expansion;
	}

}
