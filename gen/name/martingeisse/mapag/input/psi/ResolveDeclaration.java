package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class ResolveDeclaration extends ASTWrapperPsiElement {

	public ResolveDeclaration(@NotNull ASTNode node) {
		super(node);
	}

	public ResolveDeclaration_Action getAction() {
		return (ResolveDeclaration_Action) (getChildren()[0]);
	}

	public ResolveDeclarationSymbol getFirstSymbol() {
		return (ResolveDeclarationSymbol) (getChildren()[1]);
	}

	public ResolveDeclaration_AdditionalSymbols getAdditionalSymbols() {
		return (ResolveDeclaration_AdditionalSymbols) (getChildren()[2]);
	}

}
