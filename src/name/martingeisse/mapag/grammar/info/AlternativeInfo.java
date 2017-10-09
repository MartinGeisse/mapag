package name.martingeisse.mapag.grammar.info;

/**
 *
 */
public final class AlternativeInfo {

	private final List<String> expansion;

	public AlternativeInfo(List<String> expansion) {
		this.expansion = expansion;
	}

	public AlternativeBuilder(String... expansionSymbols) {
		this(Arrays.AsList(expansionSymbols));
	}

	public List<String> getExpansion() {
		return expansion;
	}
}
