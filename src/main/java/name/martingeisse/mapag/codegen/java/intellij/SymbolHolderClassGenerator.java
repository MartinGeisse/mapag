package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.codegen.java.JavaPropertyNames;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.util.ListUtil;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SymbolHolderClassGenerator {

	public static final String CLASS_NAME_PROPERTY = "symbol.holder.class";
	public static final String ELEMENT_TYPE_CLASS = "symbol.elementType.class";
	public static final String TERMINAL_ELEMENT_TYPE_CLASS = "symbol.elementType.terminal.class";
	public static final String NONTERMINAL_ELEMENT_TYPE_CLASS = "symbol.elementType.nonterminal.class";

	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;

	public SymbolHolderClassGenerator(CodeGenerationParameters parameters) {
		this.grammar = parameters.getGrammarInfo().getGrammar();
		this.configuration = parameters.getConfiguration();
		this.outputFileFactory = parameters.getOutputFileFactory();
	}

	public void generate() throws ConfigurationException, IOException {
		String packageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE);

		List<String> nonterminalAlternatives = new ArrayList<>();
		for (NonterminalDefinition nonterminal : grammar.getNonterminalDefinitions().values()) {
			if (nonterminal.getPsiStyle().isDistinctSymbolPerAlternative()) {
				for (Alternative alternative : nonterminal.getAlternatives()) {
					nonterminalAlternatives.add(IdentifierUtil.getAlternativeVariableIdentifier(nonterminal, alternative));
				}
			} else {
				nonterminalAlternatives.add(IdentifierUtil.getNonterminalVariableIdentifier(nonterminal));
			}
		}
		Collections.sort(nonterminalAlternatives);

		VelocityContext context = new VelocityContext();
		context.put("packageName", packageName);
		context.put("className", configuration.getRequired(CLASS_NAME_PROPERTY));
		context.put("terminals", ListUtil.sorted(grammar.getTerminalDefinitions().keySet(), null));
		context.put("terminalElementTypeClass", configuration.getExactlyOne(TERMINAL_ELEMENT_TYPE_CLASS, ELEMENT_TYPE_CLASS));
		context.put("nonterminalAlternatives", nonterminalAlternatives);
		context.put("nonterminalElementTypeClass", configuration.getExactlyOne(NONTERMINAL_ELEMENT_TYPE_CLASS, ELEMENT_TYPE_CLASS));

		try (OutputStream outputStream = outputFileFactory.createSourceFile(packageName, configuration.getRequired(CLASS_NAME_PROPERTY))) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("templates/intellij/SymbolHolder.vm").merge(context, outputStreamWriter);
			}
		}

	}

}
