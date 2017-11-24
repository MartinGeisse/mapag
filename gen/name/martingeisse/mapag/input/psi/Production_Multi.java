package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi extends Production {

	public Production_Multi(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getNonterminalName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

	public Production_Multi_Alternatives getAlternatives() {
		return (Production_Multi_Alternatives) InternalPsiUtil.getChild(this, 3);
	}

	public String getName() {
		return PsiUtil.getName(this);
	}

	public PsiElement setName(String newName) throws IncorrectOperationException {
		return PsiUtil.setName(this, newName);
	}

	public PsiReference getReference() {
		return PsiUtil.getReference(this);
	}

}
