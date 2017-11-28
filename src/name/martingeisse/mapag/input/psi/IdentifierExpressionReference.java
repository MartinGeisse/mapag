package name.martingeisse.mapag.input.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
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
public class IdentifierExpressionReference implements PsiReference {

	private final Expression_Identifier expression;

	public IdentifierExpressionReference(Expression_Identifier expression) {
		this.expression = expression;
	}

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

		Grammar grammar = PsiUtil.getAncestor(expression, Grammar.class);
		if (grammar == null) {
			return null;
		}

		String identifier = expression.getIdentifier().getText();

		// TODO terminals
//				result.add(grammar.getTerminals().getFirstIdentifier());
//				for (NonemptyIdentifierList_1 more : grammar.getTerminals().getMoreIdentifiers().getAll()) {
//					result.add(more.getIdentifier());
//				}

		// nonterminals / productions
		for (Production production : grammar.getProductions().getAll()) {
			String nonterminalName = production.getName();
			if (nonterminalName != null && nonterminalName.equals(identifier)) {
				return production;
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
		return PsiUtil.setText(expression.getIdentifier(), newName);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
		// binding to terminals is currently not supported
		if (psiElement instanceof Production) {
			String newName = PsiUtil.getNonterminalName((Production) psiElement);
			if (newName != null) {
				return PsiUtil.setText(expression.getIdentifier(), newName);
			}
		}
		throw new IncorrectOperationException();
	}

	@Override
	public boolean isReferenceTo(PsiElement psiElement) {

		// TODO terminals

		if (psiElement instanceof Production) {
			String productionName = ((Production) psiElement).getName();
			if (productionName != null) {
				String id = getCanonicalText();
				if (id.equals(productionName)) {
					PsiElement resolved = resolve();
					return (resolved != null && resolved.equals(psiElement));
				}
			}
		}

		return false;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		// note: if this returns PSI elements, they must be PsiNamedElement or contain the name in meta-data
		List<Object> variants = new ArrayList<>();
		for (LeafPsiElement element : PsiUtil.getSymbolDefiningPsiElements(expression)) {
			variants.add(element.getText());
		}
		return variants.toArray();
	}

	@Override
	public boolean isSoft() {
		return false;
	}

}
