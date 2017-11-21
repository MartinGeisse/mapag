package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class NonemptyIdentifierList extends ASTWrapperPsiElement {

	public NonemptyIdentifierList(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getFirstIdentifier() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

	public NonemptyIdentifierList_MoreIdentifiers getMoreIdentifiers() {
		return (NonemptyIdentifierList_MoreIdentifiers) InternalPsiUtil.getChild(this, 1);
	}

}
