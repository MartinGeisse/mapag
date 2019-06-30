package name.martingeisse.mapag.input;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.ConflictResolution;
import name.martingeisse.mapag.grammar.SpecialSymbols;
import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.grammar.extended.expression.*;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.cm.*;
import name.martingeisse.mapag.util.UserMessageException;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts the CM to an extended grammar.
 */
public class CmToGrammarConverter {

	private final boolean failOnErrors;
	private final GrammarToCmMap backMap = new GrammarToCmMap();

	public CmToGrammarConverter(boolean failOnErrors) {
		this.failOnErrors = failOnErrors;
	}

	public Grammar convert(MapagSourceFile mapagSourceFile) {
		name.martingeisse.mapag.input.cm.Grammar cmGrammar = mapagSourceFile.getGrammar();
		if (cmGrammar == null) {
			throw new RuntimeException("could not find grammar CM node");
		}
		return convert(cmGrammar);
	}

	public GrammarToCmMap getBackMap() {
		return backMap;
	}

	public Grammar convert(name.martingeisse.mapag.input.cm.Grammar cmGrammar) {

		ImmutableList<TerminalDeclaration> terminalDeclarations =
			convertTerminalDeclarations(cmGrammar.getTerminalDeclarations());

		PrecedenceTable precedenceTable =
			convertPrecedenceTable(cmGrammar.getPrecedenceTable());

		String startSymbol = cmGrammar.getStartSymbolName().getText();
		backMap.startSymbol = cmGrammar.getStartSymbolName();

		ImmutableList<Production> productions = convertProductions(cmGrammar.getProductions());

		return new Grammar(terminalDeclarations, precedenceTable, startSymbol, productions);
	}

	private ImmutableList<TerminalDeclaration> convertTerminalDeclarations(Grammar_TerminalDeclarations terminalDeclarations) {
		List<TerminalDeclaration> result = new ArrayList<>();
		for (name.martingeisse.mapag.input.cm.TerminalDeclaration node : terminalDeclarations.getIdentifiers().getAll()) {
			String text = node.getIdentifier().getText();
			TerminalDeclaration terminalDeclaration = new TerminalDeclaration(text);
			result.add(terminalDeclaration);
			backMap.terminalDeclarations.put(terminalDeclaration, node);
		}
		return ImmutableList.copyOf(result);
	}

	private PrecedenceTable convertPrecedenceTable(CmOptional<Grammar_PrecedenceTable> cmPrecedenceTable) {
		if (cmPrecedenceTable.getIt() == null) {
			return null;
		}
		List<PrecedenceTable.Entry> convertedEntries = new ArrayList<>();
		for (PrecedenceDeclaration precedenceDeclaration : cmPrecedenceTable.getIt().getPrecedenceDeclarations().getAll()) {
			if (precedenceDeclaration instanceof PrecedenceDeclaration_Normal) {
				PrecedenceDeclaration_Normal typedPrecedenceDeclaration = (PrecedenceDeclaration_Normal) precedenceDeclaration;
				ImmutableList<String> identifiers = convertPrecedenceTableSymbols(typedPrecedenceDeclaration.getTerminals());
				PrecedenceDeclaration_Normal_Associativity associativityNode = typedPrecedenceDeclaration.getAssociativity();
				Associativity associativity;
				if (associativityNode instanceof PrecedenceDeclaration_Normal_Associativity_Left) {
					associativity = Associativity.LEFT;
				} else if (associativityNode instanceof PrecedenceDeclaration_Normal_Associativity_Right) {
					associativity = Associativity.RIGHT;
				} else if (associativityNode instanceof PrecedenceDeclaration_Normal_Associativity_Nonassoc) {
					associativity = Associativity.NONASSOC;
				} else {
					if (failOnErrors) {
						throw new RuntimeException("unknown associativity CM node: " + associativityNode);
					} else {
						continue;
					}
				}
				PrecedenceTable.Entry entry = new PrecedenceTable.Entry(ImmutableList.copyOf(identifiers), associativity);
				convertedEntries.add(entry);
				backMap.precedenceTableEntries.put(entry, precedenceDeclaration);
			} else {
				if (failOnErrors) {
					throw new UserMessageException("grammar contains errors");
				}
			}
		}
		return new PrecedenceTable(ImmutableList.copyOf(convertedEntries));
	}

	private ImmutableList<String> convertPrecedenceTableSymbols(CmList<PrecedenceDeclarationSymbol> identifiers) {
		List<String> result = new ArrayList<>();
		for (PrecedenceDeclarationSymbol node : identifiers.getAll()) {
			result.add(node.getIdentifier().getText());
		}
		return ImmutableList.copyOf(result);
	}

