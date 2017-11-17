package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class NonemptyIdentifierList_MoreIdentifiers extends ASTWrapperPsiElement {

	public NonemptyIdentifierList_MoreIdentifiers(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<NonemptyIdentifierList_1> getAll();

	public abstract void addAllTo(List<NonemptyIdentifierList_1> list);

	public abstract void addAllTo(ImmutableList.Builder<NonemptyIdentifierList_1> builder);

}
