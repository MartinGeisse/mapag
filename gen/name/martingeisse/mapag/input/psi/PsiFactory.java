package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import name.martingeisse.mapag.input.Symbols;

public class PsiFactory {

	public static PsiElement createPsiElement(ASTNode node) {
		IElementType type = node.getElementType();

		if (type == Symbols.production_Multi_1_Named_Named) {
			return new Production_Multi_1_Named_Named(node);
		}
		if (type == Symbols.rightHandSide_WithoutResolver) {
			return new RightHandSide_WithoutResolver(node);
		}
		if (type == Symbols.rightHandSide_WithPrecedenceResolver) {
			return new RightHandSide_WithPrecedenceResolver(node);
		}
		if (type == Symbols.rightHandSide_WithExplicitResolver) {
			return new RightHandSide_WithExplicitResolver(node);
		}
		if (type == Symbols.expression_Identifier) {
			return new Expression_Identifier(node);
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
		if (type == Symbols.expression_Optional) {
			return new Expression_Optional(node);
		}
		if (type == Symbols.expression_Parenthesized) {
			return new Expression_Parenthesized(node);
		}
		if (type == Symbols.expression_Named) {
			return new Expression_Named(node);
		}
		if (type == Symbols.resolveDeclaration_AdditionalSymbols_Start) {
			return new ResolveDeclaration_AdditionalSymbols_Start(node);
		}
		if (type == Symbols.resolveDeclaration_AdditionalSymbols_Next) {
			return new ResolveDeclaration_AdditionalSymbols_Next(node);
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
		if (type == Symbols.production_Error) {
			return new Production_Error(node);
		}
		if (type == Symbols.production_Multi_Alternatives_Start) {
			return new Production_Multi_Alternatives_Start(node);
		}
		if (type == Symbols.production_Multi_Alternatives_Next) {
			return new Production_Multi_Alternatives_Next(node);
		}
		if (type == Symbols.resolveDeclaration_1) {
			return new ResolveDeclaration_1(node);
		}
		if (type == Symbols.resolveDeclarationSymbol_Identifier) {
			return new ResolveDeclarationSymbol_Identifier(node);
		}
		if (type == Symbols.resolveDeclarationSymbol_Eof) {
			return new ResolveDeclarationSymbol_Eof(node);
		}
		if (type == Symbols.production_Multi_1_Unnamed) {
			return new Production_Multi_1_Unnamed(node);
		}
		if (type == Symbols.production_Multi_1_Named) {
			return new Production_Multi_1_Named(node);
		}
		if (type == Symbols.rightHandSide_WithExplicitResolver_ResolveDeclarations_Start) {
			return new RightHandSide_WithExplicitResolver_ResolveDeclarations_Start(node);
		}
		if (type == Symbols.rightHandSide_WithExplicitResolver_ResolveDeclarations_Next) {
			return new RightHandSide_WithExplicitResolver_ResolveDeclarations_Next(node);
		}
		if (type == Symbols.precedenceDeclaration) {
			return new PrecedenceDeclaration(node);
		}
		if (type == Symbols.grammar_1) {
			return new Grammar_1(node);
		}
		if (type == Symbols.grammar) {
			return new Grammar(node);
		}
		if (type == Symbols.nonemptyIdentifierList_1) {
			return new NonemptyIdentifierList_1(node);
		}
		if (type == Symbols.grammar_1_PrecedenceDeclarations_Start) {
			return new Grammar_1_PrecedenceDeclarations_Start(node);
		}
		if (type == Symbols.grammar_1_PrecedenceDeclarations_Next) {
			return new Grammar_1_PrecedenceDeclarations_Next(node);
		}
		if (type == Symbols.production_Multi_1_Unnamed_Unnamed) {
			return new Production_Multi_1_Unnamed_Unnamed(node);
		}
		if (type == Symbols.nonemptyIdentifierList) {
			return new NonemptyIdentifierList(node);
		}
		if (type == Symbols.precedenceDeclaration_Associativity_Left) {
			return new PrecedenceDeclaration_Associativity_Left(node);
		}
		if (type == Symbols.precedenceDeclaration_Associativity_Right) {
			return new PrecedenceDeclaration_Associativity_Right(node);
		}
		if (type == Symbols.precedenceDeclaration_Associativity_Nonassoc) {
			return new PrecedenceDeclaration_Associativity_Nonassoc(node);
		}
		if (type == Symbols.resolveDeclaration_Action_Shift) {
			return new ResolveDeclaration_Action_Shift(node);
		}
		if (type == Symbols.resolveDeclaration_Action_Reduce) {
			return new ResolveDeclaration_Action_Reduce(node);
		}
		if (type == Symbols.grammar_Productions_Start) {
			return new Grammar_Productions_Start(node);
		}
		if (type == Symbols.grammar_Productions_Next) {
			return new Grammar_Productions_Next(node);
		}
		if (type == Symbols.resolveDeclaration) {
			return new ResolveDeclaration(node);
		}
		if (type == Symbols.grammar_PrecedenceTable_Absent) {
			return new Grammar_PrecedenceTable_Absent(node);
		}
		if (type == Symbols.grammar_PrecedenceTable_Present) {
			return new Grammar_PrecedenceTable_Present(node);
		}
		if (type == Symbols.nonemptyIdentifierList_MoreIdentifiers_Start) {
			return new NonemptyIdentifierList_MoreIdentifiers_Start(node);
		}
		if (type == Symbols.nonemptyIdentifierList_MoreIdentifiers_Next) {
			return new NonemptyIdentifierList_MoreIdentifiers_Next(node);
		}
		if (type == Symbols.__PARSED_FRAGMENT) {
			return null;
		}

		throw new RuntimeException("cannot create PSI element for AST node due to unknown element type: " + type);
	}

}
