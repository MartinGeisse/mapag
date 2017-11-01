package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.Action;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.Properties;

/**
 *
 */
public class ParserClassGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "parser.package";
	public static final String CLASS_NAME_PROPERTY = "parser.class";
	public static final String FILE_ELEMENT_TYPE_PROPERTY = "parser.fileElementType";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final StateMachine stateMachine;
	private final Configuration configuration;

	public ParserClassGenerator(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.stateMachine = stateMachine;
		this.configuration = configuration;
	}

	public void generate() throws ConfigurationException {

		StateMachineEncoder stateMachineEncoder = new StateMachineEncoder(grammarInfo, stateMachine);
		int numberOfStates = stateMachine.getStates().size();
		int numberOfTerminals = grammar.getTerminalDefinitions().size();
		int numberOfNonterminals = grammar.getNonterminalDefinitions().size();

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("className", configuration.getRequired(CLASS_NAME_PROPERTY));
		{
			String[] terminalNames = new String[numberOfTerminals];
			for (String terminal : grammar.getTerminalDefinitions().keySet()) {
				int index = stateMachineEncoder.getTerminalIndex(terminal);
				terminalNames[index] = terminal;
			}
			context.put("tokensInTokenCodeOrder", terminalNames);
		}
		context.put("fileElementType", configuration.getRequired(FILE_ELEMENT_TYPE_PROPERTY));
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
		MapagVelocityEngine.engine.getTemplate("Parser.vm").merge(context, sw);
		System.out.println(sw);

	}

}
