package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public final class Production_SingleNamed extends Production {

	public Production_SingleNamed(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getNonterminalName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

	public LeafPsiElement getAlternativeName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 2);
	}

	public RightHandSide getRightHandSide() {
		return (RightHandSide) InternalPsiUtil.getChild(this, 4);
	}

	public PsiElement getNameIdentifier() {
		return PsiUtil.getNameIdentifier(this);
	}

	public String getName() {
		return getNameIdentifier().getText();
	}

	public PsiElement setName(String newName) throws IncorrectOperationException {
		return (LeafPsiElement) getNameIdentifier().replaceWithText(newName);
	}

	public void delete() throws IncorrectOperationException {
		PsiUtil.delete(this);
	}

}
