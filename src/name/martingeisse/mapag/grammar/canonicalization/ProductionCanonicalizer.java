package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.AlternativeAttributes;
import name.martingeisse.mapag.grammar.canonical.Expansion;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.grammar.canonical.PsiStyle;
import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.*;

public class ProductionCanonicalizer {

	private final List<Production> pendingProductions;
	private final List<Production> nextPendingBatch;
	private final Map<String, List<name.martingeisse.mapag.grammar.canonical.Alternative>> nonterminalAlternatives;
	private final Map<String, PsiStyle> nonterminalPsiStyles;
	private final SyntheticNonterminalNameGenerator syntheticNonterminalNameGenerator;
	private final Map<Expression, String> mergedSyntheticNonterminals = new HashMap<>();

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

	public Map<String, PsiStyle> getNonterminalPsiStyles() {
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

		// symbol references can be used as-is without extracting them
		if (expression instanceof SymbolReference) {
			return ((SymbolReference) expression).getSymbolName();
		}

		// Anything else generates a synthetic nonterminal and is potentially subject to merging, so we'll cehck first
		// if a merged version of this expression already exists as a nonterminal. Note that the lookup key for merging
		// is the expression, not the generated nonterminal name. We do have to remove the expression name first
		// though, since that name is irrelevant for merging and conceptually "outside" the merged nonterminal.
		{
			Expression anonymousExpression = expression.withName(null);
			String existingMergedName = mergedSyntheticNonterminals.get(anonymousExpression);
			if (existingMergedName != null) {
				return existingMergedName;
			}
		}

		// build the synthetic nonterminal
		if (expression instanceof EmptyExpression) {

			// strange case, but if the caller really needs a symbol for an EmptyExpression,
			// we must create a synthetic nonterminal with empty content
			String nonterminal = syntheticNonterminalNameGenerator.createSyntheticName(expression.getName());
			createSyntheticNonterminal(nonterminal, ImmutableList.of(syntheticAlternative(null, expression)), PsiStyle.Normal.INSTANCE);
			return nonterminal;

		} else if (expression instanceof OrExpression) {

			// an OR-expression can be extracted into alternatives
			String nonterminal = syntheticNonterminalNameGenerator.createSyntheticName(expression.getName());
			List<Alternative> alternatives = new ArrayList<>();
			for (Expression orOperand : getOrOperands(expression)) {
				// for a sequence, we'll have to remove the name, or the expression gets pushed out into yet another nonterminal
				String alternativeName = orOperand.getName();
				if (orOperand instanceof SequenceExpression) {
					orOperand = orOperand.withName(null);
				}
				alternatives.add(syntheticAlternative(alternativeName, orOperand));
			}
			createSyntheticNonterminal(nonterminal, alternatives, PsiStyle.Normal.INSTANCE);
			return nonterminal;

		} else if (expression instanceof SequenceExpression) {

			// A sequence gets extracted, but we must remove the expression's name so it gets inlined in the new
			// nonterminal -- otherwise we'd push it out to an infinite loop of new nonterminals.
			String nonterminal = syntheticNonterminalNameGenerator.createSyntheticName(expression.getName());
			createSyntheticNonterminal(nonterminal, ImmutableList.of(syntheticAlternative(null, expression.withName(null))), PsiStyle.Normal.INSTANCE);
			return nonterminal;

		} else if (expression instanceof OptionalExpression) {

			// An OptionalExpression gets extracted into a two-alternative nonterminal
			OptionalExpression optionalExpression = (OptionalExpression) expression;

			// check if this expression can be merged, then choose a name
			boolean merge = (optionalExpression.getOperand() instanceof SymbolReference);
			String nonterminal;
			if (merge) {
				nonterminal = "synthetic/optional/" + ((SymbolReference)optionalExpression.getOperand()).getSymbolName();
				mergedSyntheticNonterminals.put(expression.withName(null), nonterminal);
			} else {
				syntheticNonterminalNameGenerator.save();
				syntheticNonterminalNameGenerator.extend(expression.getName());
				nonterminal = syntheticNonterminalNameGenerator.createSyntheticName("optional");
				syntheticNonterminalNameGenerator.restore();
			}

			// Convert the operand. The expression name we assign here is the desired name in case of a non-merged
			// nonterminal. In case of merging, we have a single symbol so the expression name is ignored anyway.
			String operandSymbol = convertExpressionToSymbol(optionalExpression.getOperand().withName(expression.getName()));
			Expression replacementOperand = new SymbolReference(operandSymbol).withName("it");

			// create a nonterminal for the OptionalExpression
			List<Alternative> alternatives = new ArrayList<>();
			alternatives.add(syntheticAlternative("absent", new EmptyExpression()));
			alternatives.add(syntheticAlternative("present", replacementOperand));
			createSyntheticNonterminal(nonterminal, alternatives, new PsiStyle.Optional(operandSymbol));
			return nonterminal;

		} else if (expression instanceof Repetition) {

			// A repetition gets extracted into a recursive two-alternative nonterminal. If the repetition is emptyable
			// and has a separator, that nonterminal must be wrapped into another OptionalExpression-like nonterminal
			// (try to build the canonical grammar by hand if you don't believe this).
			Repetition repetition = (Repetition) expression;
			boolean hasSeparator = repetition.getSeparatorExpression() != null;
			boolean wrapInOptional = repetition.isEmptyAllowed() && hasSeparator;

			// Check if this expression can be merged, then choose a name. Note that we call the nonterminal for the
			// whole expression "list", no matter whether it is wrapped in an optional (i.e. whether it is an
			// emptyable separated list). This keeps things simple and those names are only reflected in the parser
			// symbols -- the PSI nodes use generic list classes anyway. The repetitionNonterminal is the nonterminal
			// used for the actual repetition, that is, the wrapped one in case of an emptyable separated list.
			boolean merge = (repetition.getElementExpression() instanceof SymbolReference &&
				(repetition.getSeparatorExpression() == null || repetition.getSeparatorExpression() instanceof SymbolReference));
			String nonterminal, repetitionNonterminal;
			if (merge) {
				String elementSymbol = ((SymbolReference)repetition.getElementExpression()).getSymbolName();
				if (hasSeparator) {
					String separatorSymbol = ((SymbolReference)repetition.getSeparatorExpression()).getSymbolName();
					if (repetition.isEmptyAllowed()) {
						nonterminal = "synthetic/separatedList/" + elementSymbol + "/" + separatorSymbol;
						repetitionNonterminal = nonterminal + "/nonempty";
					} else {
						nonterminal = "synthetic/separatedList/" + elementSymbol + "/" + separatorSymbol + "/nonempty";
						repetitionNonterminal = nonterminal;
					}
				} else {
					nonterminal = "synthetic/list/" + elementSymbol + (repetition.isEmptyAllowed() ? "" : "/nonempty");
					repetitionNonterminal = nonterminal;
				}
				mergedSyntheticNonterminals.put(expression.withName(null), nonterminal);
			} else {
				syntheticNonterminalNameGenerator.save();
				syntheticNonterminalNameGenerator.extend(expression.getName());
				nonterminal = syntheticNonterminalNameGenerator.createSyntheticName("list");
				repetitionNonterminal = wrapInOptional ? syntheticNonterminalNameGenerator.createSyntheticName("nonemptyList") : nonterminal;
				syntheticNonterminalNameGenerator.restore();
			}

			// Convert the element. The expression name we assign here is the desired name in case of a non-merged
			// nonterminal. In case of merging, we have a single symbol so the expression name is ignored anyway.
			String elementSymbol = convertExpressionToSymbol(repetition.getElementExpression().withName(expression.getName()));
			Expression replacementElement = new SymbolReference(elementSymbol).withName("element");

			// Convert the separator, if any. Again, we are not concerned with the expression name in case of merging.
			String separatorSymbol;
			Expression replacementSeparator;
			if (hasSeparator) {
				syntheticNonterminalNameGenerator.save();
				syntheticNonterminalNameGenerator.extend(expression.getName());
				separatorSymbol = convertExpressionToSymbol(repetition.getSeparatorExpression().withName("separator"));
				syntheticNonterminalNameGenerator.restore();
				replacementSeparator = new SymbolReference(separatorSymbol).withName("separator");
			} else {
				separatorSymbol = null;
				replacementSeparator = null;
			}

			// build the alternatives (base case and repetition case) for the actual repetition
			List<name.martingeisse.mapag.grammar.extended.Alternative> repetitionAlternatives = new ArrayList<>();
			{
				boolean repetitionHasEmptyBaseCase = repetition.isEmptyAllowed() && !hasSeparator;
				repetitionAlternatives.add(syntheticAlternative("start", repetitionHasEmptyBaseCase ? new EmptyExpression() : replacementElement));
				Expression repetitionDelta = hasSeparator ? new SequenceExpression(replacementSeparator, replacementElement) : replacementElement;
				repetitionAlternatives.add(syntheticAlternative("next",
					new SequenceExpression(new SymbolReference(repetitionNonterminal).withName("previous"), repetitionDelta))
				);
			}

			// Now, if we have an emptyable separated repetition, we must wrap that (now nonempty) repetition in an
			// optional, otherwise we use it as-is. The toplevel PSI style is always a repetition; if the toplevel
			// node is an optional-wrapper, the wrapped node has transparent PSI style.
			PsiStyle psiStyle = new PsiStyle.Repetition(elementSymbol, separatorSymbol);
			if (wrapInOptional) {
				createSyntheticNonterminal(repetitionNonterminal, repetitionAlternatives, psiStyle);
				List<Alternative> wrapperAlternatives = new ArrayList<>();
				wrapperAlternatives.add(syntheticAlternative("empty", new EmptyExpression()));
				wrapperAlternatives.add(syntheticAlternative("nonempty", new SymbolReference(repetitionNonterminal).withName("nonempty")));
				createSyntheticNonterminal(nonterminal, wrapperAlternatives, psiStyle);
			} else {
				createSyntheticNonterminal(nonterminal, repetitionAlternatives, psiStyle);
			}
			return nonterminal;

		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}

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

	//
	// Helper methods to create synthetic nonterminals
	//

	private name.martingeisse.mapag.grammar.extended.Alternative syntheticAlternative(String name, Expression expression) {
		return new name.martingeisse.mapag.grammar.extended.Alternative(name, expression, null, null, false, false);
	}

	private void createSyntheticNonterminal(String nonterminal, Collection<Alternative> alternatives, PsiStyle psiStyle) {
		if (nonterminalPsiStyles.get(nonterminal) != null) {
			throw new RuntimeException("nonterminal name collision: " + nonterminal);
		}
		Production production = new Production(nonterminal, ImmutableList.copyOf(alternatives));
		pendingProductions.add(production);
		nonterminalPsiStyles.put(nonterminal, psiStyle);
	}

}
