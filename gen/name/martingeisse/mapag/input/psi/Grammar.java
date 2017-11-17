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
		return (NonemptyIdentifierList) (getChildren()[2]);
	}

	public NonemptyIdentifierList getNonterminals() {
		return (NonemptyIdentifierList) (getChildren()[6]);
	}

	public Grammar_PrecedenceTable getPrecedenceTable() {
		return (Grammar_PrecedenceTable) (getChildren()[8]);
	}

	public LeafPsiElement getStartSymbolName() {
		return (LeafPsiElement) (getChildren()[10]);
	}

	public Grammar_Productions getProductions() {
		return (Grammar_Productions) (getChildren()[12]);
	}

}
