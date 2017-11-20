package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi_1_Named_Named extends ASTWrapperPsiElement {

	public Production_Multi_1_Named_Named(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getAlternativeName() {
		return (LeafPsiElement) (getChildren()[0]);
	}

	public RightHandSide getRightHandSide() {
		return (RightHandSide) (getChildren()[2]);
	}

}
