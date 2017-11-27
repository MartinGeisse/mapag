package name.martingeisse.mapag.input;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.psi.*;
import name.martingeisse.mapag.util.UserMessageException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Converts the PSI to an extended grammar.
 */
public class PsiToGrammarConverter {

	public Grammar convert(MapagSourceFile mapagSourceFile) {
		name.martingeisse.mapag.input.psi.Grammar psiGrammar = mapagSourceFile.getGrammar();
		if (psiGrammar == null) {
			throw new RuntimeException("could not find grammar PSI node");
		}
		return convert(psiGrammar);
	}

	public Grammar convert(name.martingeisse.mapag.input.psi.Grammar psiGrammar) {

		ImmutableList<TerminalDeclaration> terminalDeclarations =
			convertIdentifiers(psiGrammar.getTerminals(), TerminalDeclaration::new);

		PrecedenceTable precedenceTable =
			convertPrecedenceTable(psiGrammar.getPrecedenceTable());

		String startSymbol = getText(psiGrammar.getStartSymbolName());

		ImmutableList<Production> productions = convertProductions(psiGrammar.getProductions());

		return new Grammar(terminalDeclarations, precedenceTable, startSymbol, productions);
	}

	private PrecedenceTable convertPrecedenceTable(Grammar_PrecedenceTable psiPrecedenceTable) {
		if (psiPrecedenceTable.getIt() == null) {
			return null;
		}
		List<PrecedenceTable.Entry> convertedEntries = new ArrayList<>();
		for (PrecedenceDeclaration precedenceDeclaration : psiPrecedenceTable.getIt().getPrecedenceDeclarations().getAll()) {
			ImmutableList<String> identifiers = convertIdentifiers(precedenceDeclaration.getTerminals(), s -> s);
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
			convertedEntries.add(new PrecedenceTable.Entry(ImmutableList.copyOf(identifiers), associativity));
		}
		return new PrecedenceTable(ImmutableList.copyOf(convertedEntries));
	}

	private ImmutableList<Production> convertProductions(Grammar_Productions psiProductions) {
		List<Production> productions = new ArrayList<>();
		for (name.martingeisse.mapag.input.psi.Production psiProduction : psiProductions.getAll()) {
			if (psiProduction instanceof Production_SingleUnnamed) {

				Production_SingleUnnamed typed = (Production_SingleUnnamed) psiProduction;
				String nonterminal = getText(typed.getNonterminalName());
				Alternative alternative = convertAlternative(null, typed.getRightHandSide());
				productions.add(new Production(nonterminal, ImmutableList.of(alternative)));

			} else if (psiProduction instanceof Production_SingleNamed) {

				Production_SingleNamed typed = (Production_SingleNamed) psiProduction;
				String nonterminal = getText(typed.getNonterminalName());
				String alternativeName = getText(typed.getAlternativeName());
				Alternative alternative = convertAlternative(alternativeName, typed.getRightHandSide());
				productions.add(new Production(nonterminal, ImmutableList.of(alternative)));

			} else if (psiProduction instanceof Production_Multi) {

				Production_Multi typed = (Production_Multi) psiProduction;
				String nonterminal = getText(typed.getNonterminalName());
				List<Alternative> alternatives = new ArrayList<>();
				for (Production_Multi_1 element : typed.getAlternatives().getAll()) {
					if (element instanceof Production_Multi_1_Unnamed) {

						Production_Multi_1_Unnamed typedElement = (Production_Multi_1_Unnamed) element;
						alternatives.add(convertAlternative(null, typedElement.getUnnamed().getRightHandSide()));

					} else if (element instanceof Production_Multi_1_Named) {

						Production_Multi_1_Named typedElement = (Production_Multi_1_Named) element;
						alternatives.add(convertAlternative(
							getText(typedElement.getNamed().getAlternativeName()),
							typedElement.getNamed().getRightHandSide()
						));

					} else {
						throw new RuntimeException("unknown multi-alternative production element node: " + element);
					}
				}
				productions.add(new Production(nonterminal, ImmutableList.copyOf(alternatives)));

			} else if (psiProduction instanceof Production_Error) {
				throw new UserMessageException("grammar contains errors");
			} else {
				throw new RuntimeException("unknown production PSI node: " + psiProduction);
			}
		}
		return ImmutableList.copyOf(productions);
	}

