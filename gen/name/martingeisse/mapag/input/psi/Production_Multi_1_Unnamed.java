package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Production_Multi_1_Unnamed extends Production_Multi_1 {

	public Production_Multi_1_Unnamed(@NotNull ASTNode node) {
		super(node);
	}

	public Production_Multi_1_Unnamed_Unnamed getUnnamed() {
		return (Production_Multi_1_Unnamed_Unnamed) InternalPsiUtil.getChild(this, 0);
	}

}
