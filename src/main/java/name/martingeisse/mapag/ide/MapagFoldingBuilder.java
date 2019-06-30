package name.martingeisse.mapag.ide;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.input.cm.impl.Grammar_PrecedenceTableImpl;
import name.martingeisse.mapag.input.cm.impl.Grammar_TerminalDeclarationsImpl;
import name.martingeisse.mapag.input.cm.impl.ProductionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MapagFoldingBuilder implements FoldingBuilder {

	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode astNode, @NotNull Document document) {
		List<FoldingDescriptor> foldingDescriptors = new ArrayList<>();
		collectFoldingRegions(astNode.getPsi(), foldingDescriptors);
		return foldingDescriptors.toArray(new FoldingDescriptor[foldingDescriptors.size()]);
	}

	private void collectFoldingRegions(PsiElement psiElement, List<FoldingDescriptor> destination) {
		if (psiElement instanceof Grammar_TerminalDeclarationsImpl || psiElement instanceof Grammar_PrecedenceTableImpl || psiElement instanceof ProductionImpl) {
			if (psiElement.getNode() != null && psiElement.getTextRange() != null) {
				destination.add(new FoldingDescriptor(psiElement.getNode(), psiElement.getTextRange()));
			}
		}
		for (PsiElement child : psiElement.getChildren()) {
			collectFoldingRegions(child, destination);
		}
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode astNode) {
		PsiElement psiElement = astNode.getPsi();
		if (psiElement instanceof Grammar_TerminalDeclarationsImpl) {
			return "%terminals {...}";
		}
		if (psiElement instanceof Grammar_PrecedenceTableImpl) {
			return "%precedence {...}";
		}
		if (psiElement instanceof ProductionImpl) {
			return ((ProductionImpl) psiElement).getName() + " ::= ";
		} else {
			return psiElement.toString();
		}
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
		return false;
	}

}
