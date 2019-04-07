/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.CodeGenerationContext;
import name.martingeisse.mapag.codegen.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.CodeGenerationParameters;

/**
 *
 */
public final class StandaloneCodeGenerationContext implements CodeGenerationContext {

	@Override
	public String getName() {
		return "standalone";
	}

	public boolean isIntellij() {
		return false;
	}

	public String getNotNullAnnotation() {
		return "";
	}

	@Override
	public CodeGenerationDriver createDriver(CodeGenerationParameters parameters) {
		return new OldCodeGenerationDriver(parameters);
	}

}
