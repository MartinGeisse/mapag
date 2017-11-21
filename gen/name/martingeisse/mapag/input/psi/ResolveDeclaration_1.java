package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class ResolveDeclaration_1 extends ASTWrapperPsiElement {

	public ResolveDeclaration_1(@NotNull ASTNode node) {
		super(node);
	}

	public ResolveDeclarationSymbol getSymbol() {
		return (ResolveDeclarationSymbol) InternalPsiUtil.getChild(this, 1);
	}

}
