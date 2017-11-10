package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.canonical.AlternativeAnnotation;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * TODO test names of generated alternatives (name of converted alternative is that of the original one; name of
 * synthetic alternatives is null; name of synthetic nonterminals includes the parent alternative name if any
 */
public class ProductionCanonicalizer {

	private final List<Production> pendingProductions;
	private final List<Production> nextPendingBatch;
	private final Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternatives;
	private final SyntheticNonterminalNameGenerator syntheticNonterminalNameGenerator;

	public ProductionCanonicalizer(ImmutableList<Production> inputProductions) {
		ParameterUtil.ensureNotNull(inputProductions, "inputProductions");
		this.pendingProductions = new ArrayList<>(inputProductions);
		this.nextPendingBatch = new ArrayList<>();
		this.nonterminalAlternatives = new HashMap<>();
		this.syntheticNonterminalNameGenerator = new SyntheticNonterminalNameGenerator();
		// TODO also register known nonterminals
		for (Production production : inputProductions) {
			syntheticNonterminalNameGenerator.registerKnownSymbol(production.getLeftHandSide());
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
			syntheticNonterminalNameGenerator.prepare(leftHandSide, inputAlternative.getName());
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
			expansion.add(extractRepetition(oneOrMoreExpression, oneOrMoreExpression.getOperand(), false));
			expressionNames.add(expression.getNameOrEmpty());
		} else if (expression instanceof OptionalExpression) {
			expansion.add(extractOptionalExpression((OptionalExpression) expression));
			expressionNames.add(expression.getNameOrEmpty());
		} else if (expression instanceof OrExpression) {
			// TODO this turns expression names into alternative names and thus makes the expressions inaccessible!
			expansion.add(extractOpaqueExpression(expression));
			expressionNames.add(expression.getNameOrEmpty());
		} else if (expression instanceof SequenceExpression) {
			if (expression.getName() == null) {
				SequenceExpression sequenceExpression = (SequenceExpression) expression;
				convertExpression(sequenceExpression.getLeft(), expansion, expressionNames);
				convertExpression(sequenceExpression.getRight(), expansion, expressionNames);
			} else {
				expansion.add(extractOpaqueExpression(expression));
				expressionNames.add(expression.getNameOrEmpty());
			}
		} else if (expression instanceof SymbolReference) {
			SymbolReference symbolReference = (SymbolReference) expression;
			expansion.add(symbolReference.getSymbolName());
			expressionNames.add(expression.getNameOrEmpty());
		} else if (expression instanceof ZeroOrMoreExpression) {
			ZeroOrMoreExpression zeroOrMoreExpression = (ZeroOrMoreExpression) expression;
			expansion.add(extractRepetition(zeroOrMoreExpression, zeroOrMoreExpression.getOperand(), true));
			expressionNames.add(expression.getNameOrEmpty());
		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}
	}

	private String extractOptionalExpression(OptionalExpression expression) {
		// Note: we could add the top-level OR operands from the optional's operand as alternatives of the synthetic
		// nonterminal... but that would be too much "magic" that confuses the user and also makes code-generation
		// very non-uniform just for the sake of being clever.
		return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
			// TODO use the optional's name for the operand (unless the operand already has a name) so it becomes the name of the synth NT and the generated PSI class
			String operandSymbol = toSingleSymbol(expression.getOperand());
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				"absent", new EmptyExpression(), null));
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				"present", expression.getOperand().withFallbackName("it"), null));
		});
	}

	private String extractRepetition(Expression repetition, Expression operand, boolean zeroAllowed) {
		// Note that we just call the elements "elements" here and don't care about their expression name (if any)
		// because we intent to store them in a List object anyway.
		return createSyntheticNonterminal(repetition, (repetitionSyntheticName, alternatives) -> {
			String operandSymbol = toSingleSymbol(operand);
			// TODO use the repetition's name for the operand (something like RepnameElement) (unless the operand already has a name) so it becomes the name of the synth NT and the generated PSI class
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				"start",
				zeroAllowed ? new EmptyExpression() : new SymbolReference(operandSymbol).withName("element"),
				null));
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				"next",
				new SequenceExpression(
					new SymbolReference(repetitionSyntheticName).withName("previous"),
					new SymbolReference(operandSymbol).withName("element")
				), null));
		});
	}

	/**
	 * Turns the specified expression into a single-symbol expression, generating synthetic nonterminals if needed,
	 * and returns that symbol. The caller should usually use the original expression's name for the returned
	 * symbol.
	 */
	private String toSingleSymbol(Expression expression) {
		if (expression instanceof SymbolReference) {
			return ((SymbolReference) expression).getSymbolName();
		} else {
			return extractOpaqueExpression(expression);
		}
	}

	/**
	 * Extracts the specified expression into a new synthetic nonterminal:
	 *
	 * - the expression's name will be used for the synthetic nonterminal, and the expression becomes the "content"
	 *   of the synthetic nonterminal.
	 *
	 * - if the expression is an OR expression, then its operands become alternatives. The OR's name gets lost at
	 *   expression level. The operand's names become alternative names and are also attached to the alternatives'
	 *   content to generate getters; the exception is if an operand is a sequence -- then a getter is useless and
	 *   would generate another redundant nonterminal.
	 *
	 * - if the expression is not an OR expression, then the synthetic nonterminal as a single unnamed alternative
	 *   whose content is the expression itself.
	 *
	 * - if the expression is a sequence, then its name gets dropped at expression level because keeping it would
	 *   cause an infinite loop of newly created nonterminals. A getter for the whole sequence would be useless anyway.
	 *
	 * - for all other expressions, the name is kept.
	 */
	private String extractOpaqueExpression(Expression expression) {
		return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {

			List<Expression> orOperands = new ArrayList<>();
			collectOrOperands(expression, orOperands);

			for (Expression orOperand : orOperands) {
				String alternativeName = (expression instanceof OrExpression) ? orOperand.getName() : null;
				if (orOperand instanceof SequenceExpression) {
					orOperand = orOperand.withName(null);
				}
				alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(alternativeName, orOperand, null));
			}

		});
	}

	private void collectOrOperands(Expression expression, List<Expression> operands) {
		if (expression instanceof OrExpression) {
			OrExpression orExpression = (OrExpression)expression;
			collectOrOperands(orExpression.getLeftOperand(), operands);
			collectOrOperands(orExpression.getRightOperand(), operands);
		} else {
			operands.add(expression);
		}
	}

	/**
	 * Creates a synthetic nonterminal, using the specified callback to provide the expressions for its alternatives.
	 * All generated alternatives have undefined precedence.
	 * <p>
	 * The original expression is passed to use its name, if any, for the synthetic nonterminal.
	 */
	private String createSyntheticNonterminal(Expression expression, BiConsumer<String, List<name.martingeisse.mapag.grammar.extended.Alternative>> alternativesAdder) {
		String syntheticName = syntheticNonterminalNameGenerator.createSyntheticName(expression);
		List<name.martingeisse.mapag.grammar.extended.Alternative> alternatives = new ArrayList<>();
		alternativesAdder.accept(syntheticName, alternatives);
		Production production = new Production(syntheticName, ImmutableList.copyOf(alternatives));
		pendingProductions.add(production);
		return syntheticName;
	}

}
