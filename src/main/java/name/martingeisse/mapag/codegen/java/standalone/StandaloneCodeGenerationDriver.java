/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.java.standalone;

import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.old.InternalCodeGenerationParameters;
import name.martingeisse.mapag.codegen.old.OldCodeGenerationDriver;

import java.io.IOException;

/**
 *
 */
public class StandaloneCodeGenerationDriver implements CodeGenerationDriver {

	@Override
	public void generate(CodeGenerationParameters parameters) throws ConfigurationException, IOException {
		OldCodeGenerationDriver.generate(new InternalCodeGenerationParameters(parameters, false, ""));
	}

}
