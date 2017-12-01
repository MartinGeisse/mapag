package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.Alternative;
import name.martingeisse.mapag.grammar.extended.PrecedenceTable;
import name.martingeisse.mapag.grammar.extended.Production;
import name.martingeisse.mapag.grammar.extended.ResolveDeclaration;

/**
 *
 */
public abstract class ErrorLocation {

	public static final class TerminalDeclaration extends ErrorLocation {

		private final name.martingeisse.mapag.grammar.extended.TerminalDeclaration terminalDeclaration;

		public TerminalDeclaration(name.martingeisse.mapag.grammar.extended.TerminalDeclaration terminalDeclaration) {
			this.terminalDeclaration = terminalDeclaration;
		}

		public name.martingeisse.mapag.grammar.extended.TerminalDeclaration getTerminalDeclaration() {
			return terminalDeclaration;
		}

	}

	public static final class PrecedenceTableEntry extends ErrorLocation {

		private final PrecedenceTable.Entry entry;

		public PrecedenceTableEntry(PrecedenceTable.Entry entry) {
			this.entry = entry;
		}

		public PrecedenceTable.Entry getEntry() {
			return entry;
		}

	}

	public static final class StartSymbol extends ErrorLocation {
	}

	public static abstract class ProductionRelated extends ErrorLocation {

		private final Production production;

		public ProductionRelated(Production production) {
			this.production = production;
		}

		public Production getProduction() {
			return production;
		}
	}

	public static final class ProductionLeftHandSide extends ProductionRelated {

		public ProductionLeftHandSide(Production production) {
			super(production);
		}

	}

	public static abstract class AlternativeRelated extends ProductionRelated {

		private final Alternative alternative;

		public AlternativeRelated(Production production, Alternative alternative) {
			super(production);
			this.alternative = alternative;
		}

		public Alternative getAlternative() {
			return alternative;
		}

	}

	public static final class PrecedenceDefiningTerminal extends AlternativeRelated {

		public PrecedenceDefiningTerminal(Production production, Alternative alternative) {
			super(production, alternative);
		}

	}

	public static final class SymbolInResolveDeclaration extends AlternativeRelated {

		private final ResolveDeclaration resolveDeclaration;
		private final int symbolIndex;

		public SymbolInResolveDeclaration(Production production, Alternative alternative, ResolveDeclaration resolveDeclaration, int symbolIndex) {
			super(production, alternative);
			this.resolveDeclaration = resolveDeclaration;
			this.symbolIndex = symbolIndex;
		}

		public ResolveDeclaration getResolveDeclaration() {
			return resolveDeclaration;
		}

		public int getSymbolIndex() {
			return symbolIndex;
		}

	}

	public static final class Expression extends AlternativeRelated {

		private final name.martingeisse.mapag.grammar.extended.expression.Expression expression;

		public Expression(Production production, Alternative alternative, name.martingeisse.mapag.grammar.extended.expression.Expression expression) {
			super(production, alternative);
			this.expression = expression;
		}

		public name.martingeisse.mapag.grammar.extended.expression.Expression getExpression() {
			return expression;
		}

	}

}
