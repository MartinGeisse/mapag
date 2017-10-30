package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.Action;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Properties;

/**
 *
 */
public class MapagParserClassGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "parser.package";
	public static final String CLASS_NAME_PROPERTY = "parser.class";

	private static final VelocityEngine engine;
	static {
		engine = new VelocityEngine();
		engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
		engine.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		engine.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		engine.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.loader.description", "classpath-based resource loader");
		engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		engine.setProperty("classpath.resource.loader.cache", true);
		engine.init();
	}

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final StateMachine stateMachine;
	private final Properties codeGenerationProperties;

	public MapagParserClassGenerator(GrammarInfo grammarInfo, StateMachine stateMachine, Properties codeGenerationProperties) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.stateMachine = stateMachine;
		this.codeGenerationProperties = codeGenerationProperties;
	}

	public void generate() {

		StateMachineEncoder stateMachineEncoder = new StateMachineEncoder(grammarInfo, stateMachine);
		int numberOfStates = stateMachine.getStates().size();
		int numberOfTerminals = grammar.getTerminalDefinitions().size();
		int numberOfNonterminals = grammar.getNonterminalDefinitions().size();

		VelocityContext context = new VelocityContext();
		context.put("packageName", codeGenerationProperties.get(PACKAGE_NAME_PROPERTY));
		context.put("className", codeGenerationProperties.get(CLASS_NAME_PROPERTY));
		{
			String[] terminalNames = new String[numberOfTerminals];
			for (String terminal : grammar.getTerminalDefinitions().keySet()) {
				int index = stateMachineEncoder.getTerminalIndex(terminal);
				terminalNames[index] = terminal;
			}
			context.put("tokensInTokenCodeOrder", terminalNames);
		}
		context.put("startSymbolName", grammar.getStartNonterminalName());
		context.put("startStateCode", stateMachineEncoder.getStateIndex(stateMachine.getStartState()));
		{
			int actionTableWidth = 1 + numberOfTerminals + numberOfNonterminals;
			int[][] actionTable = new int[numberOfStates][actionTableWidth];
			for (State state : stateMachine.getStates()) {
				int stateIndex = stateMachineEncoder.getStateIndex(state);
				{
					Action action = stateMachine.getTerminalOrEofActions().get(state).get(SpecialSymbols.EOF_SYMBOL_NAME);
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][0] = actionCode;
				}
				for (String terminal : grammar.getTerminalDefinitions().keySet()) {
					int symbolIndex = stateMachineEncoder.getSymbolIndex(terminal);
					Action action = stateMachine.getTerminalOrEofActions().get(state).get(terminal);
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][symbolIndex] = actionCode;
				}
				for (String nonterminal : grammar.getNonterminalDefinitions().keySet()) {
					int symbolIndex = stateMachineEncoder.getSymbolIndex(nonterminal);
					Action.Shift action = stateMachine.getNonterminalActions().get(state).get(nonterminal);
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][symbolIndex] = actionCode;
				}
			}
			context.put("actionTableRows", actionTable);
			context.put("actionTableWidth", actionTableWidth);
		}

		StringWriter sw = new StringWriter();
		engine.getTemplate("Parser.vm").merge(context, sw);
		System.out.println(sw);

		stateMachineEncoder.dump();

	}

}
