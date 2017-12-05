package name.martingeisse.mapag.ide;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import name.martingeisse.mapag.input.Symbols;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 *
 */
public class MapagFormattingModelBuilder implements FormattingModelBuilder {

	@NotNull
	@Override
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {


		MyBlock block = new MyBlock(element.getNode(), null);
		return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
	}

	@Nullable
	@Override
	public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
		return null;
	}

	// TODO null indent means default indent -- check if this is sufficient
	// TODO wrap is "always", "never" or "if line is too long" -- soudns logical
	// TODO spacing says min/max spaces and whether line break. Can use SpacingBuilder.
	// Note the difference: line break == this belongs into the next line; wrap == line is too long
	// TODO alignment: align symbols in subsequent lines, e.g. colons in a JSON object. I hate that anyway.
	// TODO getChildAttributes(): Supports indentation at the moment the user breaks a line manually
	public static class MyBlock extends AbstractBlock {

		public MyBlock(@NotNull ASTNode node, @Nullable Wrap wrap) {
			super(node, wrap, null);
		}

		@Override
		protected List<Block> buildChildren() {
			return ContainerUtil.mapNotNull(myNode.getChildren(null), node -> {
				if (node.getElementType() == TokenType.WHITE_SPACE || node.getTextLength() == 0) {
					return null;
				} else {
					return buildChild(node);
				}
			});
		}

		private Block buildChild(ASTNode childNode) {
			// TODO choose indent, spacing, wrap
			return new MyBlock(childNode, null);
		}

		@Override
		public Indent getIndent() {

			// TODO re-write when PSI is stable

//			IElementType type = myNode.getElementType();
//			IElementType parentType = (myNode.getTreeParent() == null ? null : myNode.getTreeParent().getElementType());
//
//			// relative to its parent, a closing brace is never indented
//			if (type == Symbols.CLOSING_CURLY_BRACE) {
//				return Indent.getNoneIndent();
//			}
//
//			// indent terminals inside their curly-braces block by one level, but no further indentation of terminals
//			// inside the terminal list (specifcally, no continuation indent -- the braces already delimit that list).
//			if (type == Symbols.terminalDeclarations) {
//				return Indent.getNormalIndent();
//			}
//			if (isInside(myNode, Symbols.terminalDeclaration)) {
//				return Indent.getNoneIndent();
//			}
//
//			// Indent the precedence table inside its braces. Unlike the terminals, this table is separated into lines,
//			// so continuation indent is okay.
//			if (type == Symbols.grammar_PrecedenceTable) {
//				return Indent.getNormalIndent();
//			}
//			if (isInside(myNode, Symbols.grammar_PrecedenceTable)) {
//				return null;
//			}
//
//			// Indent the resolve declarations inside a resolve block, but not their list nodes
//			if (type == Symbols.resolveDeclaration) {
//				return Indent.getNormalIndent();
//			}
//			if (type == Symbols.alternativeAttribute_ResolveBlock_ResolveDeclarations_Start || type == Symbols.alternativeAttribute_ResolveBlock_ResolveDeclarations_Next) {
//				return Indent.getNoneIndent();
//			}
//
//			// don't indent any top-level elements (including productions and production list nodes)
//			if (parentType == Symbols.grammar || parentType == Symbols.grammar_Productions_Start || parentType == Symbols.grammar_Productions_Next) {
//				return Indent.getNoneIndent();
//			}
//
//			// indent the alternatives within a multi-alternative production, but not their list nodes
//			if (type == Symbols.production_Multi_Alternatives_1_Named || type == Symbols.production_Multi_Alternatives_1_Unnamed) {
//				return Indent.getNormalIndent();
//			}
//			if (type == Symbols.production_Multi_Alternatives_Start || type == Symbols.production_Multi_Alternatives_Next) {
//				return Indent.getNoneIndent();
//			}

			return null;
		}

		private boolean isInside(ASTNode node, IElementType type) {
			while (node != null) {
				if (node.getElementType() == type) {
					return true;
				}
				node = node.getTreeParent();
			}
			return false;
		}

		@Nullable
		@Override
		public Spacing getSpacing(@Nullable Block block, @NotNull Block block1) {
			// TODO
			return null;
		}

		@Override
		public boolean isLeaf() {
			return myNode.getFirstChildNode() == null;
		}

	}
}
