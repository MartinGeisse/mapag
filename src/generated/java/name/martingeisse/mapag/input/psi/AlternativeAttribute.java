package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class AlternativeAttribute extends ASTWrapperPsiElement {

	public AlternativeAttribute(@NotNull ASTNode node) {
		super(node);
	}

}
