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

public final class Grammar_TerminalDeclarationsImpl extends ASTWrapperPsiElement implements Grammar_TerminalDeclarations,PsiCm {

    public Grammar_TerminalDeclarationsImpl(@NotNull ASTNode node) {
        super(node);
    }

    	    public CmList<TerminalDeclaration> getIdentifiers() {
            return (CmList<TerminalDeclaration>)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 2));
        }

    	public CmListImpl<TerminalDeclaration, TerminalDeclarationImpl> getIdentifiersPsi() {
            return (CmListImpl<TerminalDeclaration, TerminalDeclarationImpl>)InternalPsiUtil.getChild(this, 2);
        }
    
        
    
    
    
}
