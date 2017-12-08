package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class PrecedenceDeclarationSymbol extends ASTWrapperPsiElement {

	public PrecedenceDeclarationSymbol(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getIdentifier() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

	public PsiReference getReference() {
		return PsiUtil.getReference(this);
	}

}
