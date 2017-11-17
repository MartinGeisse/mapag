package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Production_SingleNamed extends Production {

	public Production_SingleNamed(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getNonterminalName() {
		return (LeafPsiElement) (getChildren()[0]);
	}

	public LeafPsiElement getAlternativeName() {
		return (LeafPsiElement) (getChildren()[2]);
	}

	public RightHandSide getRightHandSide() {
		return (RightHandSide) (getChildren()[4]);
	}

}
