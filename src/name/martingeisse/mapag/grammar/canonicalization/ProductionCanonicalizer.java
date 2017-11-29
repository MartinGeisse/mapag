package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.AlternativeAttributes;
import name.martingeisse.mapag.grammar.canonical.Expansion;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.*;
import java.util.function.BiConsumer;

public class ProductionCanonicalizer {

	private final List<Production> pendingProductions;
	private final List<Production> nextPendingBatch;
	private final Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternatives;
	private final Map<String, NonterminalDefinition.PsiStyle> nonterminalPsiStyles;
	private final SyntheticNonterminalNameGenerator syntheticNonterminalNameGenerator;

	public ProductionCanonicalizer(Collection<String> terminals, ImmutableList<Production> inputProductions) {
		ParameterUtil.ensureNotNull(terminals, "terminals");
		ParameterUtil.ensureNotNull(inputProductions, "inputProductions");
		this.pendingProductions = new ArrayList<>(inputProductions);
		this.nextPendingBatch = new ArrayList<>();
		this.nonterminalAlternatives = new HashMap<>();
		this.nonterminalPsiStyles = new HashMap<>();
		this.syntheticNonterminalNameGenerator = new SyntheticNonterminalNameGenerator();
		for (String terminal : terminals) {
			syntheticNonterminalNameGenerator.registerKnownSymbol(terminal);
		}
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

	public Map<String, NonterminalDefinition.PsiStyle> getNonterminalPsiStyles() {
		return nonterminalPsiStyles;
	}

	private void work() {
		// try to keep the order of input productions and synthetic productions so it's easier to debug the canonical grammar
		Production production = pendingProductions.remove(0);
		String leftHandSide = production.getLeftHandSide();
		int alternativeCounter = 0;
		for (name.martingeisse.mapag.grammar.extended.Alternative inputAlternative : production.getAlternatives()) {
			syntheticNonterminalNameGenerator.prepare(leftHandSide, inputAlternative.getName());
			name.martingeisse.mapag.grammar.canonical.Alternative convertedAlternative = convertAlternative(inputAlternative, alternativeCounter);
			if (nonterminalAlternatives.get(leftHandSide) == null) {
				nonterminalAlternatives.put(leftHandSide, new ArrayList<>());
			}
			nonterminalAlternatives.get(leftHandSide).add(convertedAlternative);
			alternativeCounter++;
		}
		if (!nextPendingBatch.isEmpty()) {
			pendingProductions.addAll(0, nextPendingBatch);
			nextPendingBatch.clear();
		}
	}

	private name.martingeisse.mapag.grammar.canonical.Alternative convertAlternative(name.martingeisse.mapag.grammar.extended.Alternative inputAlternative, int alternativeCounter) {
		List<ExpansionElement> expansionElements = new ArrayList<>();
		convertExpressionToExpansion(inputAlternative.getExpression(), expansionElements);
		return new name.martingeisse.mapag.grammar.canonical.Alternative(
			inputAlternative.getName() == null ? ("a" + alternativeCounter) : inputAlternative.getName(),
			new Expansion(ImmutableList.copyOf(expansionElements)),
			convertAttributes(inputAlternative)
		);
	}

	private AlternativeAttributes convertAttributes(name.martingeisse.mapag.grammar.extended.Alternative inputAlternative) {

		ImmutableMap<String, ConflictResolution> conflictResolutionMap;
		if (inputAlternative.getResolveBlock() == null) {
			conflictResolutionMap = null;
		} else {
			Map<String, ConflictResolution> terminalToConflictResolution = new HashMap<>();
			for (ResolveDeclaration resolveDeclaration : inputAlternative.getResolveBlock().getResolveDeclarations()) {
				ConflictResolution resolution = resolveDeclaration.getConflictResolution();
				for (String terminal : resolveDeclaration.getTerminals()) {
					terminalToConflictResolution.put(terminal, resolution);
				}
			}
			conflictResolutionMap = ImmutableMap.copyOf(terminalToConflictResolution);
		}

		return new AlternativeAttributes(
			inputAlternative.getPrecedenceDefiningTerminal(),
			conflictResolutionMap,
			inputAlternative.isReduceOnError(),
			inputAlternative.isReduceOnEofOnly()
		);

	}

	private void convertExpressionToExpansion(Expression expression, List<ExpansionElement> expansionElements) {
		if (expression instanceof EmptyExpression) {

			// an empty expression becomes an empty expansion

		} else if (expression instanceof SequenceExpression && expression.getName() == null) {

			// an unnamed sequence gets "inlined" in the output expansion. (Named sequences get extracted in the
			// else-case below.)
			SequenceExpression sequenceExpression = (SequenceExpression) expression;
			convertExpressionToExpansion(sequenceExpression.getLeft(), expansionElements);
			convertExpressionToExpansion(sequenceExpression.getRight(), expansionElements);

		} else {

			// anything else must be converted to a single symbol that gets added to the expansion (this also handles
			// SymbolReferences and named sequences).
			expansionElements.add(new ExpansionElement(convertExpressionToSymbol(expression), expression.getName()));

		}
	}

	private String convertExpressionToSymbol(Expression expression) {
		if (expression instanceof EmptyExpression) {

			// strange case, but if the caller really needs a symbol for an EmptyExpression,
			// we must create a synthetic nonterminal with empty content
			return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
				alternatives.add(syntheticAlternative(null, expression));
			}, NonterminalDefinition.PsiStyle.NORMAL);

		} else if (expression instanceof SymbolReference) {

			// symbol references can be used as-is without extracting them
			return ((SymbolReference) expression).getSymbolName();

		} else if (expression instanceof OrExpression) {

			// an OR-expression can be extracted into alternatives
			return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
				for (Expression orOperand : getOrOperands(expression)) {
					alternatives.add(syntheticAlternative(orOperand.getName(), orOperand));
				}
			}, NonterminalDefinition.PsiStyle.NORMAL);

		} else if (expression instanceof SequenceExpression) {

			// A sequence gets extracted, but we must remove the expression's name so it gets inlined in the new
			// nonterminal -- otherwise we'd push it out to an infinite loop of new nonterminals.
			return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
				alternatives.add(syntheticAlternative(null, expression.withName(null)));
			}, NonterminalDefinition.PsiStyle.NORMAL);

		} else if (expression instanceof OptionalExpression) {

			// An OptionalExpression gets extracted into a two-alternative nonterminal
			Expression operand = ((OptionalExpression) expression).getOperand();
			extendOperandNameStart(expression);
			String operandSymbol = convertExpressionToSymbol(operand);
			extendOperandNameEnd(expression);
			Expression replacementOperand = new SymbolReference(operandSymbol).withName(operand.getName()).withFallbackName("it");
			return createSyntheticNonterminal(expression, (syntheticName, alternatives) -> {
				alternatives.add(syntheticAlternative("absent", new EmptyExpression()));
				alternatives.add(syntheticAlternative("present", replacementOperand));
			}, NonterminalDefinition.PsiStyle.OPTIONAL);

		} else if (expression instanceof ZeroOrMoreExpression) {

			// a repetition gets extracted into a two-alternative nonterminal
			ZeroOrMoreExpression zeroOrMoreExpression = (ZeroOrMoreExpression) expression;
			return extractRepetition(zeroOrMoreExpression, zeroOrMoreExpression.getOperand(), true);

		} else if (expression instanceof OneOrMoreExpression) {

			// a repetition gets extracted into a two-alternative nonterminal
			OneOrMoreExpression oneOrMoreExpression = (OneOrMoreExpression) expression;
			return extractRepetition(oneOrMoreExpression, oneOrMoreExpression.getOperand(), false);

		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}
	}

	// common handling for ZeroOrMoreExpression and OneOrMoreExpression
	private String extractRepetition(Expression repetition, Expression operand, boolean zeroAllowed) {
		extendOperandNameStart(repetition);
		String operandSymbol = convertExpressionToSymbol(operand);
		extendOperandNameEnd(repetition);
		Expression replacementOperand = new SymbolReference(operandSymbol).withName(operand.getName()).withFallbackName("element");
		NonterminalDefinition.PsiStyle psiStyle = (zeroAllowed ? NonterminalDefinition.PsiStyle.ZERO_OR_MORE : NonterminalDefinition.PsiStyle.ONE_OR_MORE);
		return createSyntheticNonterminal(repetition, (repetitionSyntheticName, alternatives) -> {
			alternatives.add(syntheticAlternative("start",
				zeroAllowed ? new EmptyExpression() : replacementOperand));
			alternatives.add(syntheticAlternative("next",
				new SequenceExpression(
					new SymbolReference(repetitionSyntheticName).withName("previous"),
					replacementOperand
				)));
		}, psiStyle);
	}

	// flattens a top-level OrExpression tree (if any) into a list of OR operands
	private ImmutableList<Expression> getOrOperands(Expression expression) {
		if (expression instanceof OrExpression) {
			OrExpression orExpression = (OrExpression) expression;
			ImmutableList<Expression> leftOperands = getOrOperands(orExpression.getLeftOperand());
			ImmutableList<Expression> rightOperands = getOrOperands(orExpression.getRightOperand());
			return ImmutableList.<Expression>builder().addAll(leftOperands).addAll(rightOperands).build();
		} else {
			return ImmutableList.of(expression);
		}
	}

	/**
	 * This method is used for an outer expression that contains an operand expression, i.e. an optional or repetition.
	 * Due to the way these are converted, the operand would normally be prefixed with the name of the original
	 * nonterminal, but not with the name of the outer expression (if any). This method causes the outer expression's
	 * name to be used too.
	 */
	//
	private void extendOperandNameStart(Expression outerExpression) {
		if (outerExpression.getName() != null) {
			syntheticNonterminalNameGenerator.save();
			syntheticNonterminalNameGenerator.extend(outerExpression.getName());
		}
	}

	private void extendOperandNameEnd(Expression outerExpression) {
		if (outerExpression.getName() != null) {
			syntheticNonterminalNameGenerator.restore();
		}
	}

	/**
	 * Creates a synthetic nonterminal, using the specified callback to provide the expressions for its alternatives.
	 * All generated alternatives have undefined precedence.
	 * <p>
	 * The original expression is passed to use its name, if any, for the synthetic nonterminal.
	 */
	private String createSyntheticNonterminal(
		Expression expression,
		BiConsumer<String, List<name.martingeisse.mapag.grammar.extended.Alternative>> alternativesAdder,
		NonterminalDefinition.PsiStyle psiStyle) {

		String syntheticName = syntheticNonterminalNameGenerator.createSyntheticName(expression);
		List<name.martingeisse.mapag.grammar.extended.Alternative> alternatives = new ArrayList<>();
		alternativesAdder.accept(syntheticName, alternatives);
		Production production = new Production(syntheticName, ImmutableList.copyOf(alternatives));
		pendingProductions.add(production);
		nonterminalPsiStyles.put(syntheticName, psiStyle);
		return syntheticName;
	}

	private name.martingeisse.mapag.grammar.extended.Alternative syntheticAlternative(String name, Expression expression) {
		return new name.martingeisse.mapag.grammar.extended.Alternative(name, expression, null, null, false, false);
	}

}
