package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class PrecedenceDeclaration_Associativity_Left extends PrecedenceDeclaration_Associativity {

	public PrecedenceDeclaration_Associativity_Left(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getLeft() {
		return (LeafPsiElement) (getChildren()[0]);
	}

}
