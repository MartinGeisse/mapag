package name.martingeisse.mapag.input;

import com.intellij.psi.tree.IElementType;
import name.martingeisse.mapag.ide.MapagSpecificationLanguage;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagElementType extends IElementType {

	public MapagElementType(@NotNull String debugName) {
		super(debugName, MapagSpecificationLanguage.INSTANCE);
	}

}
