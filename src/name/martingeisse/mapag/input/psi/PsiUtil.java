package name.martingeisse.mapag.input.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;

import java.util.ArrayList;
import java.util.List;

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

	// TODO not needed, .getName() does the same
	public static String getNonterminalName(Production production) {
		PsiElement element = getNonterminalNameNode(production);
		return (element == null ? null : element.getText());
	}

	// TODO support .getNameIdentifier() in the nodes themselves? Probably useful for PsiNameIdentifierOwner. Then remove this.
	public static LeafPsiElement getNonterminalNameNode(Production production) {
		if (production instanceof Production_SingleUnnamed) {
			return ((Production_SingleUnnamed) production).getNonterminalName();
		} else if (production instanceof Production_SingleNamed) {
			return ((Production_SingleNamed) production).getNonterminalName();
		} else if (production instanceof Production_Multi) {
			return ((Production_Multi) production).getNonterminalName();
		} else if (production instanceof Production_Error3) {
			return ((Production_Error3) production).getNonterminalName();
		} else if (production instanceof Production_Error4) {
			return ((Production_Error4) production).getNonterminalName();
		} else {
			return null;
		}
	}

	public static PsiElement setText(LeafPsiElement element, String newText) {
		return (PsiElement) element.replaceWithText(newText);
	}

	public static List<String> convertNonemptyIdentifierListToStrings(NonemptyIdentifierList list) {
		List<String> strings = new ArrayList<>();
		strings.add(list.getFirstIdentifier().getText());
		for (NonemptyIdentifierList_1 more : list.getMoreIdentifiers().getAll()) {
			strings.add(more.getIdentifier().getText());
		}
		return strings;
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

	// TODO only used once -> inline
	public static List<LeafPsiElement> getSymbolDefiningPsiElements(PsiElement anchor) {
		Grammar grammar = getAncestor(anchor, Grammar.class);
		if (grammar == null) {
			return new ArrayList<>();
		}
		List<LeafPsiElement> result = new ArrayList<>();
		result.add(grammar.getTerminals().getFirstIdentifier().getIdentifier());
		for (TerminalDeclarations_1 more : grammar.getTerminals().getMoreIdentifiers().getAll()) {
			result.add(more.getIdentifier().getIdentifier());
		}
		for (Production production : grammar.getProductions().getAll()) {
			LeafPsiElement nonterminalName = getNonterminalNameNode(production);
			if (nonterminalName != null) {
				result.add(nonterminalName);
			}
		}
		return result;
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

	public static String getName(Production_SingleUnnamed node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_SingleUnnamed node, String newName) throws IncorrectOperationException {
		// rawReplaceWithText()? The docs don't tell the difference.
		return (LeafPsiElement) node.getNonterminalName().replaceWithText(newName);
	}

	public static String getName(Production_SingleNamed node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_SingleNamed node, String newName) throws IncorrectOperationException {
		return (LeafPsiElement) node.getNonterminalName().replaceWithText(newName);
	}

	public static String getName(Production_Multi node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_Multi node, String newName) throws IncorrectOperationException {
		return (LeafPsiElement) node.getNonterminalName().replaceWithText(newName);
	}

	public static String getName(Production_Error1 node) {
		return null;
	}

	public static String getName(Production_Error2 node) {
		return null;
	}

	public static String getName(Production_Error3 node) {
		return node.getNonterminalName().getText();
	}

	public static String getName(Production_Error4 node) {
		return node.getNonterminalName().getText();
	}

	public static PsiElement setName(Production_Error1 node, String newName) throws IncorrectOperationException {
		throw new IncorrectOperationException("grammar contains errors");
	}

	public static PsiElement setName(Production_Error2 node, String newName) throws IncorrectOperationException {
		throw new IncorrectOperationException("grammar contains errors");
	}

	public static PsiElement setName(Production_Error3 node, String newName) throws IncorrectOperationException {
		return (LeafPsiElement) node.getNonterminalName().replaceWithText(newName);
	}

	public static PsiElement setName(Production_Error4 node, String newName) throws IncorrectOperationException {
		return (LeafPsiElement) node.getNonterminalName().replaceWithText(newName);
	}

	//
	// safe delete
	//

	public static void delete(Production_SingleUnnamed node) {

	}

	public static void delete(Production_SingleNamed node) {

	}

	public static void delete(Production_Multi node) {

	}

	public static void delete(Production_Error1 node) {

	}

	public static void delete(Production_Error2 node) {

	}

	public static void delete(Production_Error3 node) {

	}

	public static void delete(Production_Error4 node) {

	}

}
