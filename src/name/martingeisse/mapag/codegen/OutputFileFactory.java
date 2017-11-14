package name.martingeisse.mapag.codegen;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public interface OutputFileFactory {

	public OutputStream createOutputFile(String packageName, String className) throws IOException;

}
