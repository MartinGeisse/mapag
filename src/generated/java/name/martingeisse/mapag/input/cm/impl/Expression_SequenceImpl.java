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

public final class Expression_SequenceImpl extends ExpressionImpl implements Expression_Sequence,PsiCm {

    public Expression_SequenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    	    public Expression getLeft() {
            return (Expression)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 0));
        }

    	public ExpressionImpl getLeftPsi() {
            return (ExpressionImpl)InternalPsiUtil.getChild(this, 0);
        }
    	    public Expression getRight() {
            return (Expression)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 1));
        }

    	public ExpressionImpl getRightPsi() {
            return (ExpressionImpl)InternalPsiUtil.getChild(this, 1);
        }
    
        
    
    
    
}
