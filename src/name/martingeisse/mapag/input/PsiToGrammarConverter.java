package name.martingeisse.mapag.input;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.psi.*;
import name.martingeisse.mapag.util.UserMessageException;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts the PSI to an extended grammar.
 */
public class PsiToGrammarConverter {

	private final GrammarToPsiMap backMap = new GrammarToPsiMap();

	public Grammar convert(MapagSourceFile mapagSourceFile) {
		name.martingeisse.mapag.input.psi.Grammar psiGrammar = mapagSourceFile.getGrammar();
		if (psiGrammar == null) {
			throw new RuntimeException("could not find grammar PSI node");
		}
		return convert(psiGrammar);
	}

	public GrammarToPsiMap getBackMap() {
		return backMap;
	}

	public Grammar convert(name.martingeisse.mapag.input.psi.Grammar psiGrammar) {

		ImmutableList<TerminalDeclaration> terminalDeclarations =
			convertTerminalDeclarations(psiGrammar.getTerminalDeclarations());

		PrecedenceTable precedenceTable =
			convertPrecedenceTable(psiGrammar.getPrecedenceTable());

		String startSymbol = getText(psiGrammar.getStartSymbolName());
		backMap.startSymbol = psiGrammar.getStartSymbolName();

		ImmutableList<Production> productions = convertProductions(psiGrammar.getProductions());

		return new Grammar(terminalDeclarations, precedenceTable, startSymbol, productions);
	}

	private ImmutableList<TerminalDeclaration> convertTerminalDeclarations(Grammar_TerminalDeclarations terminalDeclarations) {
		List<TerminalDeclaration> result = new ArrayList<>();
		for (name.martingeisse.mapag.input.psi.TerminalDeclaration node : terminalDeclarations.getIdentifiers().getAll()) {
			String text = getText(node.getIdentifier());
			TerminalDeclaration terminalDeclaration = new TerminalDeclaration(text);
			result.add(terminalDeclaration);
			backMap.terminalDeclarations.put(terminalDeclaration, node);
		}
		return ImmutableList.copyOf(result);
	}

	private PrecedenceTable convertPrecedenceTable(Optional<Grammar_PrecedenceTable> psiPrecedenceTable) {
		if (psiPrecedenceTable.getIt() == null) {
			return null;
		}
		List<PrecedenceTable.Entry> convertedEntries = new ArrayList<>();
		for (PrecedenceDeclaration precedenceDeclaration : psiPrecedenceTable.getIt().getPrecedenceDeclarations().getAll()) {
			ImmutableList<String> identifiers = convertPrecedenceTableSymbols(precedenceDeclaration.getTerminals());
			PrecedenceDeclaration_Associativity associativityNode = precedenceDeclaration.getAssociativity();
			Associativity associativity;
			if (associativityNode instanceof PrecedenceDeclaration_Associativity_Left) {
				associativity = Associativity.LEFT;
			} else if (associativityNode instanceof PrecedenceDeclaration_Associativity_Right) {
				associativity = Associativity.RIGHT;
			} else if (associativityNode instanceof PrecedenceDeclaration_Associativity_Nonassoc) {
				associativity = Associativity.NONASSOC;
			} else {
				throw new RuntimeException("unknown associativity PSI node: " + associativityNode);
			}
			PrecedenceTable.Entry entry = new PrecedenceTable.Entry(ImmutableList.copyOf(identifiers), associativity);
			convertedEntries.add(entry);
			backMap.precedenceTableEntries.put(entry, precedenceDeclaration);
		}
		return new PrecedenceTable(ImmutableList.copyOf(convertedEntries));
	}

	private ImmutableList<String> convertPrecedenceTableSymbols(ListNode<PrecedenceDeclarationSymbol> identifiers) {
		List<String> result = new ArrayList<>();
		for (PrecedenceDeclarationSymbol node : identifiers.getAll()) {
			result.add(getText(node.getIdentifier()));
		}
		return ImmutableList.copyOf(result);
	}

