package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.AlternativeAnnotation;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * TODO test names of generated alternatives (name of converted alternative is that of the original one; name of
 * synthetic alternatives is null; name of synthetic nonterminals includes the parent alternative name if any
 */
public class ProductionCanonicalizer {

	private final List<Production> pendingProductions;
	private final List<Production> nextPendingBatch;
	private final Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternatives;
	private final Map<String, Integer> syntheticNameCounters;
	private final Set<String> knownNonterminals = new HashSet<>();

	private String syntheticNamePrefix;

	public ProductionCanonicalizer(ImmutableList<Production> inputProductions) {
		ParameterUtil.ensureNotNull(inputProductions, "inputProductions");
		this.pendingProductions = new ArrayList<>(inputProductions);
		this.nextPendingBatch = new ArrayList<>();
		this.nonterminalAlternatives = new HashMap<>();
		this.syntheticNameCounters = new HashMap<>();
		for (Production production : inputProductions) {
			knownNonterminals.add(production.getLeftHandSide());
		}
	}

	public void run() {
		while (!pendingProductions.isEmpty()) {
			work();
		}
	}

	public Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> getNonterminalAlternatives() {
		return nonterminalAlternatives;
	}

	private void work() {
		// try to keep the order of input productions and synthetic productions so it's easier to debug the canonical grammar
		Production production = pendingProductions.remove(0);
		String leftHandSide = production.getLeftHandSide();
		for (name.martingeisse.mapag.grammar.extended.Alternative inputAlternative : production.getAlternatives()) {
			String alternativeName = (inputAlternative.getName() == null ? "" : inputAlternative.getName());
			this.syntheticNamePrefix = leftHandSide + '/' + alternativeName + '/';
			name.martingeisse.mapag.grammar.canonical.Alternative convertedAlternative = convertAlternative(inputAlternative);
			if (nonterminalAlternatives.get(leftHandSide) == null) {
				nonterminalAlternatives.put(leftHandSide, new ArrayList<>());
			}
			nonterminalAlternatives.get(leftHandSide).add(convertedAlternative);
		}
		if (!nextPendingBatch.isEmpty()) {
			pendingProductions.addAll(0, nextPendingBatch);
			nextPendingBatch.clear();
		}
	}

	private name.martingeisse.mapag.grammar.canonical.Alternative convertAlternative(name.martingeisse.mapag.grammar.extended.Alternative inputAlternative) {
		List<String> expansion = new ArrayList<>();
		List<String> expressionNames = new ArrayList<>();
		convertExpression(inputAlternative.getExpression(), expansion, expressionNames);
		return new name.martingeisse.mapag.grammar.canonical.Alternative(
			ImmutableList.copyOf(expansion),
			inputAlternative.getPrecedenceDefiningTerminal(),
			new AlternativeAnnotation(
				inputAlternative.getName(),
				ImmutableList.copyOf(expressionNames)
			)
		);
	}

	private void convertExpression(Expression expression, List<String> expansion, List<String> expressionNames) {
		if (expression instanceof EmptyExpression) {
			// nothing to do
		} else if (expression instanceof OneOrMoreExpression) {
			OneOrMoreExpression oneOrMoreExpression = (OneOrMoreExpression) expression;
			expansion.add(extractRepetition(oneOrMoreExpression.getOperand(), false));
			expressionNames.add(expression.getName());
		} else if (expression instanceof OptionalExpression) {
			expansion.add(extractOptionalExpression((OptionalExpression) expression));
			expressionNames.add(expression.getName());
		} else if (expression instanceof OrExpression) {
			expansion.add(extractOrExpression((OrExpression) expression));
			expressionNames.add(expression.getName());
		} else if (expression instanceof SequenceExpression) {
			if (expression.getName() == null) {
				SequenceExpression sequenceExpression = (SequenceExpression) expression;
				convertExpression(sequenceExpression.getLeft(), expansion, expressionNames);
				convertExpression(sequenceExpression.getRight(), expansion, expressionNames);
			} else {
				expansion.add(extractOpaqueExpression(expression));
				expressionNames.add(expression.getName());
			}
		} else if (expression instanceof SymbolReference) {
			SymbolReference symbolReference = (SymbolReference) expression;
			expansion.add(symbolReference.getSymbolName());
			expressionNames.add(expression.getName());
		} else if (expression instanceof ZeroOrMoreExpression) {
			ZeroOrMoreExpression zeroOrMoreExpression = (ZeroOrMoreExpression) expression;
			expansion.add(extractRepetition(zeroOrMoreExpression.getOperand(), true));
			expressionNames.add(expression.getName());
		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}
	}

	private String extractOrExpression(OrExpression expression) {
		return extractOpaqueExpression(expression);
	}

	private String extractOptionalExpression(OptionalExpression expression) {
		return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(null, new EmptyExpression(), null));
			for (Expression toplevelOrOperand : expression.getOperand().determineOrOperands()) {
				alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(null, toplevelOrOperand, null));
			}
		});
	}

	private String extractRepetition(Expression operand, boolean zeroAllowed) {
		return createSyntheticNonterminal(operand, (repetitionSyntheticName, alternatives) -> {
			String elementSyntheticName = extractOpaqueExpression(operand);
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				null,
				zeroAllowed ? new EmptyExpression() : new SymbolReference(elementSyntheticName),
				null));
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				null,
				new SequenceExpression(new SymbolReference(repetitionSyntheticName), new SymbolReference(elementSyntheticName)),
				null));
		});
	}

	/**
	 * Extracts the specified expression into a new synthetic nonterminal. This method asks the expression to generate
	 * sub-expressions for toplevel alternatives, but does not otherwise treat any kind of expression specially. This
	 * method should therefore not by used as the "normal" extraction method for optionals and repetition, since it
	 * would create an unnecessary nonterminal that just redirects to another one.
	 */
	private String extractOpaqueExpression(Expression expression) {
		return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
			for (Expression toplevelOrOperand : expression.determineOrOperands()) {
				alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(null, toplevelOrOperand, null));
			}
		});
	}

	/**
	 * Creates a synthetic nonterminal, using the specified callback to provide the expressions for its alternatives.
	 * All generated alternatives have undefined precedence.
	 *
	 * The original expression is passed to use its name, if any, for the synthetic nonterminal.
	 */
	private String createSyntheticNonterminal(Expression expression, BiConsumer<String, List<name.martingeisse.mapag.grammar.extended.Alternative>> alternativesAdder) {
		String syntheticName = createSyntheticName(expression);
		List<name.martingeisse.mapag.grammar.extended.Alternative> alternatives = new ArrayList<>();
		alternativesAdder.accept(syntheticName, alternatives);
		Production production = new Production(syntheticName, ImmutableList.copyOf(alternatives));
		pendingProductions.add(production);
		return syntheticName;
	}

	// TODO test name collision resolution
	private String createSyntheticName(Expression expression) {
		if (expression.getName() != null) {
			String syntheticName = syntheticNamePrefix + expression.getName();
			if (knownNonterminals.add(syntheticName)) {
				return syntheticName;
			}
		}
		String prefix = syntheticNamePrefix;
		if (expression.getName() != null) {
			prefix = prefix + expression.getName() + '/';
		}
		while (true) {
			int syntheticNameCounter = syntheticNameCounters.getOrDefault(prefix, 0);
			syntheticNameCounter++;
			syntheticNameCounters.put(prefix, syntheticNameCounter);
			String syntheticName = prefix + syntheticNameCounter;
			if (knownNonterminals.add(syntheticName)) {
				return syntheticName;
			}
		}
	}

}
