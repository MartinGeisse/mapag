package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.List;

/**
 *
 */
public class MapagParserClassGenerator {

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

	public MapagParserClassGenerator(GrammarInfo grammarInfo, StateMachine stateMachine) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.stateMachine = stateMachine;
	}

	public void generate() {

		StateMachineEncoder stateMachineEncoder = new StateMachineEncoder(grammarInfo, stateMachine);
		int numberOfStates = stateMachine.getStates().size();
		int numberOfTerminals = grammar.getTerminalDefinitions().size();
		int numberOfNonterminals = grammar.getNonterminalDefinitions().size();

		VelocityContext context = new VelocityContext();
		context.put("packageName", grammar.getPackageName());
		context.put("className", grammar.getClassName());
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
			int actionTableWidth = numberOfTerminals + numberOfNonterminals;
			int[] actionTable = new int[numberOfStates * actionTableWidth];
			for (State state : stateMachine.getStates()) {
				int stateIndex = stateMachineEncoder.getStateIndex(state);
				for (String terminal : grammar.getTerminalDefinitions().keySet()) {
					int symbolIndex = stateMachineEncoder.getSymbolIndex(terminal);
					int actionCode = stateMachineEncoder.getac
				}
			}
		}

		StringWriter sw = new StringWriter();
		engine.getTemplate("Parser.vm").merge(context, sw);
		System.out.println(sw);

	}

}
