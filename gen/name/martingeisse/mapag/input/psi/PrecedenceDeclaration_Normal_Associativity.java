package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class PrecedenceDeclaration_Normal_Associativity extends ASTWrapperPsiElement {

	public PrecedenceDeclaration_Normal_Associativity(@NotNull ASTNode node) {
		super(node);
	}

}
