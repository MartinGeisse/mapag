package name.martingeisse.mapag.codegen;

/**
 * This class defines the difference between a standalone parser and an IntelliJ integrated parser.
 */
public interface CodeGenerationContext {

	// TODO refactor this to be more abstract
	boolean isIntellij();

	String getNotNullAnnotation();

	CodeGenerationDriver createDriver(CodeGenerationParameters parameters);

}
