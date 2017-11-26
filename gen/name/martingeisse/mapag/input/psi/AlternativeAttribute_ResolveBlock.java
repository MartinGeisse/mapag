package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class AlternativeAttribute_ResolveBlock extends AlternativeAttribute {

	public AlternativeAttribute_ResolveBlock(@NotNull ASTNode node) {
		super(node);
	}

	public AlternativeAttribute_ResolveBlock_ResolveDeclarations getResolveDeclarations() {
		return (AlternativeAttribute_ResolveBlock_ResolveDeclarations) InternalPsiUtil.getChild(this, 2);
	}

}
