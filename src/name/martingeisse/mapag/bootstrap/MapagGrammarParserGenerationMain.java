package name.martingeisse.mapag.bootstrap;

import name.martingeisse.mapag.codegen.MapagParserClassGenerator;

/**
 *
 */
public class MapagGrammarParserGenerationMain {

	public static void main(String[] args) {
		MapagParserClassGenerator mapagParserClassGenerator = new MapagParserClassGenerator();
		mapagParserClassGenerator.setPackageName("foobar");
		mapagParserClassGenerator.setClassName("FooBar");
		mapagParserClassGenerator.generate();
	}

}
