package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class PrecedenceDeclaration extends ASTWrapperPsiElement {

	public PrecedenceDeclaration(@NotNull ASTNode node) {
		super(node);
	}

	public PrecedenceDeclaration_Associativity getAssociativity() {
		return (PrecedenceDeclaration_Associativity) InternalPsiUtil.getChild(this, 0);
	}

	public ListNode<LeafPsiElement> getTerminals() {
		return (ListNode<LeafPsiElement>) InternalPsiUtil.getChild(this, 1);
	}

}