	private ImmutableList<Production> convertProductions(ListNode<name.martingeisse.mapag.input.psi.Production> psiProductions) {
		List<Production> productions = new ArrayList<>();
		for (name.martingeisse.mapag.input.psi.Production psiProduction : psiProductions.getAll()) {
			Production convertedProduction;
			if (psiProduction instanceof Production_SingleUnnamed) {

				Production_SingleUnnamed typed = (Production_SingleUnnamed) psiProduction;
				String nonterminal = getText(typed.getNonterminalName());
				Alternative alternative = convertAlternative(typed.getRightHandSide(), null, typed.getRightHandSide());
				convertedProduction = new Production(nonterminal, ImmutableList.of(alternative));

			} else if (psiProduction instanceof Production_SingleNamed) {

				Production_SingleNamed typed = (Production_SingleNamed) psiProduction;
				String nonterminal = getText(typed.getNonterminalName());
				String alternativeName = getText(typed.getAlternativeName());
				Alternative alternative = convertAlternative(typed.getRightHandSide(), alternativeName, typed.getRightHandSide());
				convertedProduction = new Production(nonterminal, ImmutableList.of(alternative));

			} else if (psiProduction instanceof Production_Multi) {

				Production_Multi typed = (Production_Multi) psiProduction;
				String nonterminal = getText(typed.getNonterminalName());
				List<Alternative> alternatives = new ArrayList<>();
				for (Production_Multi_Alternatives element : typed.getAlternatives().getAll()) {
					if (element instanceof Production_Multi_Alternatives_Unnamed) {

						Production_Multi_Alternatives_Unnamed typedElement = (Production_Multi_Alternatives_Unnamed) element;
						alternatives.add(convertAlternative(
							typedElement,
							null,
							typedElement.getUnnamed().getRightHandSide()));

					} else if (element instanceof Production_Multi_Alternatives_Named) {

						Production_Multi_Alternatives_Named typedElement = (Production_Multi_Alternatives_Named) element;
						alternatives.add(convertAlternative(
							typedElement,
							getText(typedElement.getNamed().getAlternativeName()),
							typedElement.getNamed().getRightHandSide()
						));

					} else {
						throw new RuntimeException("unknown multi-alternative production element node: " + element);
					}
				}
				convertedProduction = new Production(nonterminal, ImmutableList.copyOf(alternatives));

			} else if (psiProduction instanceof Production_ErrorWithNonterminalNameWithSemicolon
				|| psiProduction instanceof Production_ErrorWithNonterminalNameWithClosingCurlyBrace
				|| psiProduction instanceof Production_ErrorWithNonterminalNameAtEof
				|| psiProduction instanceof Production_ErrorWithoutNonterminalNameWithSemicolon
				|| psiProduction instanceof Production_ErrorWithoutNonterminalNameWithClosingCurlyBrace
				|| psiProduction instanceof Production_ErrorWithoutNonterminalNameAtEof) {
				throw new UserMessageException("grammar contains errors");
			} else {
				throw new RuntimeException("unknown production PSI node: " + psiProduction);
			}
			productions.add(convertedProduction);
			backMap.productions.put(convertedProduction, psiProduction);
		}
		return ImmutableList.copyOf(productions);
	}

	private Alternative convertAlternative(PsiElement psiElement, String alternativeName, RightHandSide rightHandSide) {
		Expression expression = convertExpression(rightHandSide.getExpression());
		String precedenceDefiningTerminal = null;
		ResolveBlock resolveBlock = null;
		boolean reduceOnError = false;
		boolean reduceOnEofOnly = false;
		for (AlternativeAttribute attribute : rightHandSide.getAttributes().getAll()) {
			if (attribute instanceof AlternativeAttribute_Precedence) {

				AlternativeAttribute_Precedence precedenceAttribute = (AlternativeAttribute_Precedence) attribute;
				precedenceDefiningTerminal = getText(precedenceAttribute.getPrecedenceDefiningTerminal());

			} else if (attribute instanceof AlternativeAttribute_ResolveBlock) {

				AlternativeAttribute_ResolveBlock resolveBlockAttribute = (AlternativeAttribute_ResolveBlock) attribute;
				resolveBlock = convertResolveBlock(resolveBlockAttribute.getResolveDeclarations().getAll());

			} else if (attribute instanceof AlternativeAttribute_ReduceOnError) {

				reduceOnError = true;

			} else if (attribute instanceof AlternativeAttribute_Eof) {

				reduceOnEofOnly = true;

			} else {
				throw new RuntimeException("unknown right-hand side attribute PSI node: " + attribute);

			}
		}
		Alternative convertedAlternative = new Alternative(alternativeName, expression, precedenceDefiningTerminal, resolveBlock, reduceOnError, reduceOnEofOnly);
		backMap.alternatives.put(convertedAlternative, psiElement);
		backMap.rightHandSides.put(convertedAlternative, rightHandSide);
		return convertedAlternative;
	}

