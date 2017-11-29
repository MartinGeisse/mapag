package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class TerminalDeclarations_MoreIdentifiers_1 extends ASTWrapperPsiElement {

	public TerminalDeclarations_MoreIdentifiers_1(@NotNull ASTNode node) {
		super(node);
	}

	public TerminalDeclaration getIdentifier() {
		return (TerminalDeclaration) InternalPsiUtil.getChild(this, 1);
	}

}
