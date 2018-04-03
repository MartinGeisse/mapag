package name.martingeisse.mapag.codegen;

/**
 * This class defines the difference between a standalone parser and an IntelliJ integrated parser.
 */
public interface CodeGenerationContext {

	boolean isIntellij();

	String getNotNullAnnotation();

}
