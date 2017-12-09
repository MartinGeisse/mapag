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
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.ContainerUtil;
import name.martingeisse.mapag.input.Symbols;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 *
 */
public class MapagFormattingModelBuilder implements FormattingModelBuilder {

	// comments are not indented relative to their parent, which is usually indented itself
	private static final TokenSet COMMENT_SYMBOLS = TokenSet.create(Symbols.LINE_COMMENT, Symbols.BLOCK_COMMENT);

	// whitespace is needed for the workaround for comments
	private static final TokenSet WHITESPACE_SYMBOLS = TokenSet.create(TokenType.WHITE_SPACE);
	private static final TokenSet WHITESPACE_AND_COMMENT_SYMBOLS = TokenSet.orSet(WHITESPACE_SYMBOLS, COMMENT_SYMBOLS);

	private static final TokenSet DEFAULT_INDENTED_SYMBOLS = TokenSet.create(

		// list elements for which the list is already indented normally
		Symbols.precedenceDeclaration_Normal,
		Symbols.precedenceDeclaration_ErrorWithSemicolon,
		Symbols.precedenceDeclaration_ErrorWithoutSemicolon,
		Symbols.resolveDeclaration,

		// terminal list inside a precedence declaration
		Symbols.precedenceDeclaration_Normal_Terminals_List,
		Symbols.precedenceDeclarationSymbol

	);

	private static final TokenSet NORMALLY_INDENTED_SYMBOLS = TokenSet.create(
		Symbols.grammar_TerminalDeclarations_Identifiers_List,
		Symbols.grammar_PrecedenceTable_PrecedenceDeclarations_List,
		Symbols.production_Multi_Alternatives_List,
		Symbols.alternativeAttribute_ResolveBlock_ResolveDeclarations_List
	);

	private static final TokenSet NOT_INDENTED_SYMBOLS = TokenSet.create(

		// relative to its parent, a closing brace is never indented
		Symbols.CLOSING_CURLY_BRACE,

		// individual terminal declarations are not indented relative to their list, which in turn IS indented
		Symbols.terminalDeclaration,

		// top-level elements
		Symbols.grammar_TerminalDeclarations,
		Symbols.grammar_PrecedenceTable,
		Symbols.grammar_Productions_List,
		Symbols.KW_START,
		Symbols.production_SingleUnnamed,
		Symbols.production_SingleNamed,
		Symbols.production_Multi,
		Symbols.production_ErrorWithNonterminalNameWithSemicolon,
		Symbols.production_ErrorWithNonterminalNameWithClosingCurlyBrace,
		Symbols.production_ErrorWithNonterminalNameAtEof,
		Symbols.production_ErrorWithoutNonterminalNameWithSemicolon,
		Symbols.production_ErrorWithoutNonterminalNameWithClosingCurlyBrace,
		Symbols.production_ErrorWithoutNonterminalNameAtEof

	);

	@NotNull
	@Override
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
		MyBlock block = new MyBlock(element.getNode(), null);

		// TODO remove ------
		dump(block, 0);
		// TODO remove ------

		return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
	}

	private void dump(MyBlock block, int dumpIndent) {
		for (int i=0; i<dumpIndent; i++) {
			System.out.print("  ");
		}
		System.out.println(block.getNode().getElementType() + " -- indent: " + block.getIndent());
		for (Block child : block.getSubBlocks()) {
			dump((MyBlock)child, dumpIndent + 1);
		}
	}

	@Nullable
	@Override
	public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
		return null;
	}

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
			return new MyBlock(childNode, null);
		}

		@Override
		public Indent getIndent() {

			// TODO re-write when PSI is stable
			// TODO null indent means default indent -- check if this is sufficient
			// TODO wrap is "always", "never" or "if line is too long" -- soudns logical
			// TODO spacing says min/max spaces and whether line break. Can use SpacingBuilder.
			// Note the difference: line break == this belongs into the next line; wrap == line is too long
			// TODO alignment: align symbols in subsequent lines, e.g. colons in a JSON object. I hate that anyway.
			// TODO getChildAttributes(): Supports indentation at the moment the user breaks a line manually

			IElementType type = myNode.getElementType();
			if (COMMENT_SYMBOLS.contains(type)) {
				// Comment symbols are not handled by the parser, but rather implicitly attached to the AST produced
				// by the parser. This sometimes produces a wrong result, especially for comments in an AST list node:
				// in such a case, a comment before the first list node is wrongly attached outside of the list, and
				// thus not indented. We solve this by looking fir the first non-comment token to see if it is
				// normally indented.
				ASTNode node = getNode();
				while (node != null && WHITESPACE_AND_COMMENT_SYMBOLS.contains(node.getElementType())) {
					node = node.getTreeNext();
				}
				if (node != null && NORMALLY_INDENTED_SYMBOLS.contains(node.getElementType())) {
					return Indent.getNormalIndent();
				}
				return Indent.getNoneIndent();
			}
			if (DEFAULT_INDENTED_SYMBOLS.contains(type)) {
				return null;
			}
			if (NORMALLY_INDENTED_SYMBOLS.contains(type)) {
				return Indent.getNormalIndent();
			}
			if (NOT_INDENTED_SYMBOLS.contains(type)) {
				return Indent.getNoneIndent();
			}
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
