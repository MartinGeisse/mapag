/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen;

import java.io.IOException;

/**
 *
 */
public interface CodeGenerationDriver {

	void generate(CodeGenerationParameters parameters) throws ConfigurationException, IOException;

}
