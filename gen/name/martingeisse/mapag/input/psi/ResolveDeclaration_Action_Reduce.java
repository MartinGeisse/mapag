package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class ResolveDeclaration_Action_Reduce extends ResolveDeclaration_Action {

	public ResolveDeclaration_Action_Reduce(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getReduce() {
		return (LeafPsiElement) (getChildren()[0]);
	}

}
