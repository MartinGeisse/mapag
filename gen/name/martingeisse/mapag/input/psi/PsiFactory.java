package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;

import name.martingeisse.mapag.input.Symbols;

public class PsiFactory {

    public static PsiElement createPsiElement(ASTNode node) {
        IElementType type = node.getElementType();

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
                    if (type == Symbols.resolveDeclaration_3_Start) {
                return new ResolveDeclaration_3_Start(node);
            }
                    if (type == Symbols.resolveDeclaration_3_Next) {
                return new ResolveDeclaration_3_Next(node);
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
                    if (type == Symbols.resolveDeclaration_1_A0) {
                return new ResolveDeclaration_1_A0(node);
            }
                    if (type == Symbols.resolveDeclaration_1_A1) {
                return new ResolveDeclaration_1_A1(node);
            }
                    if (type == Symbols.resolveDeclaration_2) {
                return new ResolveDeclaration_2(node);
            }
                    if (type == Symbols.production_Multi_2_Start) {
                return new Production_Multi_2_Start(node);
            }
                    if (type == Symbols.production_Multi_2_Next) {
                return new Production_Multi_2_Next(node);
            }
                    if (type == Symbols.resolveDeclarationSymbol_Identifier) {
                return new ResolveDeclarationSymbol_Identifier(node);
            }
                    if (type == Symbols.resolveDeclarationSymbol_Eof) {
                return new ResolveDeclarationSymbol_Eof(node);
            }
                    if (type == Symbols.production_Multi_1_A0) {
                return new Production_Multi_1_A0(node);
            }
                    if (type == Symbols.production_Multi_1_A1) {
                return new Production_Multi_1_A1(node);
            }
                    if (type == Symbols.precedenceDeclaration_1_A0) {
                return new PrecedenceDeclaration_1_A0(node);
            }
                    if (type == Symbols.precedenceDeclaration_1_A1) {
                return new PrecedenceDeclaration_1_A1(node);
            }
                    if (type == Symbols.precedenceDeclaration_1_A2) {
                return new PrecedenceDeclaration_1_A2(node);
            }
                    if (type == Symbols.precedenceDeclaration) {
                return new PrecedenceDeclaration(node);
            }
                    if (type == Symbols.grammar_1) {
                return new Grammar_1(node);
            }
                    if (type == Symbols.nonemptyIdentifierList_2_Start) {
                return new NonemptyIdentifierList_2_Start(node);
            }
                    if (type == Symbols.nonemptyIdentifierList_2_Next) {
                return new NonemptyIdentifierList_2_Next(node);
            }
                    if (type == Symbols.grammar_3_Start) {
                return new Grammar_3_Start(node);
            }
                    if (type == Symbols.grammar_3_Next) {
                return new Grammar_3_Next(node);
            }
                    if (type == Symbols.grammar) {
                return new Grammar(node);
            }
                    if (type == Symbols.grammar_2_Absent) {
                return new Grammar_2_Absent(node);
            }
                    if (type == Symbols.grammar_2_Present) {
                return new Grammar_2_Present(node);
            }
                    if (type == Symbols.rightHandSide_WithExplicitResolver_1_Start) {
                return new RightHandSide_WithExplicitResolver_1_Start(node);
            }
                    if (type == Symbols.rightHandSide_WithExplicitResolver_1_Next) {
                return new RightHandSide_WithExplicitResolver_1_Next(node);
            }
                    if (type == Symbols.nonemptyIdentifierList_1) {
                return new NonemptyIdentifierList_1(node);
            }
                    if (type == Symbols.nonemptyIdentifierList) {
                return new NonemptyIdentifierList(node);
            }
                    if (type == Symbols.grammar_1_1_Start) {
                return new Grammar_1_1_Start(node);
            }
                    if (type == Symbols.grammar_1_1_Next) {
                return new Grammar_1_1_Next(node);
            }
                    if (type == Symbols.resolveDeclaration) {
                return new ResolveDeclaration(node);
            }
        
        throw new RuntimeException("cannot create PSI element for AST node due to unknown element type: " + type);
    }

}
