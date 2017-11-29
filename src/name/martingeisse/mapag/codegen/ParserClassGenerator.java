package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.Action;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
	private final OutputFileFactory outputFileFactory;

	public ParserClassGenerator(GrammarInfo grammarInfo, StateMachine stateMachine, Configuration configuration, OutputFileFactory outputFileFactory) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.stateMachine = stateMachine;
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
	}

	public void generate() throws ConfigurationException, IOException {

		StateMachineEncoder stateMachineEncoder = new StateMachineEncoder(grammarInfo, stateMachine);
		int numberOfStates = stateMachine.getStates().size();
		int numberOfTerminals = grammar.getTerminalDefinitions().size();
		int numberOfNonterminals = grammar.getNonterminalDefinitions().size();

		StateInputExpectationBuilder stateInputExpectationBuilder = new StateInputExpectationBuilder(grammarInfo, stateMachine, configuration.getPrefixed("parser.error."));
		stateInputExpectationBuilder.build();

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("className", configuration.getRequired(CLASS_NAME_PROPERTY));
		{
			String[] terminalNames = new String[numberOfTerminals];
			for (String terminal : grammar.getTerminalDefinitions().keySet()) {
				int index = stateMachineEncoder.getTerminalIndex(terminal);
				terminalNames[index] = terminal;
			}
			context.put("tokenCodeToToken", terminalNames);
		}
		context.put("fileElementType", configuration.getRequired(FILE_ELEMENT_TYPE_PROPERTY));
		context.put("startStateCode", stateMachineEncoder.getStateIndex(stateMachine.getStartState()));
		{
			int actionTableWidth = 3 + numberOfTerminals + numberOfNonterminals;
			int[][] actionTable = new int[numberOfStates][actionTableWidth];
			for (State state : stateMachine.getStates()) {
				int stateIndex = stateMachineEncoder.getStateIndex(state);

				// %eof
				{
					Action action = stateMachine.getTerminalOrEofActions().get(state).get(SpecialSymbols.EOF_SYMBOL_NAME);
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][0] = actionCode;
				}

				// %error
				{
					Action.Shift action = stateMachine.getNonterminalActions().get(state).get(SpecialSymbols.ERROR_SYMBOL_NAME);
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][1] = actionCode;
				}

				// %badchar
				{
					Action action = state.getError();
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][2] = actionCode;
				}

				// terminals
				for (String terminal : grammar.getTerminalDefinitions().keySet()) {
					int symbolIndex = stateMachineEncoder.getSymbolIndex(terminal);
					Action action = stateMachine.getTerminalOrEofActions().get(state).get(terminal);
					int actionCode = stateMachineEncoder.getActionCode(action);
					actionTable[stateIndex][symbolIndex] = actionCode;
				}

				// nonterminals
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
		{
			int highestAlternativeIndex = 0;
			for (NonterminalDefinition nonterminalDefinition : grammarInfo.getGrammar().getNonterminalDefinitions().values()) {
				for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
					int alternativeIndex = stateMachineEncoder.getAlternativeIndex(nonterminalDefinition.getName(), alternative);
					highestAlternativeIndex = Math.max(highestAlternativeIndex, alternativeIndex);
				}
			}
			AlternativeEntry[] alternativeEntries = new AlternativeEntry[highestAlternativeIndex + 1];
			for (NonterminalDefinition nonterminalDefinition : grammarInfo.getGrammar().getNonterminalDefinitions().values()) {
				for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
					int alternativeIndex = stateMachineEncoder.getAlternativeIndex(nonterminalDefinition.getName(), alternative);
					String symbolVariable;
					if (nonterminalDefinition.getPsiStyle() == NonterminalDefinition.PsiStyle.OPTIONAL) {
						symbolVariable = IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
					} else {
						symbolVariable = IdentifierUtil.getAlternativeVariableIdentifier(nonterminalDefinition, alternative);
					}
					alternativeEntries[alternativeIndex] = new AlternativeEntry(
						alternative.getExpansion().getElements().size(),
						symbolVariable,
						stateMachineEncoder.getSymbolIndex(nonterminalDefinition.getName())
					);
				}
			}
			context.put("alternativeEntries", alternativeEntries);
		}
		{
			Map<State, String> stateInputExpectation = stateInputExpectationBuilder.getExpectations();
			String[] encodedStateInputExpectation = new String[numberOfStates];
			for (Map.Entry<State, String> entry : stateInputExpectation.entrySet()) {
				int stateIndex = stateMachineEncoder.getStateIndex(entry.getKey());
				encodedStateInputExpectation[stateIndex] = entry.getValue();
			}
			context.put("stateInputExpectation", encodedStateInputExpectation);
		}

		try (OutputStream outputStream = outputFileFactory.createOutputFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), configuration.getRequired(CLASS_NAME_PROPERTY))) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("Parser.vm").merge(context, outputStreamWriter);
			}
		}

	}

	public static class AlternativeEntry {

		private final int rightHandSideLength;
		private final String nonterminalName;
		private final int nonterminalSymbolCode;

		public AlternativeEntry(int rightHandSideLength, String nonterminalName, int nonterminalSymbolCode) {
			this.rightHandSideLength = rightHandSideLength;
			this.nonterminalName = nonterminalName;
			this.nonterminalSymbolCode = nonterminalSymbolCode;
		}

		public int getRightHandSideLength() {
			return rightHandSideLength;
		}

		public String getNonterminalName() {
			return nonterminalName;
		}

		public int getNonterminalSymbolCode() {
			return nonterminalSymbolCode;
		}

	}

}
