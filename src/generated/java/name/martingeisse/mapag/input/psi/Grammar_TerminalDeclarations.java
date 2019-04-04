package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Grammar_TerminalDeclarations extends ASTWrapperPsiElement {

	public Grammar_TerminalDeclarations(@NotNull ASTNode node) {
		super(node);
	}

	public ListNode<TerminalDeclaration> getIdentifiers() {
		return (ListNode<TerminalDeclaration>) InternalPsiUtil.getChild(this, 2);
	}

}
