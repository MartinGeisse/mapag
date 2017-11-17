package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Grammar_1 extends ASTWrapperPsiElement {

	public Grammar_1(@NotNull ASTNode node) {
		super(node);
	}

	public Grammar_1_PrecedenceDeclarations getPrecedenceDeclarations() {
		return (Grammar_1_PrecedenceDeclarations) (getChildren()[2]);
	}

}
