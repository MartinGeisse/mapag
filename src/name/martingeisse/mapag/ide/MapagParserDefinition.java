package name.martingeisse.mapag.ide;

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
		return new MapagParser();
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
		return TokenSet.create(MapagSpecificationElementTypes.COMMENT);
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements() {
		return TokenSet.EMPTY;
	}

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node) {
		return new ASTWrapperPsiElement(node);
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
