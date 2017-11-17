package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RightHandSide_WithExplicitResolver_ResolveDeclarations_Next extends RightHandSide_WithExplicitResolver_ResolveDeclarations {

	public RightHandSide_WithExplicitResolver_ResolveDeclarations_Next(@NotNull ASTNode node) {
		super(node);
	}

	public RightHandSide_WithExplicitResolver_ResolveDeclarations getPrevious() {
		return (RightHandSide_WithExplicitResolver_ResolveDeclarations) (getChildren()[0]);
	}

	public ResolveDeclaration getElement() {
		return (ResolveDeclaration) (getChildren()[1]);
	}

	public ImmutableList<ResolveDeclaration> getAll() {
		ImmutableList.Builder<ResolveDeclaration> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<ResolveDeclaration> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<ResolveDeclaration> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
