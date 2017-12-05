package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Grammar extends ASTWrapperPsiElement {

	public Grammar(@NotNull ASTNode node) {
		super(node);
	}

	public TerminalDeclarations getTerminals() {
		return (TerminalDeclarations) InternalPsiUtil.getChild(this, 2);
	}

	public Optional<Grammar_PrecedenceTable> getPrecedenceTable() {
		return (Optional<Grammar_PrecedenceTable>) InternalPsiUtil.getChild(this, 4);
	}

	public LeafPsiElement getStartSymbolName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 6);
	}

	public ListNode<Production> getProductions() {
		return (ListNode<Production>) InternalPsiUtil.getChild(this, 8);
	}

}
