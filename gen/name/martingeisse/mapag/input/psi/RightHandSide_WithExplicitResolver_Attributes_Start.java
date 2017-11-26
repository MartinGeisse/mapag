package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RightHandSide_WithExplicitResolver_Attributes_Start extends RightHandSide_WithExplicitResolver_Attributes {

	public RightHandSide_WithExplicitResolver_Attributes_Start(@NotNull ASTNode node) {
		super(node);
	}

	public ImmutableList<RightHandSideAttribute> getAll() {
		return ImmutableList.of();
	}

	public void addAllTo(List<RightHandSideAttribute> list) {
	}

	public void addAllTo(ImmutableList.Builder<RightHandSideAttribute> builder) {
	}

}
