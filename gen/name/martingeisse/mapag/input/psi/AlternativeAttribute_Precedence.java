package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public final class AlternativeAttribute_Precedence extends AlternativeAttribute {

	public AlternativeAttribute_Precedence(@NotNull ASTNode node) {
		super(node);
	}

	public LeafPsiElement getPrecedenceDefiningTerminal() {
		return (LeafPsiElement) InternalPsiUtil.getChild(this, 1);
	}

}
