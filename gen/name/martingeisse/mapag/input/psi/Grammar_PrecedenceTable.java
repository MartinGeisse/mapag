package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class Grammar_PrecedenceTable extends ASTWrapperPsiElement {

	public Grammar_PrecedenceTable(@NotNull ASTNode node) {
		super(node);
	}

	public abstract Grammar_1 getIt();

}
