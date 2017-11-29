package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TerminalDeclarations_MoreIdentifiers_Next extends TerminalDeclarations_MoreIdentifiers {

	public TerminalDeclarations_MoreIdentifiers_Next(@NotNull ASTNode node) {
		super(node);
	}

	public TerminalDeclarations_MoreIdentifiers getPrevious() {
		return (TerminalDeclarations_MoreIdentifiers) InternalPsiUtil.getChild(this, 0);
	}

	public TerminalDeclarations_MoreIdentifiers_1 getElement() {
		return (TerminalDeclarations_MoreIdentifiers_1) InternalPsiUtil.getChild(this, 1);
	}

	public ImmutableList<TerminalDeclarations_MoreIdentifiers_1> getAll() {
		ImmutableList.Builder<TerminalDeclarations_MoreIdentifiers_1> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<TerminalDeclarations_MoreIdentifiers_1> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<TerminalDeclarations_MoreIdentifiers_1> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
