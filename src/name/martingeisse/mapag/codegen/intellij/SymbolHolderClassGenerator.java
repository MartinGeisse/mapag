package name.martingeisse.mapag.codegen.intellij;

import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
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

	public static final String PACKAGE_NAME_PROPERTY = "symbolHolder.package";
	public static final String CLASS_NAME_PROPERTY = "symbolHolder.class";
	public static final String ELEMENT_TYPE_CLASS = "symbol.elementType.class";
	public static final String TERMINAL_ELEMENT_TYPE_CLASS = "symbol.elementType.terminal.class";
	public static final String NONTERMINAL_ELEMENT_TYPE_CLASS = "symbol.elementType.nonterminal.class";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;

	public SymbolHolderClassGenerator(GrammarInfo grammarInfo, Configuration configuration, OutputFileFactory outputFileFactory) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
	}

	public void generate() throws ConfigurationException, IOException {

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
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("className", configuration.getRequired(CLASS_NAME_PROPERTY));
		context.put("terminals", ListUtil.sorted(grammar.getTerminalDefinitions().keySet(), null));
		context.put("terminalElementTypeClass", configuration.getExactlyOne(TERMINAL_ELEMENT_TYPE_CLASS, ELEMENT_TYPE_CLASS));
		context.put("nonterminalAlternatives", nonterminalAlternatives);
		context.put("nonterminalElementTypeClass", configuration.getExactlyOne(NONTERMINAL_ELEMENT_TYPE_CLASS, ELEMENT_TYPE_CLASS));

		try (OutputStream outputStream = outputFileFactory.createSourceFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), configuration.getRequired(CLASS_NAME_PROPERTY))) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("templates/SymbolHolder.vm").merge(context, outputStreamWriter);
			}
		}

	}

}
