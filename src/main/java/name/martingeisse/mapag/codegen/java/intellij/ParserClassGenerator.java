package name.martingeisse.mapag.codegen.java.intellij;

import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.java.AbstractParserClassGenerator;
import org.apache.velocity.VelocityContext;

/**
 *
 */
public class ParserClassGenerator extends AbstractParserClassGenerator {

	public static final String FILE_ELEMENT_TYPE_PROPERTY = "parser.fileElementType";

	private final CodeGenerationParameters parameters;

	public ParserClassGenerator(CodeGenerationParameters parameters) {
		super(parameters);
		this.parameters = parameters;
	}

	@Override
	protected String getTemplateName() {
		return "templates/intellij/Parser.vm";
	}

	@Override
	protected void configureVelocityContext(VelocityContext context) {
		context.put("intellij", true);
		context.put("fileElementType", parameters.getConfiguration().getRequired(FILE_ELEMENT_TYPE_PROPERTY));
	}

}
