package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class RightHandSideAttribute_ResolveBlock extends RightHandSideAttribute {

	public RightHandSideAttribute_ResolveBlock(@NotNull ASTNode node) {
		super(node);
	}

	public RightHandSideAttribute_ResolveBlock_ResolveDeclarations getResolveDeclarations() {
		return (RightHandSideAttribute_ResolveBlock_ResolveDeclarations) InternalPsiUtil.getChild(this, 2);
	}

}
