package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import name.martingeisse.mapag.input.Symbols;

public class PsiFactory {

	public static PsiElement createPsiElement(ASTNode node) {
		IElementType type = node.getElementType();

		if (type == Symbols.rightHandSide) {
			return new RightHandSide(node);
		}
		if (type == Symbols.expression_Empty) {
			return new Expression_Empty(node);
		}
		if (type == Symbols.expression_Identifier) {
			return new Expression_Identifier(node);
		}
		if (type == Symbols.expression_Error) {
			return new Expression_Error(node);
		}
		if (type == Symbols.expression_Sequence) {
			return new Expression_Sequence(node);
		}
		if (type == Symbols.expression_Or) {
			return new Expression_Or(node);
		}
		if (type == Symbols.expression_ZeroOrMore) {
			return new Expression_ZeroOrMore(node);
		}
		if (type == Symbols.expression_OneOrMore) {
			return new Expression_OneOrMore(node);
		}
		if (type == Symbols.expression_SeparatedZeroOrMore) {
			return new Expression_SeparatedZeroOrMore(node);
		}
		if (type == Symbols.expression_SeparatedOneOrMore) {
			return new Expression_SeparatedOneOrMore(node);
		}
		if (type == Symbols.expression_Optional) {
			return new Expression_Optional(node);
		}
		if (type == Symbols.expression_Parenthesized) {
			return new Expression_Parenthesized(node);
		}
		if (type == Symbols.expression_Named) {
			return new Expression_Named(node);
		}
		if (type == Symbols.production_SingleUnnamed) {
			return new Production_SingleUnnamed(node);
		}
		if (type == Symbols.production_SingleNamed) {
			return new Production_SingleNamed(node);
		}
		if (type == Symbols.production_Multi) {
			return new Production_Multi(node);
		}
		if (type == Symbols.production_ErrorWithoutNonterminalNameWithSemicolon) {
			return new Production_ErrorWithoutNonterminalNameWithSemicolon(node);
		}
		if (type == Symbols.production_ErrorWithoutNonterminalNameWithClosingCurlyBrace) {
			return new Production_ErrorWithoutNonterminalNameWithClosingCurlyBrace(node);
		}
		if (type == Symbols.production_ErrorWithoutNonterminalNameAtEof) {
			return new Production_ErrorWithoutNonterminalNameAtEof(node);
		}
		if (type == Symbols.production_ErrorWithNonterminalNameWithSemicolon) {
			return new Production_ErrorWithNonterminalNameWithSemicolon(node);
		}
		if (type == Symbols.production_ErrorWithNonterminalNameWithClosingCurlyBrace) {
			return new Production_ErrorWithNonterminalNameWithClosingCurlyBrace(node);
		}
		if (type == Symbols.production_ErrorWithNonterminalNameAtEof) {
			return new Production_ErrorWithNonterminalNameAtEof(node);
		}
		if (type == Symbols.synthetic_SeparatedList_TerminalDeclaration_COMMA_Nonempty) {
			return new ListNode<TerminalDeclaration>(node, createTokenSet(Symbols.terminalDeclaration), TerminalDeclaration.class);
		}
		if (type == Symbols.production_Multi_Alternatives_Unnamed) {
			return new Production_Multi_Alternatives_Unnamed(node);
		}
		if (type == Symbols.production_Multi_Alternatives_Named) {
			return new Production_Multi_Alternatives_Named(node);
		}
		if (type == Symbols.grammar_TerminalDeclarations) {
			return new Grammar_TerminalDeclarations(node);
		}
		if (type == Symbols.synthetic_List_PrecedenceDeclaration) {
			return new ListNode<PrecedenceDeclaration>(node, createTokenSet(Symbols.precedenceDeclaration_Normal, Symbols.precedenceDeclaration_ErrorWithSemicolon, Symbols.precedenceDeclaration_ErrorWithoutSemicolon), PrecedenceDeclaration.class);
		}
		if (type == Symbols.grammar_PrecedenceTable_Optional) {
			return new Optional<Grammar_PrecedenceTable>(node);
		}
		if (type == Symbols.synthetic_List_AlternativeAttribute) {
			return new ListNode<AlternativeAttribute>(node, createTokenSet(Symbols.alternativeAttribute_Precedence, Symbols.alternativeAttribute_ResolveBlock, Symbols.alternativeAttribute_ReduceOnError, Symbols.alternativeAttribute_Eof), AlternativeAttribute.class);
		}
		if (type == Symbols.precedenceDeclaration_Normal) {
			return new PrecedenceDeclaration_Normal(node);
		}
		if (type == Symbols.precedenceDeclaration_ErrorWithSemicolon) {
			return new PrecedenceDeclaration_ErrorWithSemicolon(node);
		}
		if (type == Symbols.precedenceDeclaration_ErrorWithoutSemicolon) {
			return new PrecedenceDeclaration_ErrorWithoutSemicolon(node);
		}
		if (type == Symbols.synthetic_SeparatedList_PrecedenceDeclarationSymbol_COMMA_Nonempty) {
			return new ListNode<PrecedenceDeclarationSymbol>(node, createTokenSet(Symbols.precedenceDeclarationSymbol), PrecedenceDeclarationSymbol.class);
		}
		if (type == Symbols.production_Multi_Alternatives_List) {
			return new ListNode<Production_Multi_Alternatives>(node, createTokenSet(Symbols.production_Multi_Alternatives_Unnamed, Symbols.production_Multi_Alternatives_Named), Production_Multi_Alternatives.class);
		}
		if (type == Symbols.grammar) {
			return new Grammar(node);
		}
		if (type == Symbols.terminalDeclaration) {
			return new TerminalDeclaration(node);
		}
		if (type == Symbols.precedenceDeclarationSymbol) {
			return new PrecedenceDeclarationSymbol(node);
		}
		if (type == Symbols.synthetic_List_Production_Nonempty) {
			return new ListNode<Production>(node, createTokenSet(Symbols.production_SingleUnnamed, Symbols.production_SingleNamed, Symbols.production_Multi, Symbols.production_ErrorWithoutNonterminalNameWithSemicolon, Symbols.production_ErrorWithoutNonterminalNameWithClosingCurlyBrace, Symbols.production_ErrorWithoutNonterminalNameAtEof, Symbols.production_ErrorWithNonterminalNameWithSemicolon, Symbols.production_ErrorWithNonterminalNameWithClosingCurlyBrace, Symbols.production_ErrorWithNonterminalNameAtEof), Production.class);
		}
		if (type == Symbols.resolveDeclaration_Action_Shift) {
			return new ResolveDeclaration_Action_Shift(node);
		}
		if (type == Symbols.resolveDeclaration_Action_Reduce) {
			return new ResolveDeclaration_Action_Reduce(node);
		}
		if (type == Symbols.synthetic_SeparatedList_IDENTIFIER_COMMA_Nonempty) {
			return new ListNode<LeafPsiElement>(node, createTokenSet(Symbols.IDENTIFIER), LeafPsiElement.class);
		}
		if (type == Symbols.resolveDeclaration) {
			return new ResolveDeclaration(node);
		}
		if (type == Symbols.grammar_PrecedenceTable) {
			return new Grammar_PrecedenceTable(node);
		}
		if (type == Symbols.synthetic_List_ResolveDeclaration) {
			return new ListNode<ResolveDeclaration>(node, createTokenSet(Symbols.resolveDeclaration), ResolveDeclaration.class);
		}
		if (type == Symbols.alternativeAttribute_Precedence) {
			return new AlternativeAttribute_Precedence(node);
		}
		if (type == Symbols.alternativeAttribute_ResolveBlock) {
			return new AlternativeAttribute_ResolveBlock(node);
		}
		if (type == Symbols.alternativeAttribute_ReduceOnError) {
			return new AlternativeAttribute_ReduceOnError(node);
		}
		if (type == Symbols.alternativeAttribute_Eof) {
			return new AlternativeAttribute_Eof(node);
		}
		if (type == Symbols.precedenceDeclaration_Normal_Associativity_Left) {
			return new PrecedenceDeclaration_Normal_Associativity_Left(node);
		}
		if (type == Symbols.precedenceDeclaration_Normal_Associativity_Right) {
			return new PrecedenceDeclaration_Normal_Associativity_Right(node);
		}
		if (type == Symbols.precedenceDeclaration_Normal_Associativity_Nonassoc) {
			return new PrecedenceDeclaration_Normal_Associativity_Nonassoc(node);
		}
		if (type == Symbols.__PARSED_FRAGMENT) {
			return new ASTWrapperPsiElement(node);
		}

		throw new RuntimeException("cannot create PSI element for AST node due to unknown element type: " + type);
	}

	private static TokenSet createTokenSet(IElementType... types) {
		return TokenSet.create(types);
	}

}