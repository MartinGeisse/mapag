/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.java.common.CommonCodeGenerationDriver;

import java.io.IOException;

/**
 *
 */
public class IntellijCodeGenerationDriver implements CodeGenerationDriver {

	@Override
	public void generate(CodeGenerationParameters parameters) throws ConfigurationException, IOException {
		new CommonCodeGenerationDriver().generate(parameters);
		new IntellijImplementationCodeGenerationDriver().generate(parameters);
	}

}
