package name.martingeisse.mapag.input.psi;

import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;

public final class InternalPsiUtil {

	private static TokenSet ignoredElementTypes;

	// prevent instantiation
	private InternalPsiUtil() {
	}

	/**
	 * This method works similar to parent.getChildren()[childIndex], except that it deals with all nodes, not just
	 * subclasses of CompositeElement.
	 */
	public static PsiElement getChild(ASTDelegatePsiElement parent, int childIndex) {
		if (ignoredElementTypes == null) {
			name.martingeisse.mapag.ide.MapagParserDefinition parserDefinition = new name.martingeisse.mapag.ide.MapagParserDefinition();
			ignoredElementTypes = TokenSet.orSet(parserDefinition.getWhitespaceTokens(), parserDefinition.getCommentTokens());
		}
		PsiElement child = skipWhitespace(parent.getFirstChild());
		for (int i = 0; i < childIndex; i++) {
			child = skipWhitespace(child.getNextSibling());
		}
		return child;
	}

	private static PsiElement skipWhitespace(PsiElement element) {
		while (ignoredElementTypes.contains(element.getNode().getElementType())) {
			element = element.getNextSibling();
		}
		return element;
	}

}