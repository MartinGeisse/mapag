package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ResolveDeclaration_AdditionalSymbols_Start extends ResolveDeclaration_AdditionalSymbols {

	public ResolveDeclaration_AdditionalSymbols_Start(@NotNull ASTNode node) {
		super(node);
	}

	public ImmutableList<ResolveDeclaration_1> getAll() {
		return ImmutableList.of();
	}

	public void addAllTo(List<ResolveDeclaration_1> list) {
	}

	public void addAllTo(ImmutableList.Builder<ResolveDeclaration_1> builder) {
	}

}
