package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Grammar extends ASTWrapperPsiElement {

	public Grammar(@NotNull ASTNode node) {
		super(node);
	}

	public NonemptyIdentifierList getTerminals() {
		return (NonemptyIdentifierList) InternalPsiUtil.getChild(this, 2);
	}

	public Grammar_PrecedenceTable getPrecedenceTable() {
		return (Grammar_PrecedenceTable) InternalPsiUtil.getChild(this, 4);
	}

	public LeafPsiElement getStartSymbolName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 6);
	}

	public Grammar_Productions getProductions() {
		return (Grammar_Productions) InternalPsiUtil.getChild(this, 8);
	}

}
