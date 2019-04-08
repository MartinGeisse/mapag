/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.java.common;

import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.java.HelperClassGenerator;

import java.io.IOException;

/**
 *
 */
public class CommonCodeGenerationDriver implements CodeGenerationDriver {

	@Override
	public void generate(CodeGenerationParameters parameters) throws ConfigurationException, IOException {
		String templateSubfolder = "common";
		String subpackage = "cm";

		// generate helper classes
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmNode", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmList", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmOptional", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmToken", parameters);

		// generate code model
		new CmGenerator(parameters).generate();

	}

}
