package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class RightHandSide_Attributes extends ASTWrapperPsiElement {

	public RightHandSide_Attributes(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<AlternativeAttribute> getAll();

	public abstract void addAllTo(List<AlternativeAttribute> list);

	public abstract void addAllTo(ImmutableList.Builder<AlternativeAttribute> builder);

}