	private ImmutableList<Production> convertProductions(CmList<name.martingeisse.mapag.input.cm.Production> cmProductions) {
		List<Production> productions = new ArrayList<>();
		for (name.martingeisse.mapag.input.cm.Production cmProduction : cmProductions.getAll()) {
			Production convertedProduction;
			if (cmProduction instanceof Production_SingleUnnamed) {

				Production_SingleUnnamed typed = (Production_SingleUnnamed) cmProduction;
				String nonterminal = typed.getNonterminalName().getText();
				Alternative alternative = convertAlternative(typed.getRightHandSide(), null, typed.getRightHandSide());
				convertedProduction = new Production(nonterminal, ImmutableList.of(alternative));

			} else if (cmProduction instanceof Production_SingleNamed) {

				Production_SingleNamed typed = (Production_SingleNamed) cmProduction;
				String nonterminal = typed.getNonterminalName().getText();
				String alternativeName = typed.getAlternativeName().getText();
				Alternative alternative = convertAlternative(typed.getRightHandSide(), alternativeName, typed.getRightHandSide());
				convertedProduction = new Production(nonterminal, ImmutableList.of(alternative));

			} else if (cmProduction instanceof Production_Multi) {

				Production_Multi typed = (Production_Multi) cmProduction;
				String nonterminal = typed.getNonterminalName().getText();
				List<Alternative> alternatives = new ArrayList<>();
				for (Production_Multi_Alternatives element : typed.getAlternatives().getAll()) {
					if (element instanceof Production_Multi_Alternatives_Unnamed) {

						Production_Multi_Alternatives_Unnamed typedElement = (Production_Multi_Alternatives_Unnamed) element;
						alternatives.add(convertAlternative(
							typedElement,
							null,
							typedElement.getRightHandSide()));

					} else if (element instanceof Production_Multi_Alternatives_Named) {

						Production_Multi_Alternatives_Named typedElement = (Production_Multi_Alternatives_Named) element;
						alternatives.add(convertAlternative(
							typedElement,
							typedElement.getAlternativeName().getText(),
							typedElement.getRightHandSide()
						));

					} else {
						if (failOnErrors) {
							throw new RuntimeException("unknown multi-alternative production element node: " + element);
						}
					}
				}
				convertedProduction = new Production(nonterminal, ImmutableList.copyOf(alternatives));

			} else if (cmProduction instanceof Production_ErrorWithNonterminalNameWithSemicolon
				|| cmProduction instanceof Production_ErrorWithNonterminalNameWithClosingCurlyBrace
				|| cmProduction instanceof Production_ErrorWithNonterminalNameAtEof
				|| cmProduction instanceof Production_ErrorWithoutNonterminalNameWithSemicolon
				|| cmProduction instanceof Production_ErrorWithoutNonterminalNameWithClosingCurlyBrace
				|| cmProduction instanceof Production_ErrorWithoutNonterminalNameAtEof) {

				if (failOnErrors) {
					throw new UserMessageException("grammar contains errors");
				} else {
					continue;
				}

			} else {
				if (failOnErrors) {
					throw new RuntimeException("unknown production CM node: " + cmProduction);
				} else {
					continue;
				}
			}
			productions.add(convertedProduction);
			backMap.productions.put(convertedProduction, cmProduction);
		}
		return ImmutableList.copyOf(productions);
	}

	private Alternative convertAlternative(CmNode cmNode, String alternativeName, RightHandSide rightHandSide) {
		Expression expression = convertExpression(rightHandSide.getExpression());
		String precedenceDefiningTerminal = null;
		ResolveBlock resolveBlock = null;
		boolean reduceOnError = false;
		boolean reduceOnEofOnly = false;
		for (AlternativeAttribute attribute : rightHandSide.getAttributes().getAll()) {
			if (attribute instanceof AlternativeAttribute_Precedence) {

				AlternativeAttribute_Precedence precedenceAttribute = (AlternativeAttribute_Precedence) attribute;
				precedenceDefiningTerminal = precedenceAttribute.getPrecedenceDefiningTerminal().getText();

			} else if (attribute instanceof AlternativeAttribute_ResolveBlock) {

				AlternativeAttribute_ResolveBlock resolveBlockAttribute = (AlternativeAttribute_ResolveBlock) attribute;
				resolveBlock = convertResolveBlock(resolveBlockAttribute.getResolveDeclarations().getAll());

			} else if (attribute instanceof AlternativeAttribute_ReduceOnError) {

				reduceOnError = true;

			} else if (attribute instanceof AlternativeAttribute_Eof) {

				reduceOnEofOnly = true;

			} else {
				if (failOnErrors) {
					throw new RuntimeException("unknown right-hand side attribute CM node: " + attribute);
				}
			}
		}
		Alternative convertedAlternative = new Alternative(alternativeName, expression, precedenceDefiningTerminal, resolveBlock, reduceOnError, reduceOnEofOnly);
		backMap.alternatives.put(convertedAlternative, cmNode);
		backMap.rightHandSides.put(convertedAlternative, rightHandSide);
		return convertedAlternative;
	}

