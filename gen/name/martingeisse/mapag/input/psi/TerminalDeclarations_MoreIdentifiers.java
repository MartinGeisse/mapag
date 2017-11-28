package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TerminalDeclarations_MoreIdentifiers extends ASTWrapperPsiElement {

	public TerminalDeclarations_MoreIdentifiers(@NotNull ASTNode node) {
		super(node);
	}

	public abstract ImmutableList<TerminalDeclarations_1> getAll();

	public abstract void addAllTo(List<TerminalDeclarations_1> list);

	public abstract void addAllTo(ImmutableList.Builder<TerminalDeclarations_1> builder);

}
