package name.martingeisse.mapag.grammar;

/**
 *
 */
public enum ConflictResolution {

	SHIFT {
		@Override
		public String getKeyword() {
			return "%shift";
		}
	},

	REDUCE {
		@Override
		public String getKeyword() {
			return "%reduce";
		}
	};

	public abstract String getKeyword();

}