	private ResolveBlock convertResolveBlock(List<name.martingeisse.mapag.input.cm.ResolveDeclaration> cmResolveDeclarations) {
		List<ResolveDeclaration> resolveDeclarations = new ArrayList<>();
		for (name.martingeisse.mapag.input.cm.ResolveDeclaration cmResolveDeclaration : cmResolveDeclarations) {

			ResolveDeclaration_Action action = cmResolveDeclaration.getAction();
			ConflictResolution conflictResolution;
			if (action instanceof ResolveDeclaration_Action_Shift) {
				conflictResolution = ConflictResolution.SHIFT;
			} else if (action instanceof ResolveDeclaration_Action_Reduce) {
				conflictResolution = ConflictResolution.REDUCE;
			} else {
				if (failOnErrors) {
					throw new RuntimeException("unknown resolve declaration action node: " + action);
				} else {
					continue;
				}
			}

			List<String> terminals = new ArrayList<>();
			for (CmToken elementNode : cmResolveDeclaration.getSymbols().getAll()) {
				terminals.add(elementNode.getText());
			}

			ResolveDeclaration resolveDeclaration = new ResolveDeclaration(conflictResolution, ImmutableList.copyOf(terminals));
			resolveDeclarations.add(resolveDeclaration);
			backMap.resolveDeclarations.put(resolveDeclaration, cmResolveDeclaration);
		}
		return new ResolveBlock(ImmutableList.copyOf(resolveDeclarations));
	}

	private Expression convertExpression(name.martingeisse.mapag.input.cm.Expression cmExpression) {
		Expression result;
		if (cmExpression instanceof Expression_Named) {

			Expression_Named typedExpression = (Expression_Named) cmExpression;
			String name = typedExpression.getExpressionName().getText();
			result = convertExpression(typedExpression.getExpression()).withName(name);

		} else if (cmExpression instanceof Expression_OneOrMore) {

			Expression_OneOrMore typedExpression = (Expression_OneOrMore) cmExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			result = new Repetition(operand, null, false);

		} else if (cmExpression instanceof Expression_SeparatedOneOrMore) {

			Expression_SeparatedOneOrMore typedExpression = (Expression_SeparatedOneOrMore) cmExpression;
			Expression elementExpression = convertExpression(typedExpression.getElement());
			Expression separatorExpression = convertExpression(typedExpression.getSeparator());
			result = new Repetition(elementExpression, separatorExpression, false);

		} else if (cmExpression instanceof Expression_Sequence) {

			Expression_Sequence typedExpression = (Expression_Sequence) cmExpression;
			Expression left = convertExpression(typedExpression.getLeft());
			Expression right = convertExpression(typedExpression.getRight());
			result = new SequenceExpression(left, right);

		} else if (cmExpression instanceof Expression_Empty) {

			result = new EmptyExpression();

		} else if (cmExpression instanceof Expression_Identifier) {

			Expression_Identifier typedExpression = (Expression_Identifier) cmExpression;
			result = new SymbolReference(typedExpression.getIdentifier().getText());

		} else if (cmExpression instanceof Expression_Error) {

			result = new SymbolReference(SpecialSymbols.ERROR_SYMBOL_NAME);

		} else if (cmExpression instanceof Expression_Parenthesized) {

			Expression_Parenthesized typedExpression = (Expression_Parenthesized) cmExpression;
			result = convertExpression(typedExpression.getInner());

		} else if (cmExpression instanceof Expression_ZeroOrMore) {

			Expression_ZeroOrMore typedExpression = (Expression_ZeroOrMore) cmExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			result = new Repetition(operand, null, true);

		} else if (cmExpression instanceof Expression_SeparatedZeroOrMore) {

			Expression_SeparatedZeroOrMore typedExpression = (Expression_SeparatedZeroOrMore) cmExpression;
			Expression elementExpression = convertExpression(typedExpression.getElement());
			Expression separatorExpression = convertExpression(typedExpression.getSeparator());
			result = new Repetition(elementExpression, separatorExpression, true);

		} else if (cmExpression instanceof Expression_Or) {

			Expression_Or typedExpression = (Expression_Or) cmExpression;
			Expression left = convertExpression(typedExpression.getLeft());
			Expression right = convertExpression(typedExpression.getRight());
			result = new OrExpression(left, right);

		} else if (cmExpression instanceof Expression_Optional) {

			Expression_Optional typedExpression = (Expression_Optional) cmExpression;
			Expression operand = convertExpression(typedExpression.getOperand());
			result = new OptionalExpression(operand);

		} else {
			if (failOnErrors) {
				throw new RuntimeException("unknown expression CM node: " + cmExpression);
			} else {
				result = new EmptyExpression();
			}
		}
		backMap.expressions.put(result, cmExpression);
		return result;
	}

}
