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
import java.util.function.Consumer;

/**
 *
 */
public final class HelperClassGenerator {

	// prevent instantiation
	private HelperClassGenerator() {
	}

	public static void generate(String templateSubfolder, String subpackage, String className, CodeGenerationParameters parameters) throws IOException {
		generate(templateSubfolder, subpackage, className, parameters, null);
	}

	public static void generate(String templateSubfolder, String subpackage, String className,
								CodeGenerationParameters parameters,
								Consumer<VelocityContext> velocityContextConfigurator) throws IOException {

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
		String basePackageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE);
		String packageName;
		if (subpackage == null) {
			packageName = basePackageName;
		} else {
			packageName = basePackageName + '.' + subpackage;
		}

		// build template variables
		VelocityContext context = new VelocityContext();
		context.put("basePackageName", basePackageName);
		context.put("packageName", packageName);
		if (velocityContextConfigurator != null) {
			velocityContextConfigurator.accept(context);
		}

		// generate output
		try (OutputStream outputStream = outputFileFactory.createSourceFile(packageName, className)) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate(templatePath).merge(context, outputStreamWriter);
			}
		}

	}

}
