package name.martingeisse.mapag.grammar.canonicalization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.canonical.AlternativeAttributes;
import name.martingeisse.mapag.grammar.canonical.Expansion;
import name.martingeisse.mapag.grammar.canonical.ExpansionElement;
import name.martingeisse.mapag.grammar.canonical.PsiStyle;
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
	private final SyntheticNonterminalMergingStrategy syntheticNonterminalMergingStrategy;
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
		this.syntheticNonterminalMergingStrategy = new SyntheticNonterminalMergingStrategy();
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

		// Anything else generates a synthetic nonterminaland is potentially subject to merging. If we merge, we store
		// the name of the merged nonterminal here, then we go on building productions for it (those need not be stored
		// in the mergedSyntheticNonterminals; they work like all other productions). If we don't merge, we just build
		// the productions.
		String mergedNonterminal;
		{
			Expression anonymousExpression = expression.withName(null);
			String existingMergedName = mergedSyntheticNonterminals.get(anonymousExpression);
			if (existingMergedName != null) {
				return existingMergedName;
			}
			mergedNonterminal = syntheticNonterminalMergingStrategy.determineMergedName(anonymousExpression);
			if (mergedNonterminal != null) {
				mergedSyntheticNonterminals.put(anonymousExpression, mergedNonterminal);
			}
		}
		// TODO what if that ID is already used?

		// This name is either the merged or generated name, to be distinguished by whether mergedName is null.
		// It may be used for any of the nonterminals generated below, not necessarily the root nonterminal.
		// It should be used for one of them though, since the internal counter of the name generator has already
		// been increased.
		String suggestedNonterminalName;
		if (mergedNonterminal == null) {
			suggestedNonterminalName = syntheticNonterminalNameGenerator.createSyntheticName(expression.getName());
		} else {
			suggestedNonterminalName = mergedNonterminal;
		}

		// build the list of alternatives from the original expression and choose a PSI style
		String rootNonterminalName;
		List<name.martingeisse.mapag.grammar.extended.Alternative> rootNonterminalAlternatives = new ArrayList<>();
		PsiStyle rootNonterminalPsiStyle;
		if (expression instanceof EmptyExpression) {

			// strange case, but if the caller really needs a symbol for an EmptyExpression,
			// we must create a synthetic nonterminal with empty content
			rootNonterminalName = suggestedNonterminalName;
			rootNonterminalAlternatives.add(syntheticAlternative(null, expression));
			rootNonterminalPsiStyle = PsiStyle.Normal.INSTANCE;

		} else if (expression instanceof OrExpression) {

			// an OR-expression can be extracted into alternatives
			rootNonterminalName = suggestedNonterminalName;
			for (Expression orOperand : getOrOperands(expression)) {
				// TODO remove the name from the second argument? This may be the reason for unwanted nonterminals in the meta-grammar.
				// But it depends -- if the operand is a single symbol, we cannot remove the name, otherwise we
				// also remove its getter. For a sequence, removing the name is correct though.
				rootNonterminalAlternatives.add(syntheticAlternative(orOperand.getName(), orOperand));
			}
			rootNonterminalPsiStyle = PsiStyle.Normal.INSTANCE;

		} else if (expression instanceof SequenceExpression) {

			// A sequence gets extracted, but we must remove the expression's name so it gets inlined in the new
			// nonterminal -- otherwise we'd push it out to an infinite loop of new nonterminals.
			rootNonterminalName = suggestedNonterminalName;
			rootNonterminalAlternatives.add(syntheticAlternative(null, expression.withName(null)));
			rootNonterminalPsiStyle = PsiStyle.Normal.INSTANCE;

		} else if (expression instanceof OptionalExpression) {

			// An OptionalExpression gets extracted into a two-alternative nonterminal, but with special naming
			OptionalExpression optionalExpression = (OptionalExpression) expression;

			// determine the outer and inner name
			String operandName;
			if (mergedNonterminal == null) {
				operandName = null;
			} else {
				operandName = syntheticNonterminalMergingStrategy.determineOptionalOperandName(optionalExpression);
			}
			if (operandName == null) {
				rootNonterminalName = suggestedNonterminalName + "/optional";
				operandName = suggestedNonterminalName;
			} else {
				rootNonterminalName = suggestedNonterminalName;
			}

			// convert the operand
			String operandSymbol = convertExpressionToAbsoluteSymbol(optionalExpression.getOperand(), operandName);
			Expression replacementOperand = new SymbolReference(operandSymbol).withName("it");

			// convert the optional itself
			rootNonterminalAlternatives.add(syntheticAlternative("absent", new EmptyExpression()));
			rootNonterminalAlternatives.add(syntheticAlternative("present", replacementOperand));
			rootNonterminalPsiStyle = new PsiStyle.Optional(operandSymbol);

		} else if (expression instanceof Repetition) {

			Repetition repetition = (Repetition) expression;

			// choose an element name and list name (for the list as defined by the user)
			String elementName;
			if (mergedNonterminal == null) {
				elementName = null;
			} else {
				elementName = syntheticNonterminalMergingStrategy.determineListElementName(repetition);
			}
			if (elementName == null) {
				rootNonterminalName = suggestedNonterminalName + (repetition.isEmptyAllowed() ? "/list" : "/nonemptyList");
				elementName = suggestedNonterminalName;
			} else {
				rootNonterminalName = suggestedNonterminalName;
			}

			// choose a name for the corresponding nonempty list (note: if the original repetition is already nonempty,
			// this name isn't going to be used anyway)
			String nonemptyListName;
			if (mergedNonterminal == null) {
				nonemptyListName = null;
			} else {
				nonemptyListName = syntheticNonterminalMergingStrategy.determineNonemptyListName(repetition);
			}
			if (nonemptyListName == null) {
				nonemptyListName = suggestedNonterminalName + "/nonemptyList";
			}

			// choose a name for the separator
			String separatorName;
			if (mergedNonterminal == null) {
				separatorName = null;
			} else {
				separatorName = syntheticNonterminalMergingStrategy.determineListSeparatorName(repetition);
			}
			if (separatorName == null) {
				separatorName = suggestedNonterminalName + "/separator";
			}

			// convert the element
			String elementSymbol = convertExpressionToAbsoluteSymbol(repetition.getElementExpression(), elementName);
			Expression replacementElement = new SymbolReference(elementSymbol).withName("element");

			// convert the separator, if any
			String separatorSymbol;
			Expression replacementSeparator;
			if (repetition.getSeparatorExpression() == null) {
				separatorSymbol = null;
				replacementSeparator = null;
			} else {
				separatorSymbol = convertExpressionToAbsoluteSymbol(repetition.getSeparatorExpression(), separatorName);
				replacementSeparator = new SymbolReference(separatorSymbol).withName("separator");
			}

			// choose the name and PSI style for the raw repetition nonterminal. This is the above list name and a
			// repetition PSI style, except for the special case of an emptyable list with a separator -- the latter
			// gets wrapped in an extra optional, so that optional is "the list nonterminal" as defined by the user
			// and the raw repetition nonterminal is the wrapped one.
			boolean wrapInOptional = repetition.isEmptyAllowed() && repetition.getSeparatorExpression() != null;
			String repetitionNonterminal = wrapInOptional ? nonemptyListName : rootNonterminalName;

			// build the alternatives (base case and repetition case) for the repetition
			List<name.martingeisse.mapag.grammar.extended.Alternative> repetitionAlternatives = new ArrayList<>();
			boolean repetitionHasEmptyBaseCase = repetition.isEmptyAllowed() && replacementSeparator == null;
			repetitionAlternatives.add(syntheticAlternative("start", repetitionHasEmptyBaseCase ? new EmptyExpression() : replacementElement));
			Expression repetitionDelta = replacementSeparator == null ? replacementElement : new SequenceExpression(replacementSeparator, replacementElement);
			repetitionAlternatives.add(syntheticAlternative("next",
				new SequenceExpression(new SymbolReference(repetitionNonterminal).withName("previous"), repetitionDelta))
			);

			// Now, if we have a zero-or-more repetition with separator, we must wrap that (now nonempty) repetition
			// in an optional
			if (wrapInOptional) {

				// build a nonterminal for the wrapped nonempty list
				Production nonemptyListProduction = new Production(nonemptyListName, ImmutableList.copyOf(repetitionAlternatives));
				pendingProductions.add(nonemptyListProduction);
				nonterminalPsiStyles.put(rootNonterminalName, PsiStyle.Transparent.INSTANCE);

				// wrap it in an optional
				rootNonterminalAlternatives.add(syntheticAlternative("empty", new EmptyExpression()));
				rootNonterminalAlternatives.add(syntheticAlternative("nonempty", new SymbolReference(repetitionNonterminal).withName("nonempty")));

			} else {

				// use the repetition as the root nonterminal
				rootNonterminalAlternatives = repetitionAlternatives;

			}
			rootNonterminalPsiStyle = new PsiStyle.Repetition(elementSymbol, separatorSymbol);

		} else {
			throw new RuntimeException("unknown expression type: " + expression);
		}

		// finish building the root synthetic nonterminal
		Production production = new Production(rootNonterminalName, ImmutableList.copyOf(rootNonterminalAlternatives));
		pendingProductions.add(production);
		nonterminalPsiStyles.put(rootNonterminalName, rootNonterminalPsiStyle);

		return rootNonterminalName;
	}

	// Like convertExpressionToSymbol(), but tries to use the specified absolute symbol -- absolute in the sense
	// of "independent from the current expression context", that is, use that name as-is as the nonterminal name.
	// This can still fail if that name is already used, and in that case the syntheticNonterminalNameGenerator will
	// be used to generate another name. TODO this doesn't actually happen -- if there is a collision, wrong stuff happens!
	private String convertExpressionToAbsoluteSymbol(Expression expression, String name) {
		syntheticNonterminalNameGenerator.save();
		syntheticNonterminalNameGenerator.absolute();
		String resultingSymbol = convertExpressionToSymbol(expression.withName(name));
		syntheticNonterminalNameGenerator.restore();
		return resultingSymbol;
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

	private name.martingeisse.mapag.grammar.extended.Alternative syntheticAlternative(String name, Expression expression) {
		return new name.martingeisse.mapag.grammar.extended.Alternative(name, expression, null, null, false, false);
	}

}
