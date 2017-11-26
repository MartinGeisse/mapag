package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class RightHandSide_WithExplicitResolver_Attributes extends ASTWrapperPsiElement {

	public RightHandSide_WithExplicitResolver_Attributes(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<RightHandSideAttribute> getAll();

	public abstract void addAllTo(List<RightHandSideAttribute> list);

	public abstract void addAllTo(ImmutableList.Builder<RightHandSideAttribute> builder);

}
