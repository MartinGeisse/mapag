package name.martingeisse.mapag.input.cm.impl;

import com.intellij.psi.PsiElement;

import name.martingeisse.mapag.input.cm.CmNode;

public interface PsiCm extends CmNode {

    default PsiElement getPsi() {
        return InternalPsiUtil.getPsiFromCm(this);
    }

    @Override
    default CmNode getCmParent() {
        PsiElement thisPsi = getPsi();
        if (thisPsi == null) {
            return null;
        }
        PsiElement parentPsi = thisPsi.getParent();
        if (parentPsi == null) {
            return null;
        }
        return InternalPsiUtil.getCmFromPsi(parentPsi);
    }

}
