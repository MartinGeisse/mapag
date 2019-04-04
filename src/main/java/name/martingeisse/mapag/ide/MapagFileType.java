package name.martingeisse.mapag.ide;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 *
 */
public class MapagFileType extends LanguageFileType {

	@NonNls
	public static final String DEFAULT_EXTENSION = "mapag";

	@NonNls
	public static final String DOT_DEFAULT_EXTENSION = "." + DEFAULT_EXTENSION;

	public static final MapagFileType INSTANCE = new MapagFileType();

	public MapagFileType() {
		super(MapagSpecificationLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "MAPAG";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "MaPaG grammar file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return DEFAULT_EXTENSION;
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return AllIcons.FileTypes.Text;
	}
}