	private ResolveBlock convertResolveBlock(ImmutableList<name.martingeisse.mapag.input.psi.ResolveDeclaration> psiResolveDeclarations) {
		List<ResolveDeclaration> resolveDeclarations = new ArrayList<>();
		for (name.martingeisse.mapag.input.psi.ResolveDeclaration psiResolveDeclaration : psiResolveDeclarations) {

			ResolveDeclaration_Action action = psiResolveDeclaration.getAction();
			ConflictResolution conflictResolution;
			if (action instanceof ResolveDeclaration_Action_Shift) {
				conflictResolution = ConflictResolution.SHIFT;
			} else if (action instanceof ResolveDeclaration_Action_Reduce) {
				conflictResolution = ConflictResolution.REDUCE;
			} else {
				throw new RuntimeException("unknown resolve declaration action node: " + action);
			}

			List<String> terminals = new ArrayList<>();
			for (LeafPsiElement elementNode : psiResolveDeclaration.getSymbols().getAll()) {
				terminals.add(getText(elementNode));
			}

			ResolveDeclaration resolveDeclaration = new ResolveDeclaration(conflictResolution, ImmutableList.copyOf(terminals));
			resolveDeclarations.add(resolveDeclaration);
			backMap.resolveDeclarations.put(resolveDeclaration, psiResolveDeclaration);
		}
		return new ResolveBlock(ImmutableList.copyOf(resolveDeclarations));
	}

	private Expression convertExpression(name.martingeisse.mapag.input.psi.Expression psiExpression) {
		Expression result;
		if (psiExpression instanceof Expression_Named) {

			Expression_Named typedExpression = (Expression_Named) psiExpression;
			String name = getText(typedExpression.getExpressionName());
			result = convertExpression(typedExpression.getExpression()).withName(name);

		} else if (psiExpression instanceof Expression_OneOrMore) {

			Expression_OneOrMore typedExpression = (Expression_OneOrMore) psiExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			result = new Repetition(operand, null, false);

		} else if (psiExpression instanceof Expression_SeparatedOneOrMore) {

			Expression_SeparatedOneOrMore typedExpression = (Expression_SeparatedOneOrMore) psiExpression;
			Expression elementExpression = convertExpression(typedExpression.getElement());
			Expression separatorExpression = convertExpression(typedExpression.getSeparator());
			result = new Repetition(elementExpression, separatorExpression, false);

		} else if (psiExpression instanceof Expression_Sequence) {

			Expression_Sequence typedExpression = (Expression_Sequence) psiExpression;
			Expression left = convertExpression(typedExpression.getLeft());
			Expression right = convertExpression(typedExpression.getRight());
			result = new SequenceExpression(left, right);

		} else if (psiExpression instanceof Expression_Empty) {

			result = new EmptyExpression();

		} else if (psiExpression instanceof Expression_Identifier) {

			Expression_Identifier typedExpression = (Expression_Identifier) psiExpression;
			result = new SymbolReference(getText(typedExpression.getIdentifier()));

		} else if (psiExpression instanceof Expression_Error) {

			result = new SymbolReference(SpecialSymbols.ERROR_SYMBOL_NAME);

		} else if (psiExpression instanceof Expression_Parenthesized) {

			Expression_Parenthesized typedExpression = (Expression_Parenthesized) psiExpression;
			result = convertExpression(typedExpression.getInner());

		} else if (psiExpression instanceof Expression_ZeroOrMore) {

			Expression_ZeroOrMore typedExpression = (Expression_ZeroOrMore) psiExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			result = new Repetition(operand, null, true);

		} else if (psiExpression instanceof Expression_SeparatedZeroOrMore) {

			Expression_SeparatedZeroOrMore typedExpression = (Expression_SeparatedZeroOrMore) psiExpression;
			Expression elementExpression = convertExpression(typedExpression.getElement());
			Expression separatorExpression = convertExpression(typedExpression.getSeparator());
			result = new Repetition(elementExpression, separatorExpression, true);

		} else if (psiExpression instanceof Expression_Or) {

			Expression_Or typedExpression = (Expression_Or) psiExpression;
			Expression left = convertExpression(typedExpression.getLeft());
			Expression right = convertExpression(typedExpression.getRight());
			result = new OrExpression(left, right);

		} else if (psiExpression instanceof Expression_Optional) {

			Expression_Optional typedExpression = (Expression_Optional) psiExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			result = new OptionalExpression(operand);

		} else {
			throw new RuntimeException("unknown expression PSI node: " + psiExpression);
		}
		backMap.expressions.put(result, psiExpression);
		return result;
	}

	// prevents calling .getText() on non-leaf PSI nodes by accident
	private static String getText(LeafPsiElement element) {
		return element.getText();
	}

}
