package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Grammar_1_PrecedenceDeclarations extends ASTWrapperPsiElement {

	public Grammar_1_PrecedenceDeclarations(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<PrecedenceDeclaration> getAll();

	public abstract void addAllTo(List<PrecedenceDeclaration> list);

	public abstract void addAllTo(ImmutableList.Builder<PrecedenceDeclaration> builder);

}
