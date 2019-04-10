package name.martingeisse.mapag.input.cm.impl;

import com.intellij.lang.ASTNode;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.TokenType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;
import com.intellij.util.IncorrectOperationException;
import com.intellij.psi.PsiReference;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.martingeisse.mapag.input.cm.*;

public final class GrammarImpl extends ASTWrapperPsiElement implements Grammar,PsiCm {

    public GrammarImpl(@NotNull ASTNode node) {
        super(node);
    }

    	    public Grammar_TerminalDeclarations getTerminalDeclarations() {
            return (Grammar_TerminalDeclarations)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 0));
        }

    	public Grammar_TerminalDeclarationsImpl getTerminalDeclarationsPsi() {
            return (Grammar_TerminalDeclarationsImpl)InternalPsiUtil.getChild(this, 0);
        }
    	    public CmOptional<Grammar_PrecedenceTable> getPrecedenceTable() {
            return (CmOptional<Grammar_PrecedenceTable>)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 1));
        }

    	public CmOptionalImpl<Grammar_PrecedenceTable> getPrecedenceTablePsi() {
            return (CmOptionalImpl<Grammar_PrecedenceTable>)InternalPsiUtil.getChild(this, 1);
        }
    	    public CmToken getStartSymbolName() {
            return (CmToken)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 3));
        }

    	public LeafPsiElement getStartSymbolNamePsi() {
            return (LeafPsiElement)InternalPsiUtil.getChild(this, 3);
        }
    	    public CmList<Production> getProductions() {
            return (CmList<Production>)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 5));
        }

    	public CmListImpl<Production> getProductionsPsi() {
            return (CmListImpl<Production>)InternalPsiUtil.getChild(this, 5);
        }
    
        
    
    
    
}
