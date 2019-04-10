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

public final class ResolveDeclarationImpl extends ASTWrapperPsiElement implements ResolveDeclaration,PsiCm {

    public ResolveDeclarationImpl(@NotNull ASTNode node) {
        super(node);
    }

    	    public ResolveDeclaration_Action getAction() {
            return (ResolveDeclaration_Action)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 0));
        }

    	public ResolveDeclaration_ActionImpl getActionPsi() {
            return (ResolveDeclaration_ActionImpl)InternalPsiUtil.getChild(this, 0);
        }
    	    public CmList<CmToken> getSymbols() {
            return (CmList<CmToken>)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 1));
        }

    	public CmListImpl<CmToken, LeafPsiElement> getSymbolsPsi() {
            return (CmListImpl<CmToken, LeafPsiElement>)InternalPsiUtil.getChild(this, 1);
        }
    
        
    
    
    
}
