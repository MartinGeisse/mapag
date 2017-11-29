package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class NonemptyIdentifierList_MoreIdentifiers_Start extends NonemptyIdentifierList_MoreIdentifiers {

	public NonemptyIdentifierList_MoreIdentifiers_Start(@NotNull ASTNode node) {
		super(node);
	}

	public ImmutableList<NonemptyIdentifierList_MoreIdentifiers_1> getAll() {
		return ImmutableList.of();
	}

	public void addAllTo(List<NonemptyIdentifierList_MoreIdentifiers_1> list) {
	}

	public void addAllTo(ImmutableList.Builder<NonemptyIdentifierList_MoreIdentifiers_1> builder) {
	}

}
