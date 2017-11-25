package name.martingeisse.mapag.ide;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import name.martingeisse.mapag.input.MapagLexer;
import name.martingeisse.mapag.input.psi.PsiFactory;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagParserDefinition implements ParserDefinition {

	public static final IFileElementType FILE_ELEMENT_TYPE = new IFileElementType(MapagSpecificationLanguage.INSTANCE);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new MapagLexer();
	}

	@Override
	public PsiParser createParser(Project project) {
		return new MapagGeneratedMapagParser();
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE_ELEMENT_TYPE;
	}

	@NotNull
	@Override
	public TokenSet getWhitespaceTokens() {
		return TokenSet.create(TokenType.WHITE_SPACE);
	}

	@NotNull
	@Override
	public TokenSet getCommentTokens() {
		return TokenSet.create(Symbols.BLOCK_COMMENT, Symbols.LINE_COMMENT);
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements() {
		return TokenSet.EMPTY;
	}

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node) {
		return PsiFactory.createPsiElement(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new MapagSourceFile(viewProvider);
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

}
