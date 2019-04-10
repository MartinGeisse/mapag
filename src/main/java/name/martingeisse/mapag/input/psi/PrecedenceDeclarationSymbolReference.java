package name.martingeisse.mapag.input.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import name.martingeisse.mapag.input.cm.TerminalDeclaration;
import name.martingeisse.mapag.input.cm.impl.GrammarImpl;
import name.martingeisse.mapag.input.cm.impl.PrecedenceDeclarationSymbolImpl;
import name.martingeisse.mapag.input.cm.impl.TerminalDeclarationImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PrecedenceDeclarationSymbolReference implements PsiReference {

	private final PrecedenceDeclarationSymbolImpl precedenceDeclarationSymbolPsi;

	public PrecedenceDeclarationSymbolReference(PrecedenceDeclarationSymbolImpl precedenceDeclarationSymbolPsi) {
		this.precedenceDeclarationSymbolPsi = precedenceDeclarationSymbolPsi;
	}

	@Override
	public PsiElement getElement() {
		return precedenceDeclarationSymbolPsi;
	}

	@Override
	public TextRange getRangeInElement() {
		return new TextRange(0, getCanonicalText().length());
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		GrammarImpl grammar = PsiUtil.getAncestor(precedenceDeclarationSymbolPsi, GrammarImpl.class);
		if (grammar == null) {
			return null;
		}
		String identifier = precedenceDeclarationSymbolPsi.getIdentifier().getText();
		for (TerminalDeclaration terminalDeclaration : grammar.getTerminalDeclarations().getIdentifiers().getAll()) {
			TerminalDeclarationImpl terminalDeclarationImpl = (TerminalDeclarationImpl) terminalDeclaration;
			String terminalName = terminalDeclarationImpl.getName();
			if (terminalName != null && terminalName.equals(identifier)) {
				return terminalDeclarationImpl;
			}
		}
		return null;
	}

	@NotNull
	@Override
	public String getCanonicalText() {
		return precedenceDeclarationSymbolPsi.getIdentifier().getText();
	}

	@Override
	public PsiElement handleElementRename(String newName) throws IncorrectOperationException {
		return PsiUtil.setText(precedenceDeclarationSymbolPsi.getIdentifierPsi(), newName);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
		if (psiElement instanceof TerminalDeclarationImpl) {
			String newName = ((TerminalDeclarationImpl) psiElement).getName();
			if (newName != null) {
				return PsiUtil.setText(precedenceDeclarationSymbolPsi.getIdentifierPsi(), newName);
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
		return false;
	}

	// note: if this returns PSI elements, they must be PsiNamedElement or contain the name in meta-data
	@NotNull
	@Override
	public Object[] getVariants() {
		GrammarImpl grammar = PsiUtil.getAncestor(precedenceDeclarationSymbolPsi, GrammarImpl.class);
		if (grammar == null) {
			return new Object[0];
		}
		List<Object> variants = new ArrayList<>();
		for (TerminalDeclaration more : grammar.getTerminalDeclarations().getIdentifiers().getAll()) {
			variants.add(more.getIdentifier().getText());
		}
		return variants.toArray();
	}

	@Override
	public boolean isSoft() {
		return false;
	}

}
