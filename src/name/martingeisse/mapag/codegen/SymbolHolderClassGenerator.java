package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.Action;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.*;

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

	public SymbolHolderClassGenerator(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
	}

	public void generate() throws ConfigurationException {

		List<String> terminals = new ArrayList<>(grammar.getTerminalDefinitions().keySet());
		Collections.sort(terminals);
		List<String> nonterminals = new ArrayList<>(grammar.getNonterminalDefinitions().keySet());
		Collections.sort(nonterminals);

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("className", configuration.getRequired(CLASS_NAME_PROPERTY));
		context.put("terminals", terminals);
		context.put("terminalElementTypeClass", configuration.getExactlyOne(TERMINAL_ELEMENT_TYPE_CLASS, ELEMENT_TYPE_CLASS));
		context.put("nonterminals", nonterminals);
		context.put("nonterminalElementTypeClass", configuration.getExactlyOne(NONTERMINAL_ELEMENT_TYPE_CLASS, ELEMENT_TYPE_CLASS));

		StringWriter sw = new StringWriter();
		MapagVelocityEngine.engine.getTemplate("SymbolHolder.vm").merge(context, sw);
		System.out.println(sw);

	}

}
