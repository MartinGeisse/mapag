package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class ResolveDeclaration_Action_Shift extends ResolveDeclaration_Action {

	public ResolveDeclaration_Action_Shift(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getShift() {
		return (LeafPsiElement) (getChildren()[0]);
	}

}
