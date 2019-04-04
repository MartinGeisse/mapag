package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Grammar_PrecedenceTable extends ASTWrapperPsiElement {

	public Grammar_PrecedenceTable(@NotNull ASTNode node) {
		super(node);
	}

	public ListNode<PrecedenceDeclaration> getPrecedenceDeclarations() {
		return (ListNode<PrecedenceDeclaration>) InternalPsiUtil.getChild(this, 2);
	}

}