package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Production_Multi_2 extends ASTWrapperPsiElement {

	public Production_Multi_2(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<Production_Multi_1> getAll();

	public abstract void addAllTo(List<Production_Multi_1> list);

	public abstract void addAllTo(ImmutableList.Builder<Production_Multi_1> builder);

}
