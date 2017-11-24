package name.martingeisse.mapag.input.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;

/**
 *
 */
final class PsiUtil {

	// prevent instantiation
	private PsiUtil() {
	}

	public static String getName(Production_SingleUnnamed node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_SingleUnnamed node, String newName) throws IncorrectOperationException {
		// rawReplaceWithText()? The docs don't tell the difference.
		return (LeafPsiElement)node.getNonterminalName().replaceWithText(newName);
	}

	public static String getName(Production_SingleNamed node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_SingleNamed node, String newName) throws IncorrectOperationException {
		return (LeafPsiElement)node.getNonterminalName().replaceWithText(newName);
	}

	public static String getName(Production_Multi node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_Multi node, String newName) throws IncorrectOperationException {
		return (LeafPsiElement)node.getNonterminalName().replaceWithText(newName);
	}

	public static String getName(Production_Error node) {
		return null;
	}

	public static PsiElement setName(Production_Error node, String newName) throws IncorrectOperationException {
		throw new IncorrectOperationException("grammar contains errors");
	}

}
