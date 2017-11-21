package name.martingeisse.mapag.input.psi;

public final class InternalPsiUtil {

	// prevent instantiation
	private InternalPsiUtil() {
	}

	/**
	 * This method works similar to parent.getChildren()[childIndex], except that it deals with all nodes, not just
	 * subclasses of CompositeElement.
	 */
	public static PsiElement getChild(ASTDelegatePsiElement parent, int childIndex) {
		PsiElement child = parent.getFirstChild();
		for (int i = 0; i < childIndex; i++) {
			child = child.getNextSibling();
		}
		return child;
	}

}
