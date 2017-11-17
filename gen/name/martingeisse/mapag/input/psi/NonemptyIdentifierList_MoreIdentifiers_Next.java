package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class NonemptyIdentifierList_MoreIdentifiers_Next extends NonemptyIdentifierList_MoreIdentifiers {

	public NonemptyIdentifierList_MoreIdentifiers_Next(@NotNull ASTNode node) {
		super(node);
	}

	public NonemptyIdentifierList_MoreIdentifiers getPrevious() {
		return (NonemptyIdentifierList_MoreIdentifiers) (getChildren()[0]);
	}

	public NonemptyIdentifierList_1 getElement() {
		return (NonemptyIdentifierList_1) (getChildren()[1]);
	}

	public ImmutableList<NonemptyIdentifierList_1> getAll() {
		ImmutableList.Builder<NonemptyIdentifierList_1> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<NonemptyIdentifierList_1> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<NonemptyIdentifierList_1> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
