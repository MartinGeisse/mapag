package name.martingeisse.mapag.codegen.standalone;

import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.ListUtil;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		generate("LeafPsiElement");
	}

	private void generate(String className) throws ConfigurationException, IOException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));

		try (OutputStream outputStream = outputFileFactory.createSourceFile(configuration.getRequired(PACKAGE_NAME_PROPERTY), className)) {
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
				MapagVelocityEngine.engine.getTemplate("standalone/" + className + ".vm").merge(context, outputStreamWriter);
			}
		}

	}

}
