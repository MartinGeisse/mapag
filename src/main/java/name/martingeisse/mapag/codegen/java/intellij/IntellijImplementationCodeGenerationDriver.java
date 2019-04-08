/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.java.HelperClassGenerator;

import java.io.IOException;

/**
 *
 */
public class IntellijImplementationCodeGenerationDriver implements CodeGenerationDriver {

	public static final String PARSER_DEFINITION_CLASS_PROPERTY = "intellij.parserDefinitionClass";

	@Override
	public void generate(CodeGenerationParameters parameters) throws ConfigurationException, IOException {
		Configuration configuration = parameters.getConfiguration();
		String templateSubfolder = "intellij";
		String subpackage = "cm.impl";

		// generate helper classes
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmListImpl", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmOptionalImpl", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "CmTokenImpl", parameters);
		HelperClassGenerator.generate(templateSubfolder, subpackage, "InternalPsiUtil", parameters, context -> {
			context.put("parserDefinitionClass", configuration.getRequired(PARSER_DEFINITION_CLASS_PROPERTY));
		});

		// TODO
		// throw new NotImplementedException("");

	}

}
