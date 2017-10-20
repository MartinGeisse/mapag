package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.expression.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 *
 */
public class ProductionCanonicalizer {

	private final List<Production> todoProductions;
	private final List<Production> nextTodoBatch;
	private final Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternatives;

	private String syntheticNamePrefix;
	private int syntheticNameCounter;

	public ProductionCanonicalizer(ImmutableList<Production> inputProductions) {
		this.todoProductions = new ArrayList<>(inputProductions);
		this.nextTodoBatch = new ArrayList<>();
		this.nonterminalAlternatives = new HashMap<>();
	}

	public void run() {
		while (!todoProductions.isEmpty()) {
			work();
		}
	}

	public Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> getNonterminalAlternatives() {
		return nonterminalAlternatives;
	}

	private void work() {
		// try to keep the order of input productions and synthetic productions se it's easier to debug the canonical grammar
		Production production = todoProductions.remove(0);
		String leftHandSide = production.getLeftHandSide();
		this.syntheticNamePrefix = leftHandSide;
		this.syntheticNameCounter = 0;
		for (name.martingeisse.mapag.grammar.extended.Alternative inputAlternative : production.getAlternatives()) {
			name.martingeisse.mapag.grammar.canonical.Alternative convertedAlternative = convertAlternative(inputAlternative);
			if (nonterminalAlternatives.get(leftHandSide) == null) {
				nonterminalAlternatives.put(leftHandSide, new ArrayList<>());
			}
			nonterminalAlternatives.get(leftHandSide).add(convertedAlternative);
		}
		if (!nextTodoBatch.isEmpty()) {
			todoProductions.addAll(0, nextTodoBatch);
			nextTodoBatch.clear();
		}
	}

	private name.martingeisse.mapag.grammar.canonical.Alternative convertAlternative(name.martingeisse.mapag.grammar.extended.Alternative inputAlternative) {
		List<String> expansion = new ArrayList<>();
		convertExpression(inputAlternative.getExpression(), expansion);
		return new name.martingeisse.mapag.grammar.canonical.Alternative(ImmutableList.copyOf(expansion), inputAlternative.getPrecedenceDefiningTerminal());
	}

	private void convertExpression(Expression expression, List<String> expansion) {
		if (expression instanceof EmptyExpression) {
			// nothing to do
		} else if (expression instanceof OneOrMoreExpression) {
			OneOrMoreExpression oneOrMoreExpression = (OneOrMoreExpression) expression;
			expansion.add(extractRepetition(oneOrMoreExpression.getOperand(), false));
		} else if (expression instanceof OptionalExpression) {
			expansion.add(extractOptionalExpression((OptionalExpression) expression));
		} else if (expression instanceof OrExpression) {
			expansion.add(extractOrExpression((OrExpression) expression));
		} else if (expression instanceof SequenceExpression) {
			SequenceExpression sequenceExpression = (SequenceExpression) expression;
			convertExpression(sequenceExpression.getLeft(), expansion);
			convertExpression(sequenceExpression.getRight(), expansion);
		} else if (expression instanceof SymbolReference) {
			SymbolReference symbolReference = (SymbolReference) expression;
			expansion.add(symbolReference.getSymbolName());
		} else if (expression instanceof ZeroOrMoreExpression) {
			ZeroOrMoreExpression zeroOrMoreExpression = (ZeroOrMoreExpression) expression;
			expansion.add(extractRepetition(zeroOrMoreExpression.getOperand(), true));
		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}
	}

	private String extractOrExpression(OrExpression expression) {
		return extractOpaqueExpression(expression);
	}

	private String extractOptionalExpression(OptionalExpression expression) {
		return createSyntheticNonterminal((syntheticName, alternatives) -> {
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(new EmptyExpression(), null));
			for (Expression toplevelOrOperand : expression.getOperand().determineOrOperands()) {
				alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(toplevelOrOperand, null));
			}
		});
	}

	private String extractRepetition(Expression operand, boolean zeroAllowed) {
		return createSyntheticNonterminal((repetitionSyntheticName, alternatives) -> {
			String elementSyntheticName = extractOpaqueExpression(operand);
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
				zeroAllowed ? new EmptyExpression() : new SymbolReference(elementSyntheticName),
				null));
			alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(
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
		return createSyntheticNonterminal((syntheticName, alternatives) -> {
			for (Expression toplevelOrOperand : expression.determineOrOperands()) {
				alternatives.add(new name.martingeisse.mapag.grammar.extended.Alternative(toplevelOrOperand, null));
			}
		});
	}

	/**
	 * Creates a synthetic nonterminal, using the specified callback to provide the expressions for its alternatives.
	 * All generated alternatives have undefined precedence.
	 */
	private String createSyntheticNonterminal(BiConsumer<String, List<name.martingeisse.mapag.grammar.extended.Alternative>> alternativesAdder) {
		syntheticNameCounter++;
		String syntheticName = syntheticNamePrefix + '_' + syntheticNameCounter;
		List<name.martingeisse.mapag.grammar.extended.Alternative> alternatives = new ArrayList<>();
		alternativesAdder.accept(syntheticName, alternatives);
		Production production = new Production(syntheticName, ImmutableList.copyOf(alternatives));
		todoProductions.add(production);
		return syntheticName;
	}

}
