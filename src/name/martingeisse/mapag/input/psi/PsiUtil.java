package name.martingeisse.mapag.input.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	public static String getNonterminalName(Production production) {
		PsiElement element = getNonterminalNameNode(production);
		return (element == null ? null : element.getText());
	}

	public static LeafPsiElement getNonterminalNameNode(Production production) {
		if (production instanceof Production_SingleUnnamed) {
			return ((Production_SingleUnnamed) production).getNonterminalName();
		} else if (production instanceof Production_SingleNamed) {
			return ((Production_SingleNamed) production).getNonterminalName();
		} else if (production instanceof Production_Multi) {
			return ((Production_Multi) production).getNonterminalName();
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

	public static List<LeafPsiElement> getSymbolDefiningPsiElements(PsiElement anchor) {
		Grammar grammar = getAncestor(anchor, Grammar.class);
		if (grammar == null) {
			return new ArrayList<>();
		}
		List<LeafPsiElement> result = new ArrayList<>();
		result.add(grammar.getTerminals().getFirstIdentifier());
		for (NonemptyIdentifierList_1 more : grammar.getTerminals().getMoreIdentifiers().getAll()) {
			result.add(more.getIdentifier());
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
		return new PsiReference() {

			@Override
			public PsiElement getElement() {
				return expression;
			}

			@Override
			public TextRange getRangeInElement() {
				return new TextRange(0, getCanonicalText().length());
			}

			@Nullable
			@Override
			public PsiElement resolve() {
				String id = getCanonicalText();
				for (LeafPsiElement element : getSymbolDefiningPsiElements(expression)) {
					if (id.equals(element.getText())) {
						return element;
					}
				}
				return null;
			}

			@NotNull
			@Override
			public String getCanonicalText() {
				return expression.getIdentifier().getText();
			}

			@Override
			public PsiElement handleElementRename(String newName) throws IncorrectOperationException {
				return setText(expression.getIdentifier(), newName);
			}

			@Override
			public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
				// binding to terminals is currently not supported
				if (psiElement instanceof Production) {
					String newName = getNonterminalName((Production) psiElement);
					if (newName != null) {
						return setText(expression.getIdentifier(), newName);
					}
				}
				throw new IncorrectOperationException();
			}

			@Override
			public boolean isReferenceTo(PsiElement psiElement) {
				if (psiElement instanceof LeafPsiElement) {
					String id = getCanonicalText();
					if (id.equals(psiElement.getText())) {
						return getSymbolDefiningPsiElements(expression).contains(psiElement);
					}
				}
				return false;
			}

			@NotNull
			@Override
			public Object[] getVariants() {
				// note: if this returns PSI elements, they must be PsiNamedElement or contain the name in meta-data
				// TODO: if the input file contains syntax errors then this methods doesn't get called!
				List<Object> variants = new ArrayList<>();
				for (LeafPsiElement element : getSymbolDefiningPsiElements(expression)) {
					variants.add(element.getText());
				}
				return variants.toArray();
			}

			@Override
			public boolean isSoft() {
				return false;
			}

		};
	}

	public static PsiReference getReference(Production_SingleUnnamed expression) {
		return null;
	}

	public static PsiReference getReference(Production_SingleNamed expression) {
		return null;
	}

	public static PsiReference getReference(Production_Multi expression) {
		return null;
	}

	public static PsiReference getReference(Production_Error expression) {
		return null;
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

	public static String getName(Production_Error node) {
		return null;
	}

	public static PsiElement setName(Production_Error node, String newName) throws IncorrectOperationException {
		throw new IncorrectOperationException("grammar contains errors");
	}

}
