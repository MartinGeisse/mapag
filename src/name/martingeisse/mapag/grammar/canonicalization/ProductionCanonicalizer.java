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

			// An OptionalExpression gets extracted into a two-alternative nonterminal, but with special naming
			Expression originalOperand = ((OptionalExpression) expression).getOperand();
			Expression namedOperand = originalOperand.withFallbackName(expression.getName());
			String operandSymbol = convertExpressionToSymbol(namedOperand);
			Expression replacementOperand = new SymbolReference(operandSymbol).withName("it");
			extendNameStart(namedOperand);
			String syntheticNonterminal = createSyntheticNonterminal("optional", (syntheticName, alternatives) -> {
				alternatives.add(syntheticAlternative("absent", new EmptyExpression()));
				alternatives.add(syntheticAlternative("present", replacementOperand));
			}, NonterminalDefinition.PsiStyle.OPTIONAL);
			extendNameEnd(namedOperand);
			return syntheticNonterminal;

		} else if (expression instanceof ZeroOrMoreExpression) {

			// a repetition gets extracted into a two-alternative nonterminal
			ZeroOrMoreExpression zeroOrMoreExpression = (ZeroOrMoreExpression) expression;
			return extractRepetition(zeroOrMoreExpression, zeroOrMoreExpression.getOperand(), true);

		} else if (expression instanceof OneOrMoreExpression) {

			// a repetition gets extracted into a two-alternative nonterminal
			OneOrMoreExpression oneOrMoreExpression = (OneOrMoreExpression) expression;
			return extractRepetition(oneOrMoreExpression, oneOrMoreExpression.getOperand(), false);

		} else if (expression instanceof Repetition) {

			// convert the element using the original name, then use that name as the basis for generating names
			Repetition repetition = (Repetition)expression;
			Expression namedElement = repetition.getElementExpression().withFallbackName(expression.getName());
			Expression replacementElement = new SymbolReference(convertExpressionToSymbol(namedElement)).withName("element");
			extendNameStart(namedElement);

			// convert the separator, if any
			Expression replacementSeparator;
			if (repetition.getSeparatorExpression() == null) {
				replacementSeparator = null;
			} else {
				replacementSeparator = new SymbolReference(convertExpressionToSymbol(repetition.getSeparatorExpression())).withName("separator");
			}

			// Convert the repetition, using _list for the name. We treat zero-or-more with separator as one-or-more
			// with separator for now and handle it below.
			NonterminalDefinition.PsiStyle repetitionPsiStyle;
			String repetitionNonterminalNameSuggestion = "list";
			if (replacementSeparator != null) {
				repetitionPsiStyle = NonterminalDefinition.PsiStyle.SEPARATED_ONE_OR_MORE;
				if (!repetition.isEmptyAllowed()) {
					repetitionNonterminalNameSuggestion = "nonemptyList";
				}
			} else if (repetition.isEmptyAllowed()) {
				repetitionPsiStyle = NonterminalDefinition.PsiStyle.ZERO_OR_MORE;
			} else {
				repetitionPsiStyle = NonterminalDefinition.PsiStyle.ONE_OR_MORE;
			}
			String repetitionNonterminal = createSyntheticNonterminal(repetitionNonterminalNameSuggestion, (repetitionSyntheticName, alternatives) -> {

				// base case
				boolean emptyBaseCase = repetition.isEmptyAllowed() && replacementSeparator == null;
				alternatives.add(syntheticAlternative("start", emptyBaseCase ? new EmptyExpression() : replacementElement));

				// repetition case
				Expression repetitionDelta = replacementSeparator == null ? replacementElement : new SequenceExpression(replacementSeparator, replacementElement);
				alternatives.add(syntheticAlternative("next",
					new SequenceExpression(new SymbolReference(repetitionSyntheticName).withName("previous"), repetitionDelta))
				);

			}, repetitionPsiStyle);

			// Now, if we have a zero-or-more repetition with separator, we must still handle the real base case
			String resultingNonterminal;
			if (repetition.isEmptyAllowed() && repetition.getSeparatorExpression() != null) {
				resultingNonterminal = createSyntheticNonterminal("list", (syntheticName, alternatives) -> {
					alternatives.add(syntheticAlternative("empty", new EmptyExpression()));
					alternatives.add(syntheticAlternative("nonempty", new SymbolReference(repetitionNonterminal).withName("nonempty")));
				}, NonterminalDefinition.PsiStyle.OPTIONAL_SEPARATED_ONE_OR_MORE);
			} else {
				resultingNonterminal = repetitionNonterminal;
			}

			// done -- reset naming
			extendNameEnd(namedElement);
			return resultingNonterminal;

		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}
	}

	// common handling for ZeroOrMoreExpression and OneOrMoreExpression
	private String extractRepetition(Expression repetition, Expression operand, boolean zeroAllowed) {
		extendNameStart(repetition);
		String operandSymbol = convertExpressionToSymbol(operand);
		extendNameEnd(repetition);
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
	 * Uses the specified expression as the starting point for generating synthetic names, in addition to whatever
	 * prefix is already there.
	 */
	private void extendNameStart(Expression expression) {
		extendNameStart(expression.getName());
	}

	/**
	 * Uses the specified name as the starting point for generating synthetic names, in addition to whatever
	 * prefix is already there.
	 */
	private void extendNameStart(String name) {
		if (name != null) {
			syntheticNonterminalNameGenerator.save();
			syntheticNonterminalNameGenerator.extend(name);
		}
	}

	/**
	 * Resets naming to the original behavior before calling extendNameStart().
	 */
	private void extendNameEnd(Expression expression) {
		extendNameEnd(expression.getName());
	}

	/**
	 * Resets naming to the original behavior before calling extendNameStart().
	 */
	private void extendNameEnd(String name) {
		if (name != null) {
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

		return createSyntheticNonterminal(expression.getName(), alternativesAdder, psiStyle);
	}

	/**
	 * Creates a synthetic nonterminal, using the specified callback to provide the expressions for its alternatives.
	 * All generated alternatives have undefined precedence.
	 * <p>
	 * The original expression is passed to use its name, if any, for the synthetic nonterminal.
	 */
	private String createSyntheticNonterminal(
		String suggestedName,
		BiConsumer<String, List<name.martingeisse.mapag.grammar.extended.Alternative>> alternativesAdder,
		NonterminalDefinition.PsiStyle psiStyle) {

		String syntheticName = syntheticNonterminalNameGenerator.createSyntheticName(suggestedName);
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
