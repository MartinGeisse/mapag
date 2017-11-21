package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class NonemptyIdentifierList_1 extends ASTWrapperPsiElement {

	public NonemptyIdentifierList_1(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getIdentifier() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 1);
	}

}
