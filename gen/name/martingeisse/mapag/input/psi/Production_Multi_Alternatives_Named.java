package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi_Alternatives_Named extends Production_Multi_Alternatives {

	public Production_Multi_Alternatives_Named(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getAlternativeName() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

	public RightHandSide getRightHandSide() {
		return (RightHandSide) InternalPsiUtil.getChild(this, 2);
	}

}
