package name.martingeisse.mapag.codegen.old;

import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.ConfigurationException;
import name.martingeisse.mapag.codegen.MapagVelocityEngine;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class FrameworkClassGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "psi.package";

	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;

	public FrameworkClassGenerator(Configuration configuration, OutputFileFactory outputFileFactory) {
		this.configuration = configuration;
		this.outputFileFactory = outputFileFactory;
	}

	public void generate() throws ConfigurationException, IOException {
		generate("IElementType");
		generate("ASTNode");
		generate("PsiElement");
		generate("ASTWrapperPsiElement");
		generate("LeafPsiElement");
	}

	private void generate(String className) throws ConfigurationException, IOException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));

		try (OutputStream outputStream = outputFileFactory.createSourceFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), className)) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("templates/" + className + ".vm").merge(context, outputStreamWriter);
			}
		}

	}

}
