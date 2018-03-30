package name.martingeisse.mapag.bootstrap;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.codegen.intellij.CodeGenerationDriver;
import name.martingeisse.mapag.codegen.Configuration;
import name.martingeisse.mapag.codegen.OutputFileFactory;
import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.grammar.extended.Grammar;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.TerminalDeclaration;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This is the bootstrapper for the calculator grammar (test project). This should ultimately be defined in a mapag
 * specification file, but for now, we don't actually have a parser for these specification files.
 */
public class CalculatorParserGenerationMain extends BootstrapBase {

	public static void main(String[] args) throws Exception {

		Properties codeGenerationProperties = new Properties();
		codeGenerationProperties.setProperty("parser.package", "name.martingeisse.calculator");
		codeGenerationProperties.setProperty("parser.class", "MapagGeneratedCalculationParser");
		codeGenerationProperties.setProperty("parser.fileElementType", "CalculatorParserDefinition.FILE_ELEMENT_TYPE");
		codeGenerationProperties.setProperty("symbolHolder.generate", "true");
		codeGenerationProperties.setProperty("symbolHolder.package", "name.martingeisse.calculator");
		codeGenerationProperties.setProperty("symbolHolder.class", "Symbols");
		codeGenerationProperties.setProperty("symbol.elementType.class", "CalculatorElementType");
		codeGenerationProperties.setProperty("psi.generate", "true");
		codeGenerationProperties.setProperty("psi.package", "name.martingeisse.calculator.psi");
		Configuration configuration = new Configuration(codeGenerationProperties);

		ImmutableList<TerminalDeclaration> terminalDeclarations = ImmutableList.of(
			new TerminalDeclaration("PLUS"),
			new TerminalDeclaration("MINUS"),
			new TerminalDeclaration("TIMES"),
			new TerminalDeclaration("DIVIDED_BY"),
			new TerminalDeclaration("OPENING_PARENTHESIS"),
			new TerminalDeclaration("CLOSING_PARENTHESIS"),
			new TerminalDeclaration("SEMICOLON"),
			new TerminalDeclaration("IDENTIFIER"),
			new TerminalDeclaration("NUMBER"),
			new TerminalDeclaration("BLOCK_COMMENT"), // never passed to the parser
			new TerminalDeclaration("LINE_COMMENT") // never passed to the parser
		);

		PrecedenceTable precedenceTable = new PrecedenceTable(ImmutableList.of(
			new PrecedenceTable.Entry(ImmutableList.of("PLUS", "MINUS"), Associativity.LEFT),
			new PrecedenceTable.Entry(ImmutableList.of("TIMES", "DIVIDED_BY"), Associativity.LEFT)
		));

		String startNonterminalName = "calculation";

		ImmutableList<Production> productions = ImmutableList.of(
			new Production("calculation", ImmutableList.of(
				alternative(null, zeroOrMore(symbol("statement")).withName("statements"))
			)),
			new Production("statement", ImmutableList.of(
				alternative("expression", sequence(symbol("expression").withName("expression"), symbol("SEMICOLON"))),
				alternative("error", sequence(symbol("%error"), symbol("SEMICOLON")))
			)),
			new Production("expression", ImmutableList.of(
				alternative("literal", symbol("NUMBER").withName("value")),
				alternative("variable", symbol("IDENTIFIER").withName("variableName")),
				alternativeWithPrecedence("additive", sequence(
					symbol("expression").withName("left"),
					or(symbol("PLUS"), symbol("MINUS")).withName("operator"),
					symbol("expression").withName("right")
				), "PLUS"),
				alternativeWithPrecedence("multiplicative", sequence(
					symbol("expression").withName("left"),
					or(symbol("TIMES"), symbol("DIVIDED_BY")).withName("operator"),
					symbol("expression").withName("right")
				), "TIMES"),
				alternative("parenthesized", sequence(symbol("OPENING_PARENTHESIS"), symbol("expression").withName("inner"), symbol("CLOSING_PARENTHESIS")))
			))
		);

		Grammar grammar = new Grammar(terminalDeclarations, precedenceTable, startNonterminalName, productions);
		GrammarInfo grammarInfo = new GrammarInfo(new GrammarCanonicalizer(grammar).run().getResult());
		StateMachine stateMachine = new StateMachineBuilder(grammarInfo).build();
		OutputFileFactory outputFileFactory = new OutputFileFactory() {

			@Override
			public OutputStream createSourceFile(String packageName, String className) throws IOException {
				return createFile(packageName, className + ".java");
			}

			@Override
			public OutputStream createResourceFile(String filename) throws IOException {
				return createFile("resources", filename);
			}

			private OutputStream createFile(String folderName, String fileName) throws IOException {
				File baseFolder = new File("grammar_gen_out");
				if (!baseFolder.isDirectory()) {
					baseFolder.mkdir();
				}
				File packageFolder = new File(baseFolder, folderName);
				if (!packageFolder.isDirectory()) {
					packageFolder.mkdir();
				}
				return new FileOutputStream(new File(packageFolder, fileName));
			}

		};
		new CodeGenerationDriver(grammarInfo, stateMachine, configuration, outputFileFactory).generate();

	}

}
