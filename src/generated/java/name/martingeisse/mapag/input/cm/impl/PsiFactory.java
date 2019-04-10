package name.martingeisse.mapag.input.cm.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.PsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.martingeisse.mapag.input.Symbols;
import name.martingeisse.mapag.input.cm.*;

public class PsiFactory {

    public static PsiElement createPsiElement(ASTNode node) {
        IElementType type = node.getElementType();

                    if (type == Symbols.rightHandSide) {
                return new RightHandSideImpl(node);
            }
                    if (type == Symbols.expression_Empty) {
                return new Expression_EmptyImpl(node);
            }
                    if (type == Symbols.expression_Identifier) {
                return new Expression_IdentifierImpl(node);
            }
                    if (type == Symbols.expression_Error) {
                return new Expression_ErrorImpl(node);
            }
                    if (type == Symbols.expression_Sequence) {
                return new Expression_SequenceImpl(node);
            }
                    if (type == Symbols.expression_Or) {
                return new Expression_OrImpl(node);
            }
                    if (type == Symbols.expression_ZeroOrMore) {
                return new Expression_ZeroOrMoreImpl(node);
            }
                    if (type == Symbols.expression_OneOrMore) {
                return new Expression_OneOrMoreImpl(node);
            }
                    if (type == Symbols.expression_SeparatedZeroOrMore) {
                return new Expression_SeparatedZeroOrMoreImpl(node);
            }
                    if (type == Symbols.expression_SeparatedOneOrMore) {
                return new Expression_SeparatedOneOrMoreImpl(node);
            }
                    if (type == Symbols.expression_Optional) {
                return new Expression_OptionalImpl(node);
            }
                    if (type == Symbols.expression_Parenthesized) {
                return new Expression_ParenthesizedImpl(node);
            }
                    if (type == Symbols.expression_Named) {
                return new Expression_NamedImpl(node);
            }
                    if (type == Symbols.production_SingleUnnamed) {
                return new Production_SingleUnnamedImpl(node);
            }
                    if (type == Symbols.production_SingleNamed) {
                return new Production_SingleNamedImpl(node);
            }
                    if (type == Symbols.production_Multi) {
                return new Production_MultiImpl(node);
            }
                    if (type == Symbols.production_ErrorWithoutNonterminalNameWithSemicolon) {
                return new Production_ErrorWithoutNonterminalNameWithSemicolonImpl(node);
            }
                    if (type == Symbols.production_ErrorWithoutNonterminalNameWithClosingCurlyBrace) {
                return new Production_ErrorWithoutNonterminalNameWithClosingCurlyBraceImpl(node);
            }
                    if (type == Symbols.production_ErrorWithoutNonterminalNameAtEof) {
                return new Production_ErrorWithoutNonterminalNameAtEofImpl(node);
            }
                    if (type == Symbols.production_ErrorWithNonterminalNameWithSemicolon) {
                return new Production_ErrorWithNonterminalNameWithSemicolonImpl(node);
            }
                    if (type == Symbols.production_ErrorWithNonterminalNameWithClosingCurlyBrace) {
                return new Production_ErrorWithNonterminalNameWithClosingCurlyBraceImpl(node);
            }
                    if (type == Symbols.production_ErrorWithNonterminalNameAtEof) {
                return new Production_ErrorWithNonterminalNameAtEofImpl(node);
            }
                    if (type == Symbols.synthetic_SeparatedList_TerminalDeclaration_COMMA_Nonempty) {
                return new CmListImpl<TerminalDeclaration, TerminalDeclarationImpl>(node, createTokenSet(Symbols.terminalDeclaration), TerminalDeclaration.class, TerminalDeclarationImpl.class);
            }
                    if (type == Symbols.production_Multi_Alternatives_Unnamed) {
                return new Production_Multi_Alternatives_UnnamedImpl(node);
            }
                    if (type == Symbols.production_Multi_Alternatives_Named) {
                return new Production_Multi_Alternatives_NamedImpl(node);
            }
                    if (type == Symbols.grammar_TerminalDeclarations) {
                return new Grammar_TerminalDeclarationsImpl(node);
            }
                    if (type == Symbols.synthetic_List_PrecedenceDeclaration) {
                return new CmListImpl<PrecedenceDeclaration, PrecedenceDeclarationImpl>(node, createTokenSet(Symbols.precedenceDeclaration_Normal, Symbols.precedenceDeclaration_ErrorWithSemicolon, Symbols.precedenceDeclaration_ErrorWithoutSemicolon), PrecedenceDeclaration.class, PrecedenceDeclarationImpl.class);
            }
                    if (type == Symbols.grammar_PrecedenceTable_Optional) {
                return new CmOptionalImpl<Grammar_PrecedenceTable, Grammar_PrecedenceTableImpl>(node);
            }
                    if (type == Symbols.synthetic_List_AlternativeAttribute) {
                return new CmListImpl<AlternativeAttribute, AlternativeAttributeImpl>(node, createTokenSet(Symbols.alternativeAttribute_Precedence, Symbols.alternativeAttribute_ResolveBlock, Symbols.alternativeAttribute_ReduceOnError, Symbols.alternativeAttribute_Eof), AlternativeAttribute.class, AlternativeAttributeImpl.class);
            }
                    if (type == Symbols.precedenceDeclaration_Normal) {
                return new PrecedenceDeclaration_NormalImpl(node);
            }
                    if (type == Symbols.precedenceDeclaration_ErrorWithSemicolon) {
                return new PrecedenceDeclaration_ErrorWithSemicolonImpl(node);
            }
                    if (type == Symbols.precedenceDeclaration_ErrorWithoutSemicolon) {
                return new PrecedenceDeclaration_ErrorWithoutSemicolonImpl(node);
            }
                    if (type == Symbols.synthetic_SeparatedList_PrecedenceDeclarationSymbol_COMMA_Nonempty) {
                return new CmListImpl<PrecedenceDeclarationSymbol, PrecedenceDeclarationSymbolImpl>(node, createTokenSet(Symbols.precedenceDeclarationSymbol), PrecedenceDeclarationSymbol.class, PrecedenceDeclarationSymbolImpl.class);
            }
                    if (type == Symbols.production_Multi_Alternatives_List) {
                return new CmListImpl<Production_Multi_Alternatives, Production_Multi_AlternativesImpl>(node, createTokenSet(Symbols.production_Multi_Alternatives_Unnamed, Symbols.production_Multi_Alternatives_Named), Production_Multi_Alternatives.class, Production_Multi_AlternativesImpl.class);
            }
                    if (type == Symbols.grammar) {
                return new GrammarImpl(node);
            }
                    if (type == Symbols.terminalDeclaration) {
                return new TerminalDeclarationImpl(node);
            }
                    if (type == Symbols.precedenceDeclarationSymbol) {
                return new PrecedenceDeclarationSymbolImpl(node);
            }
                    if (type == Symbols.synthetic_List_Production_Nonempty) {
                return new CmListImpl<Production, ProductionImpl>(node, createTokenSet(Symbols.production_SingleUnnamed, Symbols.production_SingleNamed, Symbols.production_Multi, Symbols.production_ErrorWithoutNonterminalNameWithSemicolon, Symbols.production_ErrorWithoutNonterminalNameWithClosingCurlyBrace, Symbols.production_ErrorWithoutNonterminalNameAtEof, Symbols.production_ErrorWithNonterminalNameWithSemicolon, Symbols.production_ErrorWithNonterminalNameWithClosingCurlyBrace, Symbols.production_ErrorWithNonterminalNameAtEof), Production.class, ProductionImpl.class);
            }
                    if (type == Symbols.resolveDeclaration_Action_Shift) {
                return new ResolveDeclaration_Action_ShiftImpl(node);
            }
                    if (type == Symbols.resolveDeclaration_Action_Reduce) {
                return new ResolveDeclaration_Action_ReduceImpl(node);
            }
                    if (type == Symbols.synthetic_SeparatedList_IDENTIFIER_COMMA_Nonempty) {
                return new CmListImpl<CmToken, LeafPsiElement>(node, createTokenSet(Symbols.IDENTIFIER), CmToken.class, LeafPsiElement.class);
            }
                    if (type == Symbols.resolveDeclaration) {
                return new ResolveDeclarationImpl(node);
            }
                    if (type == Symbols.grammar_PrecedenceTable) {
                return new Grammar_PrecedenceTableImpl(node);
            }
                    if (type == Symbols.synthetic_List_ResolveDeclaration) {
                return new CmListImpl<ResolveDeclaration, ResolveDeclarationImpl>(node, createTokenSet(Symbols.resolveDeclaration), ResolveDeclaration.class, ResolveDeclarationImpl.class);
            }
                    if (type == Symbols.alternativeAttribute_Precedence) {
                return new AlternativeAttribute_PrecedenceImpl(node);
            }
                    if (type == Symbols.alternativeAttribute_ResolveBlock) {
                return new AlternativeAttribute_ResolveBlockImpl(node);
            }
                    if (type == Symbols.alternativeAttribute_ReduceOnError) {
                return new AlternativeAttribute_ReduceOnErrorImpl(node);
            }
                    if (type == Symbols.alternativeAttribute_Eof) {
                return new AlternativeAttribute_EofImpl(node);
            }
                    if (type == Symbols.precedenceDeclaration_Normal_Associativity_Left) {
                return new PrecedenceDeclaration_Normal_Associativity_LeftImpl(node);
            }
                    if (type == Symbols.precedenceDeclaration_Normal_Associativity_Right) {
                return new PrecedenceDeclaration_Normal_Associativity_RightImpl(node);
            }
                    if (type == Symbols.precedenceDeclaration_Normal_Associativity_Nonassoc) {
                return new PrecedenceDeclaration_Normal_Associativity_NonassocImpl(node);
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
