/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen;

/**
 *
 */
public final class StandaloneCodeGenerationContext implements CodeGenerationContext {

	public boolean isIntellij() {
		return false;
	}

	public String getNotNullAnnotation() {
		return "";
	}

}
