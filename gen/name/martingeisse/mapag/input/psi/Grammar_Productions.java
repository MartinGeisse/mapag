package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Grammar_Productions extends ASTWrapperPsiElement {

	public Grammar_Productions(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<Production> getAll();

	public abstract void addAllTo(List<Production> list);

	public abstract void addAllTo(ImmutableList.Builder<Production> builder);

}
