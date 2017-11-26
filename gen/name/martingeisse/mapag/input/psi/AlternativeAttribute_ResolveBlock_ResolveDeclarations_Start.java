package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class AlternativeAttribute_ResolveBlock_ResolveDeclarations_Start extends AlternativeAttribute_ResolveBlock_ResolveDeclarations {

	public AlternativeAttribute_ResolveBlock_ResolveDeclarations_Start(@NotNull ASTNode node) {
		super(node);
	}

	public ImmutableList<ResolveDeclaration> getAll() {
		return ImmutableList.of();
	}

	public void addAllTo(List<ResolveDeclaration> list) {
	}

	public void addAllTo(ImmutableList.Builder<ResolveDeclaration> builder) {
	}

}
