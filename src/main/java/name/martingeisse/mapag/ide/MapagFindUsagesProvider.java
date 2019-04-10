package name.martingeisse.mapag.ide;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import name.martingeisse.mapag.input.MapagLexer;
import name.martingeisse.mapag.input.Symbols;
import name.martingeisse.mapag.input.cm.impl.Expression_IdentifierImpl;
import name.martingeisse.mapag.input.cm.impl.ProductionImpl;
import name.martingeisse.mapag.input.cm.impl.TerminalDeclarationImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class MapagFindUsagesProvider implements FindUsagesProvider {

	@Nullable
	@Override
	public WordsScanner getWordsScanner() {
		return new DefaultWordsScanner(new MapagLexer(),
			TokenSet.create(Symbols.IDENTIFIER),
			TokenSet.create(Symbols.BLOCK_COMMENT, Symbols.LINE_COMMENT),
			TokenSet.create()
		);
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		if (psiElement instanceof TerminalDeclarationImpl) {
			return true;
		}
		if (psiElement instanceof ProductionImpl) {
			return true;
		}
		if (psiElement instanceof Expression_IdentifierImpl) {
			return true;
		}
		return false;
	}

	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement) {
		return null;
	}

	@NotNull
	@Override
	public String getType(@NotNull PsiElement psiElement) {
		return "symbol";
	}

	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement psiElement) {
		return getNodeText(psiElement, false);
	}

	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement psiElement, boolean useFullName) {
		if (psiElement instanceof TerminalDeclarationImpl) {
			String name = ((TerminalDeclarationImpl) psiElement).getName();
			if (name != null) {
				return name;
			} else {
				return psiElement.getText();
			}
		}
		if (psiElement instanceof ProductionImpl) {
			String name = ((ProductionImpl) psiElement).getName();
			if (name != null) {
				return name;
			} else {
				return psiElement.getText();
			}
		}
		if (psiElement instanceof Expression_IdentifierImpl) {
			return ((Expression_IdentifierImpl) psiElement).getIdentifier().getText();
		}
		return psiElement.getText();
	}

}
