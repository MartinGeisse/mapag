/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.old;

/**
 *
 */
public final class IntellijCodeGenerationContext implements CodeGenerationContext {

	public boolean isIntellij() {
		return true;
	}

	public String getNotNullAnnotation() {
		return "@NotNull";
	}

}
