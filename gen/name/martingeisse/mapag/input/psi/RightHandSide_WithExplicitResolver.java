package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class RightHandSide_WithExplicitResolver extends RightHandSide {

	public RightHandSide_WithExplicitResolver(@NotNull ASTNode node) {
		super(node);
	}

	public Expression getExpression() {
		return (Expression) (getChildren()[0]);
	}

	public RightHandSide_WithExplicitResolver_ResolveDeclarations getResolveDeclarations() {
		return (RightHandSide_WithExplicitResolver_ResolveDeclarations) (getChildren()[3]);
	}

}
