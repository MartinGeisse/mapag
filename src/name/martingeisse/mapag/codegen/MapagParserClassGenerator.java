package name.martingeisse.mapag.codegen;

import name.martingeisse.mapag.grammar.canonical.Grammar;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.sm.StateMachine;

/**
 *
 */
public class MapagParserClassGenerator {

	private final GrammarInfo grammarInfo;
	private final Grammar grammar;
	private final StateMachine stateMachine;

	public MapagParserClassGenerator(GrammarInfo grammarInfo, StateMachine stateMachine) {
		this.grammarInfo = grammarInfo;
		this.grammar = grammarInfo.getGrammar();
		this.stateMachine = stateMachine;
	}

	public void generate() {
		println("package " + grammar.getPackageName() + ";\n");
		println();
		println("class " + grammar.getClassName() + " {");
		println("}");
	}

	private void print(Object o) {
		System.out.print(o);
	}

	private void println(Object o) {
		System.out.println(o);
	}

	private void println() {
		System.out.println();
	}

}
