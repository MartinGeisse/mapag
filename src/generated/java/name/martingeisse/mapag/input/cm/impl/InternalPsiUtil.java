package name.martingeisse.mapag.input.cm.impl;

import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

import java.util.function.Consumer;

import name.martingeisse.mapag.input.cm.CmNode;
import name.martingeisse.mapag.input.cm.CmToken;

public final class InternalPsiUtil {

	private static TokenSet ignoredElementTypes;

	private static void initializeIgnoredElementTypes() {
		if (ignoredElementTypes == null) {
            name.martingeisse.mapag.ide.MapagParserDefinition parserDefinition = new name.martingeisse.mapag.ide.MapagParserDefinition();
			ignoredElementTypes = TokenSet.orSet(parserDefinition.getWhitespaceTokens(), parserDefinition.getCommentTokens());
		}
	}

	// prevent instantiation
	private InternalPsiUtil() {
	}

	/**
	 * This method works similar to parent.getChildren()[childIndex], except that it deals with all nodes, not just
	 * subclasses of CompositeElement.
	 */
	public static PsiElement getChild(ASTDelegatePsiElement parent, int childIndex) {
		initializeIgnoredElementTypes();
		PsiElement child = skipWhitespace(parent.getFirstChild());
		for (int i = 0; i < childIndex; i++) {
			child = skipWhitespace(child.getNextSibling());
		}
		return child;
	}

	/**
	 * Invokes the specified consumer on the children returned by getChild().
	 */
	public static void foreachChild(ASTDelegatePsiElement parent, Consumer<PsiElement> consumer) {
		initializeIgnoredElementTypes();
		PsiElement child = skipWhitespace(parent.getFirstChild());
		while (child != null) {
			consumer.accept(child);
			child = skipWhitespace(child.getNextSibling());
		}
	}

	private static PsiElement skipWhitespace(PsiElement element) {
		while (element != null && ignoredElementTypes.contains(element.getNode().getElementType())) {
			element = element.getNextSibling();
		}
		return element;
	}

    /**
     * Returns the CM node for a PSI node, or null if not possible.
     */
    public static CmNode getCmFromPsi(PsiElement psi) {
        if (psi instanceof CmNode) {
            return (CmNode)psi;
        } else if (psi instanceof LeafPsiElement) {
            return new CmTokenImpl((LeafPsiElement)psi);
        } else {
            return null;
        }
    }

    /**
     * Returns the PSI node for a CM node, or null if not possible.
     */
    public static PsiElement getPsiFromCm(CmNode cm) {
        if (cm instanceof PsiElement) {
            return (PsiElement)cm;
        } else if (cm instanceof CmTokenImpl) {
            return ((CmTokenImpl)cm).getPsi();
        } else {
            throw new RuntimeException("found CM node that is neither a PSI node nor a token: " + cm);
        }
    }

}
