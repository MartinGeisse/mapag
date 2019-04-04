package name.martingeisse.mapag.ide;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagSpecificationLanguage extends Language {

	public static final MapagSpecificationLanguage INSTANCE = new MapagSpecificationLanguage();

	public MapagSpecificationLanguage() {
		super("MAPAG", "text/x-mapag");
	}

	@NotNull
	@Override
	public String getDisplayName() {
		return "MaPaG";
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}

}
