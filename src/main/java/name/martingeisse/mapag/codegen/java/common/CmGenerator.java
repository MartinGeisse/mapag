package name.martingeisse.mapag.codegen.java.common;

import name.martingeisse.mapag.codegen.*;
import name.martingeisse.mapag.codegen.java.IdentifierUtil;
import name.martingeisse.mapag.codegen.java.JavaPropertyNames;
import name.martingeisse.mapag.grammar.canonical.*;
import name.martingeisse.mapag.util.Comparators;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CmGenerator {

	private final Grammar grammar;
	private final Configuration configuration;
	private final OutputFileFactory outputFileFactory;

	public CmGenerator(CodeGenerationParameters codeGenerationParameters) {
		this.grammar = codeGenerationParameters.getGrammarInfo().getGrammar();
		this.configuration = codeGenerationParameters.getConfiguration();
		this.outputFileFactory = codeGenerationParameters.getOutputFileFactory();
	}

	public void generate() throws ConfigurationException, IOException {
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			handleNonterminal(nonterminalDefinition);
		}
	}

	private void handleNonterminal(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		if (nonterminalDefinition.getPsiStyle() instanceof PsiStyle.Normal) {
			if (nonterminalDefinition.getAlternatives().size() == 1) {
				generateSingleAlternative(nonterminalDefinition, nonterminalDefinition.getAlternatives().get(0));
			} else {
				generateMultiAlternativeBase(nonterminalDefinition);
				for (Alternative alternative : nonterminalDefinition.getAlternatives()) {
					generateMultiAlternativeCase(nonterminalDefinition, alternative);
				}
			}
		}
	}

	private void generateSingleAlternative(NonterminalDefinition nonterminalDefinition, Alternative alternative) throws ConfigurationException, IOException {
		TypeGenerator generator = new TypeGenerator();
		generator.name = IdentifierUtil.getAlternativeTypeIdentifier(nonterminalDefinition, alternative);
		generator.cmSuperInterface = "CmNode";
		generator.alternative = alternative;
		generator.generate();
	}

	private void generateMultiAlternativeBase(NonterminalDefinition nonterminalDefinition) throws ConfigurationException, IOException {
		TypeGenerator generator = new TypeGenerator();
		generator.name = IdentifierUtil.getNonterminalTypeIdentifier(nonterminalDefinition);
		generator.cmSuperInterface = "CmNode";
		generator.alternative = null;
		generator.generate();
	}

	private void generateMultiAlternativeCase(NonterminalDefinition nonterminalDefinition, Alternative alternative) throws ConfigurationException, IOException {
		TypeGenerator generator = new TypeGenerator();
		generator.name = IdentifierUtil.getAlternativeTypeIdentifier(nonterminalDefinition, alternative);
		generator.cmSuperInterface = IdentifierUtil.getNonterminalTypeIdentifier(nonterminalDefinition);
		generator.alternative = alternative;
		generator.generate();
	}

	private class TypeGenerator {

		String name;
		String cmSuperInterface;
		Alternative alternative;

		void generate() throws ConfigurationException, IOException {

			// determine full package name
			String packageName = configuration.getRequired(JavaPropertyNames.BASE_PACKAGE) + ".cm";

			//
			VelocityContext context = new VelocityContext();
			context.put("packageName", packageName);
			context.put("name", name);

			List<NodeGetter> nodeGetters = new ArrayList<>();
			if (alternative != null) {
				int childIndex = 0;
				for (ExpansionElement element : alternative.getExpansion().getElements()) {
					String expressionName = element.getExpressionName();
					if (expressionName != null) {
						NodeGetter nodeGetter = new NodeGetter();
						nodeGetter.childIndex = childIndex;
						nodeGetter.nodeType = TypeSelectionUtil.getEffectiveTypeForSymbol(grammar, element.getSymbol());
						nodeGetter.getterName = "get" + StringUtils.capitalize(expressionName);
						nodeGetters.add(nodeGetter);
					}
					childIndex++;
				}
			}
			context.put("nodeGetters", nodeGetters);

			// determine super-interfaces
			{
				List<String> interfaces = new ArrayList<>();
				interfaces.add(cmSuperInterface);
				String extraInterfacesKey = "cm.interfaces." + name;
				String extraInterfacesText = configuration.getOptional(extraInterfacesKey);
				if (extraInterfacesText != null) {
					for (String extraInterfaceText : StringUtils.split(extraInterfacesText, ',')) {
						interfaces.add(extraInterfaceText.trim());
					}
				}
				if (interfaces.isEmpty()) {
					context.put("extends", "");
				} else {
					context.put("extends", "extends " + StringUtils.join(interfaces, ','));
				}

			}

			try (OutputStream outputStream = outputFileFactory.createSourceFile(packageName, name)) {
				try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
					MapagVelocityEngine.engine.getTemplate("templates/common/Cm.vm").merge(context, outputStreamWriter);
				}
			}

		}

	}

	public class NodeGetter {

		int childIndex;
		String nodeType;
		String getterName;

		public int getChildIndex() {
			return childIndex;
		}

		public String getNodeType() {
			return nodeType;
		}

		public String getGetterName() {
			return getterName;
		}

	}

}
