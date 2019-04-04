package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi_Alternatives_Unnamed extends Production_Multi_Alternatives {

	public Production_Multi_Alternatives_Unnamed(@NotNull ASTNode node) {
		super(node);
	}

	public RightHandSide getRightHandSide() {
		return (RightHandSide) InternalPsiUtil.getChild(this, 0);
	}

}
