package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class PrecedenceDeclaration_Normal_Associativity_Left extends PrecedenceDeclaration_Normal_Associativity {

	public PrecedenceDeclaration_Normal_Associativity_Left(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getLeft() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

}
