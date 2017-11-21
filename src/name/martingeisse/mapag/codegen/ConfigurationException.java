package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.util.UserMessageException;

/**
 * This exception type indicates a problem with the code generation configuration.
 */
public class ConfigurationException extends UserMessageException {

	public ConfigurationException(String message) {
		super(message);
	}

}
