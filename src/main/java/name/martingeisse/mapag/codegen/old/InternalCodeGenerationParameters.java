/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

/**
 * This class acts as a parameter object for all code generation classes.
 */
public final class InternalCodeGenerationParameters {

	private final GrammarInfo grammarInfo;
	private final StateMachine stateMachine;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private final boolean intellij;
	private final String notNullAnnotation;

	public InternalCodeGenerationParameters(CodeGenerationParameters parameters, boolean intellij, String notNullAnnotation) {
		this(parameters.getGrammarInfo(), parameters.getStateMachine(), parameters.getConfiguration(),
			parameters.getOutputFileFactory(), intellij, notNullAnnotation);
	}

	public InternalCodeGenerationParameters(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory, boolean intellij, String notNullAnnotation) {
		this.grammarInfo = grammarInfo;
		this.stateMachine = stateMachine;
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
		this.intellij = intellij;
		this.notNullAnnotation = notNullAnnotation;
	}

	public GrammarInfo getGrammarInfo() {
		return grammarInfo;
	}

	public StateMachine getStateMachine() {
		return stateMachine;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public OutputFileFactory getOutputFileFactory() {
		return outputFileFactory;
	}

	public boolean isIntellij() {
		return intellij;
	}

	public String getNotNullAnnotation() {
		return notNullAnnotation;
	}

}
