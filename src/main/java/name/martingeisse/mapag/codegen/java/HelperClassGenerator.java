/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.java;

import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.MapagVelocityEngine;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public final class HelperClassGenerator {

	// prevent instantiation
	private HelperClassGenerator() {
	}

	public static void generate(String templateSubfolder, String subpackage, String className, CodeGenerationParameters parameters) throws IOException {

		// extract parameters
		Configuration configuration = parameters.getConfiguration();
		OutputFileFactory outputFileFactory = parameters.getOutputFileFactory();

		// determine full template path
		String templatePath;
		if (templateSubfolder == null) {
			templatePath = "templates/" + className + ".vm";
		} else {
			templatePath = "templates/" + templateSubfolder + '/' + className + ".vm";
		}

		// determine full package name
		String packageName;
		if (subpackage == null) {
			packageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE);
		} else {
			packageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE) + '.' + subpackage;
		}

		// build template variables
		VelocityContext context = new VelocityContext();
		context.put("packageName", packageName);

		// generate output
		try (OutputStream outputStream = outputFileFactory.createSourceFile(packageName, className)) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate(templatePath).merge(context, outputStreamWriter);
			}
		}

	}

}
