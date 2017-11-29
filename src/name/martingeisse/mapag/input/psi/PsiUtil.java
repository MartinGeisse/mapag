package name.martingeisse.mapag.input.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.PsiFileImpl;
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

	public static void delete(Production node) throws IncorrectOperationException {
		PsiFile psiFile = node.getContainingFile();
		if (!(psiFile instanceof PsiFileImpl)) {
			throw new IncorrectOperationException("production is not inside a PsiFileImpl");
		}
		node.superclassDelete();
		((PsiFileImpl)psiFile).onContentReload();

//		PsiElement parent = node.getParent();
//		if (parent instanceof Grammar_Productions_Start) {
//			Grammar_Productions_Start start = (Grammar_Productions_Start)parent;
//			PsiElement grandparent = parent.getParent();
//			if (grandparent instanceof Grammar_Productions_Next) {
//				Grammar_Productions_Next grandparentNext = (Grammar_Productions_Next)grandparent;
//				node.replace(grandparentNext.getElement());
//				grandparentNext.replace(parent);
//			} else if (grandparent instanceof Grammar) {
//				// this causes the grammar to be invalid because at least one production is mandatory
//				// TODO: make sure this gets flagged as an error marker
//				// TODO we don'T have an AST node! start.replace(new Production_Error2())
//			}
//		} else if (parent instanceof Grammar_Productions_Next) {
//			Grammar_Productions_Next next = (Grammar_Productions_Next)parent;
//			next.replace(next.getPrevious());
//		}
	}

}
