package name.martingeisse.mapag.ide;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagFileTypeFactory extends FileTypeFactory {

	@Override
	public void createFileTypes(@NotNull final FileTypeConsumer consumer) {
		consumer.consume(MapagFileType.INSTANCE, MapagFileType.INSTANCE.getDefaultExtension());
	}

}
