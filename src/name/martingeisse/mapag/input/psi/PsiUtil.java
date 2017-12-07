package name.martingeisse.mapag.input.psi;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.FileContentUtil;
import com.intellij.util.IncorrectOperationException;

/**
 *
 */
final class PsiUtil {

	// prevent instantiation
	private PsiUtil() {
	}

	//
	// general
	//

	// TODO support .getNameIdentifier() in the nodes themselves? Probably useful for PsiNameIdentifierOwner. Then remove this.
	public static LeafPsiElement getNonterminalNameNode(Production production) {
		if (production instanceof Production_SingleUnnamed) {
			return ((Production_SingleUnnamed) production).getNonterminalName();
		} else if (production instanceof Production_SingleNamed) {
			return ((Production_SingleNamed) production).getNonterminalName();
		} else if (production instanceof Production_Multi) {
			return ((Production_Multi) production).getNonterminalName();
		} else if (production instanceof Production_ErrorWithNonterminalNameWithSemicolon) {
			return ((Production_ErrorWithNonterminalNameWithSemicolon) production).getNonterminalName();
		} else if (production instanceof Production_ErrorWithNonterminalNameWithClosingCurlyBrace) {
			return ((Production_ErrorWithNonterminalNameWithClosingCurlyBrace) production).getNonterminalName();
		} else if (production instanceof Production_ErrorWithNonterminalNameAtEof) {
			return ((Production_ErrorWithNonterminalNameAtEof) production).getNonterminalName();
		} else {
			return null;
		}
	}

	public static PsiElement setText(LeafPsiElement element, String newText) {
		return (PsiElement) element.replaceWithText(newText);
	}

	public static <T> T getAncestor(PsiElement element, Class<T> nodeClass) {
		while (true) {
			if (nodeClass.isInstance(element)) {
				return nodeClass.cast(element);
			}
			if (element == null || element instanceof PsiFile) {
				return null;
			}
			element = element.getParent();
		}
	}

	//
	// reference support
	//

	public static PsiReference getReference(Expression_Identifier expression) {
		return new IdentifierExpressionReference(expression);
	}

	//
	// naming support
	//

	public static LeafPsiElement getNameIdentifier(Production_SingleUnnamed node) {
		return node.getNonterminalName();
	}

	public static LeafPsiElement getNameIdentifier(Production_SingleNamed node) {
		return node.getNonterminalName();
	}

	public static LeafPsiElement getNameIdentifier(Production_Multi node) {
		return node.getNonterminalName();
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithoutNonterminalNameWithSemicolon node) {
		return null;
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithoutNonterminalNameWithClosingCurlyBrace node) {
		return null;
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithoutNonterminalNameAtEof node) {
		return null;
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithNonterminalNameWithSemicolon node) {
		return node.getNonterminalName();
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithNonterminalNameWithClosingCurlyBrace node) {
		return node.getNonterminalName();
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithNonterminalNameAtEof node) {
		return node.getNonterminalName();
	}

	//
	// safe delete
	//

	public static void delete(Production node) throws IncorrectOperationException {
		PsiFile psiFile = node.getContainingFile();
		if (psiFile != null) {
			VirtualFile virtualFile = psiFile.getVirtualFile();
			if (virtualFile != null) {
				node.superclassDelete();
				FileContentUtil.reparseFiles(virtualFile);
				return;
			}
		}
		throw new IncorrectOperationException("could not determine containing virtual file to reparse after safe delete");
	}

}
