package name.martingeisse.mapag.codegen;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public interface OutputFileFactory {

	public OutputStream createSourceFile(String packageName, String className) throws IOException;

	public OutputStream createResourceFile(String filename) throws IOException;

}
