package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Production_SingleUnnamed extends Production {

	public Production_SingleUnnamed(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getNonterminalName() {
		return (LeafPsiElement) (getChildren()[0]);
	}

	public RightHandSide getRightHandSide() {
		return (RightHandSide) (getChildren()[2]);
	}

}
