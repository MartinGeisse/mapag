package name.martingeisse.mapag.input.psi;

import com.intellij.psi.impl.source.tree.LeafPsiElement;

/**
 *
 */
public class ProductionSelfReference extends SelfReference<Production> {

	public ProductionSelfReference(Production production) {
		super(production);
	}

	protected LeafPsiElement getNameNode() {
		return PsiUtil.getNonterminalNameNode(referenceElement);
	}

}
