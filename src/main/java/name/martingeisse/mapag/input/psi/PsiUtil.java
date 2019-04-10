package name.martingeisse.mapag.input.psi;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.FileContentUtil;
import com.intellij.util.IncorrectOperationException;
import name.martingeisse.mapag.input.cm.impl.*;

/**
 *
 */
public final class PsiUtil {

	// prevent instantiation
	private PsiUtil() {
	}

	//
	// general
	//

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

	public static PsiReference getReference(PrecedenceDeclarationSymbolImpl precedenceDeclarationSymbol) {
		return new PrecedenceDeclarationSymbolReference(precedenceDeclarationSymbol);
	}

	public static PsiReference getReference(Expression_IdentifierImpl expression) {
		return new IdentifierExpressionReference(expression);
	}

	//
	// naming support
	//

	public static LeafPsiElement getNameIdentifier(TerminalDeclarationImpl node) {
		return node.getIdentifierPsi();
	}

	public static LeafPsiElement getNameIdentifier(Production_SingleUnnamedImpl node) {
		return node.getNonterminalNamePsi();
	}

	public static LeafPsiElement getNameIdentifier(Production_SingleNamedImpl node) {
		return node.getNonterminalNamePsi();
	}

	public static LeafPsiElement getNameIdentifier(Production_MultiImpl node) {
		return node.getNonterminalNamePsi();
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithoutNonterminalNameWithSemicolonImpl node) {
		return null;
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithoutNonterminalNameWithClosingCurlyBraceImpl node) {
		return null;
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithoutNonterminalNameAtEofImpl node) {
		return null;
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithNonterminalNameWithSemicolonImpl node) {
		return node.getNonterminalNamePsi();
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithNonterminalNameWithClosingCurlyBraceImpl node) {
		return node.getNonterminalNamePsi();
	}

	public static LeafPsiElement getNameIdentifier(Production_ErrorWithNonterminalNameAtEofImpl node) {
		return node.getNonterminalNamePsi();
	}

	//
	// safe delete
	//

	public static void delete(TerminalDeclarationImpl node) throws IncorrectOperationException {
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

	public static void delete(ProductionImpl node) throws IncorrectOperationException {
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
