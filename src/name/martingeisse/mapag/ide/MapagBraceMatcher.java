package name.martingeisse.mapag.ide;

import com.intellij.codeInsight.hint.DeclarationRangeUtil;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import name.martingeisse.mapag.input.Symbols;
import name.martingeisse.mapag.input.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class MapagBraceMatcher implements PairedBraceMatcher {

	@NotNull
	@Override
	public BracePair[] getPairs() {
		return new BracePair[] {
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
		if (parent instanceof Grammar) {
			// this happens only for the terminal list
			return InternalPsiUtil.getChild((Grammar)parent, 0).getTextRange().getStartOffset();
		}
		if (parent instanceof Grammar_PrecedenceTable) {
			return parent.getTextRange().getStartOffset();
		}
		if (parent instanceof Production_Multi) {
			return parent.getTextRange().getStartOffset();
		}
		if (parent instanceof AlternativeAttribute_ResolveBlock) {
			return parent.getTextRange().getStartOffset();
		}
		return openingBraceOffset;
	}

}
