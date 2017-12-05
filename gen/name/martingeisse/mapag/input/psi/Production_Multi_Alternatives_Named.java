package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi_Alternatives_Named extends Production_Multi_Alternatives {

	public Production_Multi_Alternatives_Named(@NotNull ASTNode node) {
		super(node);
	}

	public Production_Multi_Alternatives_Named_Named getNamed() {
		return (Production_Multi_Alternatives_Named_Named) InternalPsiUtil.getChild(this, 0);
	}

}
