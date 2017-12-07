package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public final class Production_ErrorWithoutNonterminalNameWithClosingCurlyBrace extends Production {

	public Production_ErrorWithoutNonterminalNameWithClosingCurlyBrace(@NotNull ASTNode node) {
		super(node);
	}

	public PsiElement getNameIdentifier() {
		return PsiUtil.getNameIdentifier(this);
	}

	public String getName() {
		return getNameIdentifier().getText();
	}

	public PsiElement setName(String newName) throws IncorrectOperationException {
		return null;
	}

	public void delete() throws IncorrectOperationException {
		PsiUtil.delete(this);
	}

}
