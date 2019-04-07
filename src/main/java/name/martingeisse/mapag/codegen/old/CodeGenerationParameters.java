/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

/**
 * This class acts as a parameter object for all code generation classes.
 */
public final class CodeGenerationParameters {

	private final GrammarInfo grammarInfo;
	private final StateMachine stateMachine;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private final CodeGenerationContext context;

	public CodeGenerationParameters(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory, CodeGenerationContext context) {
		this.grammarInfo = grammarInfo;
		this.stateMachine = stateMachine;
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
		this.context = context;
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

	public CodeGenerationContext getContext() {
		return context;
	}

}
