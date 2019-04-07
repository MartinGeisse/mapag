package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.codegen.highlevel.StateMachineEncoder;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.PsiStyle;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.Action;
import name.martingeisse.mapag.sm.State;
import name.martingeisse.mapag.sm.StateMachine;
import org.apache.velocity.VelocityContext;

import java.io.DataOutputStream;
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
	public static final String PSI_PACKAGE_NAME_PROPERTY = "psi.package";
	public static final String CLASS_NAME_PROPERTY = "parser.class";
	public static final String DEBUG_PROPERTY = "parser.debug";
	public static final String FILE_ELEMENT_TYPE_PROPERTY = "parser.fileElementType";
	public static final String SYMBOL_HOLDER_PACKAGE_NAME_PROPERTY = "symbolHolder.package";
	public static final String SYMBOL_HOLDER_CLASS_NAME_PROPERTY = "symbolHolder.class";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final StateMachine stateMachine;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;
	private final CodeGenerationContext codeGenerationContext;

	public ParserClassGenerator(CodeGenerationParameters parameters) {
		this.grammarInfo = parameters.getGrammarInfo();
		this.grammar = grammarInfo.getGrammar();
		this.stateMachine = parameters.getStateMachine();
		this.configuration = parameters.getConfiguration();
		this.outputFileFactory = parameters.getOutputFileFactory();
		this.codeGenerationContext = parameters.getContext();
	}

	public void generate() throws ConfigurationException, IOException {

		StateMachineEncoder stateMachineEncoder = new StateMachineEncoder(grammarInfo, stateMachine);
		int numberOfStates = stateMachine.getStates().size();
		int numberOfTerminals = grammar.getTerminalDefinitions().size();
		int numberOfNonterminals = grammar.getNonterminalDefinitions().size();
		int actionTableWidth = 3 + numberOfTerminals + numberOfNonterminals;

		StateInputExpectationBuilder stateInputExpectationBuilder = new StateInputExpectationBuilder(grammarInfo, stateMachine, configuration.getPrefixed("parser.error."));
		stateInputExpectationBuilder.build();

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
		context.put("psiPackageName", configuration.getRequired(PSI_PACKAGE_NAME_PROPERTY));
		context.put("className", configuration.getRequired(CLASS_NAME_PROPERTY));
		context.put("intellij", codeGenerationContext.isIntellij());
		context.put("notNull", codeGenerationContext.getNotNullAnnotation());
		context.put("debug", "true".equals(configuration.getOptional(DEBUG_PROPERTY)));
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
		context.put("numberOfStates", numberOfStates);
		context.put("actionTableWidth", actionTableWidth);
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
					String parseNodeHead;
					String symbolHolderPrefix = configuration.getRequired(SYMBOL_HOLDER_PACKAGE_NAME_PROPERTY) + '.' + configuration.getRequired(SYMBOL_HOLDER_CLASS_NAME_PROPERTY);

					if (nonterminalDefinition.getPsiStyle() instanceof PsiStyle.Repetition) {
						String symbol = symbolHolderPrefix + '.' + IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
						parseNodeHead = "new ListNodeGenerationWrapper(" + symbol + ")";
					} else if (nonterminalDefinition.getPsiStyle().isDistinctSymbolPerAlternative()) {
						parseNodeHead = symbolHolderPrefix + '.' + IdentifierUtil.getAlternativeVariableIdentifier(nonterminalDefinition, alternative);
					} else {
						parseNodeHead = symbolHolderPrefix + '.' + IdentifierUtil.getNonterminalVariableIdentifier(nonterminalDefinition);
					}
					alternativeEntries[alternativeIndex] = new AlternativeEntry(
						alternative.getExpansion().getElements().size(),
						parseNodeHead,
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

		try (OutputStream outputStream = outputFileFactory.createSourceFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), configuration.getRequired(CLASS_NAME_PROPERTY))) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("templates/Parser.vm").merge(context, outputStreamWriter);
			}
		}

		try (OutputStream outputStream = outputFileFactory.createResourceFile(configuration.getRequired(CLASS_NAME_PROPERTY) + ".actions")) {
			try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {

				State[] states = new State[stateMachine.getStates().size()];
				for (State state : stateMachine.getStates()) {
					states[stateMachineEncoder.getStateIndex(state)] = state;
				}

				for (int stateIndex = 0; stateIndex < states.length; stateIndex++) {
					State state = states[stateIndex];
					int[] actionTableRow = new int[actionTableWidth];

					// %eof
					{
						Action action = stateMachine.getTerminalOrEofActions().get(state).get(SpecialSymbols.EOF_SYMBOL_NAME);
						int actionCode = stateMachineEncoder.getActionCode(action);
						actionTableRow[0] = actionCode;
					}

					// %error
					{
						Action.Shift action = stateMachine.getNonterminalActions().get(state).get(SpecialSymbols.ERROR_SYMBOL_NAME);
						int actionCode = stateMachineEncoder.getActionCode(action);
						actionTableRow[1] = actionCode;
					}

					// %badchar
					{
						Action action = state.getError();
						int actionCode = stateMachineEncoder.getActionCode(action);
						actionTableRow[2] = actionCode;
					}

					// terminals
					for (String terminal : grammar.getTerminalDefinitions().keySet()) {
						int symbolIndex = stateMachineEncoder.getSymbolIndex(terminal);
						Action action = stateMachine.getTerminalOrEofActions().get(state).get(terminal);
						int actionCode = stateMachineEncoder.getActionCode(action);
						actionTableRow[symbolIndex] = actionCode;
					}

					// nonterminals
					for (String nonterminal : grammar.getNonterminalDefinitions().keySet()) {
						int symbolIndex = stateMachineEncoder.getSymbolIndex(nonterminal);
						Action.Shift action = stateMachine.getNonterminalActions().get(state).get(nonterminal);
						int actionCode = stateMachineEncoder.getActionCode(action);
						actionTableRow[symbolIndex] = actionCode;
					}

					// write that row to the file
					for (int actionCode : actionTableRow) {
						dataOutputStream.writeInt(actionCode);
					}

				}

			}
		}

	}

	public static class AlternativeEntry {

		private final int rightHandSideLength;
		private final String parseNodeHead;
		private final int nonterminalSymbolCode;

		public AlternativeEntry(int rightHandSideLength, String parseNodeHead, int nonterminalSymbolCode) {
			this.rightHandSideLength = rightHandSideLength;
			this.parseNodeHead = parseNodeHead;
			this.nonterminalSymbolCode = nonterminalSymbolCode;
		}

		public int getRightHandSideLength() {
			return rightHandSideLength;
		}

		public String getParseNodeHead() {
			return parseNodeHead;
		}

		public int getNonterminalSymbolCode() {
			return nonterminalSymbolCode;
		}

	}

}
