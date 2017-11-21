package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class ResolveDeclarationSymbol_Identifier extends ResolveDeclarationSymbol {

	public ResolveDeclarationSymbol_Identifier(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getSymbol() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

}
