package name.martingeisse.mapag.input;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.ide.MapagSourceFile;
import name.martingeisse.mapag.input.psi.Grammar_PrecedenceTable;
import name.martingeisse.mapag.input.psi.NonemptyIdentifierList;
import name.martingeisse.mapag.input.psi.NonemptyIdentifierList_1;
import name.martingeisse.mapag.input.psi.PrecedenceDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Converts the PSI to an extended grammar.
 */
public class PsiToGrammarConverter {

	public Grammar convert(MapagSourceFile mapagSourceFile) {
		name.martingeisse.mapag.input.psi.Grammar psiGrammar = mapagSourceFile.getGrammar();
		if (psiGrammar == null) {
			throw new RuntimeException("could not find grammar PSI node");
		}
		return convert(psiGrammar);
	}

	public Grammar convert(name.martingeisse.mapag.input.psi.Grammar psiGrammar) {

		ImmutableList<TerminalDeclaration> terminalDeclarations =
			convertIdentifiers(psiGrammar.getTerminals(), TerminalDeclaration::new);

		ImmutableList<NonterminalDeclaration> nonterminalDeclarations =
			convertIdentifiers(psiGrammar.getNonterminals(), NonterminalDeclaration::new);

		PrecedenceTable precedenceTable =
			convertPrecedenceTable(psiGrammar.getPrecedenceTable());

		String startSymbol = getText(psiGrammar.getStartSymbolName());

		ImmutableList<Production> productions = null; // TODO

		return new Grammar(terminalDeclarations, nonterminalDeclarations, precedenceTable, startSymbol, productions);
	}

	private PrecedenceTable convertPrecedenceTable(Grammar_PrecedenceTable psiPrecedenceTable) {
		if (psiPrecedenceTable.getIt() == null) {
			return null;
		}
		List<PrecedenceTable.Entry> convertedEntries = new ArrayList<>();
		for (PrecedenceDeclaration precedenceDeclaration : psiPrecedenceTable.getIt().getPrecedenceDeclarations().getAll()) {
			// TODO precedenceDeclaration.getAssociativity().
		}
		return new PrecedenceTable(ImmutableList.copyOf(convertedEntries));
	}

	private <T> ImmutableList<T> convertIdentifiers(NonemptyIdentifierList identifiers, Function<String, T> elementFactory) {
		List<T> result = new ArrayList<>();
		result.add(elementFactory.apply(getText(identifiers.getFirstIdentifier())));
		for (NonemptyIdentifierList_1 node : identifiers.getMoreIdentifiers().getAll()) {
			result.add(elementFactory.apply(getText(node.getIdentifier())));
		}
		return ImmutableList.copyOf(result);
	}

	// prevents calling .getText() on non-leaf AST nodes by accident
	private static String getText(LeafPsiElement element) {
		return element.getText();
	}

}
