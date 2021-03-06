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

public final class Production_MultiImpl extends ProductionImpl implements Production_Multi,PsiCm {

    public Production_MultiImpl(@NotNull ASTNode node) {
        super(node);
    }

    	    public CmToken getNonterminalName() {
            return (CmToken)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 0));
        }

    	public LeafPsiElement getNonterminalNamePsi() {
            return (LeafPsiElement)InternalPsiUtil.getChild(this, 0);
        }
    	    public CmList<Production_Multi_Alternatives> getAlternatives() {
            return (CmList<Production_Multi_Alternatives>)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 3));
        }

    	public CmListImpl<Production_Multi_Alternatives, Production_Multi_AlternativesImpl> getAlternativesPsi() {
            return (CmListImpl<Production_Multi_Alternatives, Production_Multi_AlternativesImpl>)InternalPsiUtil.getChild(this, 3);
        }
    
        
        
            public LeafPsiElement getNameIdentifier() {
                return name.martingeisse.mapag.input.psi.PsiUtil.getNameIdentifier(this);
            }

            public String getName() {
                LeafPsiElement nameIdentifier = getNameIdentifier();
                return (nameIdentifier == null ? null : nameIdentifier.getText());
            }

            public PsiElement setName(String newName) throws IncorrectOperationException {
                LeafPsiElement nameIdentifier = getNameIdentifier();
                if (nameIdentifier == null) {
                    throw new IncorrectOperationException("name identifier not found");
                }
                return (LeafPsiElement) nameIdentifier.replaceWithText(newName);
            }

        
    
    
    
            public void delete() throws IncorrectOperationException {
            name.martingeisse.mapag.input.psi.PsiUtil.delete(this);
        }
    
}
