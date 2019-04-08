/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.java.HelperClassGenerator;

import java.io.IOException;

/**
 *
 */
public class IntellijImplementationCodeGenerationDriver implements CodeGenerationDriver {

	@Override
	public void generate(CodeGenerationParameters parameters) throws ConfigurationException, IOException {
		String templateSubfolder = "intellij";
		String subpackage = "cm.impl";

		// generate helper classes
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmListImpl", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmOptionalImpl", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmTokenImpl", parameters);

		// TODO
		// throw new NotImplementedException("");

	}

}
