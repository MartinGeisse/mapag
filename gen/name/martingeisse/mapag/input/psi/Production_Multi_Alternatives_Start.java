package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Production_Multi_Alternatives_Start extends Production_Multi_Alternatives {

	public Production_Multi_Alternatives_Start(@NotNull ASTNode node) {
		super(node);
	}

	public ImmutableList<Production_Multi_1> getAll() {
		return ImmutableList.of();
	}

	public void addAllTo(List<Production_Multi_1> list) {
	}

	public void addAllTo(ImmutableList.Builder<Production_Multi_1> builder) {
	}

}
