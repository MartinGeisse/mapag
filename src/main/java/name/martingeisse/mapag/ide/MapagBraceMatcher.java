package name.martingeisse.mapag.ide;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import name.martingeisse.mapag.input.Symbols;
import name.martingeisse.mapag.input.cm.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class MapagBraceMatcher implements PairedBraceMatcher {

	@NotNull
	@Override
	public BracePair[] getPairs() {
		return new BracePair[]{
			new BracePair(Symbols.OPENING_CURLY_BRACE, Symbols.CLOSING_CURLY_BRACE, true),
			new BracePair(Symbols.OPENING_PARENTHESIS, Symbols.CLOSING_PARENTHESIS, false),
		};
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
		return true;
	}

	public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		PsiElement element = file.findElementAt(openingBraceOffset);
		if (element == null || element instanceof PsiFile) {
			return openingBraceOffset;
		}
		PsiElement parent = element.getParent();
		if (parent instanceof GrammarImpl) {
			// this happens only for the terminal list
			return InternalPsiUtil.getChild((GrammarImpl) parent, 0).getTextRange().getStartOffset();
		}
		if (parent instanceof Grammar_PrecedenceTableImpl) {
			return parent.getTextRange().getStartOffset();
		}
		if (parent instanceof Production_MultiImpl) {
			return parent.getTextRange().getStartOffset();
		}
		if (parent instanceof AlternativeAttribute_ResolveBlockImpl) {
			return parent.getTextRange().getStartOffset();
		}
		return openingBraceOffset;
	}

}
