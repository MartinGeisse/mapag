package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class ResolveDeclaration extends ASTWrapperPsiElement {

	public ResolveDeclaration(@NotNull ASTNode node) {
		super(node);
	}

	public ResolveDeclaration_Action getAction() {
		return (ResolveDeclaration_Action) InternalPsiUtil.getChild(this, 0);
	}

	public ListNode<LeafPsiElement> getSymbols() {
		return (ListNode<LeafPsiElement>) InternalPsiUtil.getChild(this, 1);
	}

}
