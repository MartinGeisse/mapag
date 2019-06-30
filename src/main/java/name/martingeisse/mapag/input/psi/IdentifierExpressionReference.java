package name.martingeisse.mapag.input.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import name.martingeisse.mapag.input.cm.Production;
import name.martingeisse.mapag.input.cm.TerminalDeclaration;
import name.martingeisse.mapag.input.cm.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IdentifierExpressionReference implements PsiReference {

	private final Expression_IdentifierImpl expressionPsi;

	public IdentifierExpressionReference(Expression_IdentifierImpl expressionPsi) {
		this.expressionPsi = expressionPsi;
	}

	@Override
	public PsiElement getElement() {
		return expressionPsi;
	}

	@Override
	public TextRange getRangeInElement() {
		return new TextRange(0, getCanonicalText().length());
	}

	@Nullable
	@Override
	public PsiElement resolve() {

		GrammarImpl grammar = PsiUtil.getAncestor(expressionPsi, GrammarImpl.class);
		if (grammar == null) {
			return null;
		}

		String identifier = expressionPsi.getIdentifier().getText();

		// terminals
		for (TerminalDeclarationImpl terminalDeclaration : grammar.getTerminalDeclarationsPsi().getIdentifiersPsi().getAllPsi()) {
			String terminalName = terminalDeclaration.getName();
			if (terminalName != null && terminalName.equals(identifier)) {
				return terminalDeclaration;
			}
		}

		// nonterminals / productions
		for (ProductionImpl production : grammar.getProductionsPsi().getAllPsi()) {
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
		return expressionPsi.getIdentifier().getText();
	}

	@Override
	public PsiElement handleElementRename(String newName) throws IncorrectOperationException {
		return PsiUtil.setText(expressionPsi.getIdentifierPsi(), newName);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {

		if (psiElement instanceof TerminalDeclarationImpl) {
			String newName = ((TerminalDeclarationImpl) psiElement).getName();
			if (newName != null) {
				return PsiUtil.setText(expressionPsi.getIdentifierPsi(), newName);
			}
		}

		if (psiElement instanceof ProductionImpl) {
			String newName = ((ProductionImpl) psiElement).getName();
			if (newName != null) {
				return PsiUtil.setText(expressionPsi.getIdentifierPsi(), newName);
			}
		}

		throw new IncorrectOperationException();
	}

	@Override
	public boolean isReferenceTo(PsiElement psiElement) {

		if (psiElement instanceof TerminalDeclarationImpl) {
			String terminalName = ((TerminalDeclarationImpl) psiElement).getName();
			if (terminalName != null) {
				String id = getCanonicalText();
				if (id.equals(terminalName)) {
					PsiElement resolved = resolve();
					return (resolved != null && resolved.equals(psiElement));
				}
			}
		}

		if (psiElement instanceof ProductionImpl) {
			String productionName = ((ProductionImpl) psiElement).getName();
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

		GrammarImpl grammar = PsiUtil.getAncestor(expressionPsi, GrammarImpl.class);
		if (grammar == null) {
			return new Object[0];
		}

		List<Object> variants = new ArrayList<>();

		// terminals
		for (TerminalDeclarationImpl more : grammar.getTerminalDeclarationsPsi().getIdentifiersPsi().getAllPsi()) {
			variants.add(more.getIdentifier().getText());
		}

		// nonterminals
		for (ProductionImpl production : grammar.getProductionsPsi().getAllPsi()) {
			PsiElement nameIdentifier = production.getNameIdentifier();
			if (nameIdentifier != null) {
				variants.add(nameIdentifier.getText());
			}
		}

		return variants.toArray();
	}

	@Override
	public boolean isSoft() {
		return false;
	}

}
