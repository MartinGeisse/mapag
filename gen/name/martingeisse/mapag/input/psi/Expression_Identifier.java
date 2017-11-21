package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class Expression_Identifier extends Expression {

	public Expression_Identifier(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getIdentifier() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 0);
	}

}
