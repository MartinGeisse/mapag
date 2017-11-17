package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi extends Production {

	public Production_Multi(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getNonterminalName() {
		return (LeafPsiElement) (getChildren()[0]);
	}

	public Production_Multi_Alternatives getAlternatives() {
		return (Production_Multi_Alternatives) (getChildren()[3]);
	}

}
