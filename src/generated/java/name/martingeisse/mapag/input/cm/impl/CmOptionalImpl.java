package name.martingeisse.mapag.input.cm.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import name.martingeisse.mapag.input.cm.CmNode;
import name.martingeisse.mapag.input.cm.CmOptional;

public final class CmOptionalImpl<T extends CmNode, PSI extends PsiElement> extends ASTWrapperPsiElement implements PsiCm, CmOptional<T> {

    public CmOptionalImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public T getIt() {
        return (T)InternalPsiUtil.getCmFromPsi(InternalPsiUtil.getChild(this, 0));
    }

    public PSI getItPsi() {
        return (PSI)InternalPsiUtil.getChild(this, 0);
    }

}
