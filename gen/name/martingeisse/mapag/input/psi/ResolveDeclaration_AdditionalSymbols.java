package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ResolveDeclaration_AdditionalSymbols extends ASTWrapperPsiElement {

	public ResolveDeclaration_AdditionalSymbols(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<ResolveDeclaration_1> getAll();

	public abstract void addAllTo(List<ResolveDeclaration_1> list);

	public abstract void addAllTo(ImmutableList.Builder<ResolveDeclaration_1> builder);

}
