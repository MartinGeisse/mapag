package name.martingeisse.mapag.codegen.java.standalone;

import name.martingeisse.mapag.codegen.CodeGenerationParameters;
import name.martingeisse.mapag.codegen.java.AbstractParserClassGenerator;
import org.apache.velocity.VelocityContext;

/**
 *
 */
public class ParserClassGenerator extends AbstractParserClassGenerator {

	private final CodeGenerationParameters parameters;

	public ParserClassGenerator(CodeGenerationParameters parameters) {
		super(parameters);
		this.parameters = parameters;
	}

	@Override
	protected String getTemplateName() {
		return "templates/standalone/Parser.vm";
	}

	@Override
	protected void configureVelocityContext(VelocityContext context) {
		context.put("intellij", false);
	}

}
