package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class TerminalDeclarations extends ASTWrapperPsiElement {

	public TerminalDeclarations(@NotNull ASTNode node) {
		super(node);
	}

	public TerminalDeclaration getFirstIdentifier() {
		return (TerminalDeclaration) InternalPsiUtil.getChild(this, 0);
	}

	public TerminalDeclarations_MoreIdentifiers getMoreIdentifiers() {
		return (TerminalDeclarations_MoreIdentifiers) InternalPsiUtil.getChild(this, 1);
	}

}