	private Alternative convertAlternative(String alternativeName, RightHandSide rightHandSide) {
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
		return new Alternative(alternativeName, expression, precedenceDefiningTerminal, resolveBlock, reduceOnError, reduceOnEofOnly);
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
			terminals.add(getText(psiResolveDeclaration.getFirstSymbol()));
			for (ResolveDeclaration_1 elementNode : psiResolveDeclaration.getAdditionalSymbols().getAll()) {
				terminals.add(getText(elementNode.getSymbol()));
			}

			resolveDeclarations.add(new ResolveDeclaration(conflictResolution, ImmutableList.copyOf(terminals)));
		}
		return new ResolveBlock(ImmutableList.copyOf(resolveDeclarations));
	}

	private Expression convertExpression(name.martingeisse.mapag.input.psi.Expression psiExpression) {
		if (psiExpression instanceof Expression_Named) {

			Expression_Named typedExpression = (Expression_Named) psiExpression;
			String name = getText(typedExpression.getExpressionName());
			return convertExpression(typedExpression.getExpression()).withName(name);

		} else if (psiExpression instanceof Expression_OneOrMore) {

			Expression_OneOrMore typedExpression = (Expression_OneOrMore) psiExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			return new OneOrMoreExpression(operand);

		} else if (psiExpression instanceof Expression_Sequence) {

			Expression_Sequence typedExpression = (Expression_Sequence) psiExpression;
			Expression left = convertExpression(typedExpression.getLeft());
			Expression right = convertExpression(typedExpression.getRight());
			return new SequenceExpression(left, right);

		} else if (psiExpression instanceof Expression_Identifier) {

			Expression_Identifier typedExpression = (Expression_Identifier) psiExpression;
			return new SymbolReference(getText(typedExpression.getIdentifier()));

		} else if (psiExpression instanceof Expression_Error) {

			return new SymbolReference(SpecialSymbols.ERROR_SYMBOL_NAME);

		} else if (psiExpression instanceof Expression_Parenthesized) {

			Expression_Parenthesized typedExpression = (Expression_Parenthesized) psiExpression;
			return convertExpression(typedExpression.getInner());

		} else if (psiExpression instanceof Expression_ZeroOrMore) {

			Expression_ZeroOrMore typedExpression = (Expression_ZeroOrMore) psiExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			return new ZeroOrMoreExpression(operand);

		} else if (psiExpression instanceof Expression_Or) {

			Expression_Or typedExpression = (Expression_Or) psiExpression;
			Expression left = convertExpression(typedExpression.getLeft());
			Expression right = convertExpression(typedExpression.getRight());
			return new OrExpression(left, right);

		} else if (psiExpression instanceof Expression_Optional) {

			Expression_Optional typedExpression = (Expression_Optional) psiExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			return new OptionalExpression(operand);

		} else {
			throw new RuntimeException("unknown expression PSI node: " + psiExpression);
		}
	}

	//
	//
	//

	private <T> ImmutableList<T> convertIdentifiers(NonemptyIdentifierList identifiers, Function<String, T> elementFactory) {
		List<T> result = new ArrayList<>();
		result.add(elementFactory.apply(getText(identifiers.getFirstIdentifier())));
		for (NonemptyIdentifierList_1 node : identifiers.getMoreIdentifiers().getAll()) {
			result.add(elementFactory.apply(getText(node.getIdentifier())));
		}
		return ImmutableList.copyOf(result);
	}

	// prevents calling .getText() on non-leaf PSI nodes by accident
	private static String getText(LeafPsiElement element) {
		return element.getText();
	}

}
