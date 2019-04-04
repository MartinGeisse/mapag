package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class PrecedenceDeclaration_Normal extends PrecedenceDeclaration {

	public PrecedenceDeclaration_Normal(@NotNull ASTNode node) {
		super(node);
	}

	public PrecedenceDeclaration_Normal_Associativity getAssociativity() {
		return (PrecedenceDeclaration_Normal_Associativity) InternalPsiUtil.getChild(this, 0);
	}

	public ListNode<PrecedenceDeclarationSymbol> getTerminals() {
		return (ListNode<PrecedenceDeclarationSymbol>) InternalPsiUtil.getChild(this, 1);
	}

}
