package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.NonterminalDefinition;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.util.Comparators;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PsiClassesGenerator {

	public static final String PACKAGE_NAME_PROPERTY = "psi.package";

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final Configuration configuration;
	private int alternativeCounter;

	public PsiClassesGenerator(GrammarInfo grammarInfo, Configuration configuration) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.configuration = configuration;
	}

	public void generate() throws ConfigurationException {
		for (NonterminalDefinition nonterminalDefinition : grammar.getNonterminalDefinitions().values()) {
			if (nonterminalDefinition.getAlternatives().size() == 1) {
				generateSingleAlternativeClass(nonterminalDefinition.getName(), nonterminalDefinition.getAlternatives().get(0));
			} else {
				generateMultiAlternativeBaseClass(nonterminalDefinition.getName());
				List<Alternative> sortedAlternatives = new ArrayList<>(nonterminalDefinition.getAlternatives());
				sortedAlternatives.sort(Comparators.alternativeComparator);
				alternativeCounter = 0;
				for (Alternative alternative : sortedAlternatives) {
					generateMultiAlternativeCaseClass(nonterminalDefinition.getName(), alternative);
					alternativeCounter++;
				}
			}
		}
		generateFactoryClass();
	}

	private void generateSingleAlternativeClass(String nonterminalName, Alternative alternative) throws ConfigurationException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = toIdentifier(nonterminalName, true);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = false;
		classGenerator.generate();
	}

	private void generateMultiAlternativeBaseClass(String nonterminalName) throws ConfigurationException {
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = toIdentifier(nonterminalName, true);
		classGenerator.superclass = "ASTWrapperPsiElement";
		classGenerator.isAbstract = true;
		classGenerator.generate();
	}

	private void generateMultiAlternativeCaseClass(String nonterminalName, Alternative alternative) throws ConfigurationException {
		String alternativeName = (alternative.getAnnotation().getAlternativeName() == null ? Integer.toString(alternativeCounter) : alternative.getAnnotation().getAlternativeName());
		PsiClassGenerator classGenerator = new PsiClassGenerator();
		classGenerator.className = toIdentifier(nonterminalName + '/' + alternativeName, true);
		classGenerator.superclass = toIdentifier(nonterminalName, true);
		classGenerator.isAbstract = false;
		classGenerator.generate();
	}

	private class PsiClassGenerator {

		String className;
		String superclass;
		boolean isAbstract;

		void generate() throws ConfigurationException {

			VelocityContext context = new VelocityContext();
			context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));
			context.put("className", className);
			context.put("superclass", superclass);
			context.put("classModifiers", isAbstract ? "abstract" : "final");

			StringWriter sw = new StringWriter();
			MapagVelocityEngine.engine.getTemplate("PsiClass.vm").merge(context, sw);
			System.out.println(sw);

		}

	}

	private void generateFactoryClass() throws ConfigurationException {

		VelocityContext context = new VelocityContext();
		context.put("packageName", configuration.getRequired(PACKAGE_NAME_PROPERTY));

		StringWriter sw = new StringWriter();
		MapagVelocityEngine.engine.getTemplate("PsiFactory.vm").merge(context, sw);
		System.out.println(sw);

	}

	private static String toIdentifier(String s, boolean firstCharacterUppercase) {
		StringBuilder builder = new StringBuilder();
		boolean firstValidCharacter = true;
		boolean forceNextCharacterCase = true;
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				if (forceNextCharacterCase) {
					if (firstCharacterUppercase || !firstValidCharacter) {
						c = Character.toUpperCase(c);
					} else {
						c = Character.toLowerCase(c);
					}
				}
				builder.append(c);
				firstValidCharacter = false;
				forceNextCharacterCase = false;
			} else if (c >= '0' && c <= '9') {
				if (firstValidCharacter) {
					builder.append('_');
				}
				builder.append(c);
				firstValidCharacter = false;
				forceNextCharacterCase = false;
			} else if (c == '/') {
				builder.append('_');
				forceNextCharacterCase = true;
			} else {
				forceNextCharacterCase = true;
			}
		}
		return builder.toString();
	}

}
