package name.martingeisse.mapag.input;

import name.martingeisse.mapag.grammar.extended.*;
import name.martingeisse.mapag.grammar.extended.expression.Expression;
import name.martingeisse.mapag.ide.MapagAnnotator;
import name.martingeisse.mapag.input.cm.CmNode;
import name.martingeisse.mapag.input.cm.CmToken;
import name.martingeisse.mapag.input.cm.PrecedenceDeclaration;
import name.martingeisse.mapag.input.cm.RightHandSide;

import java.util.HashMap;
import java.util.Map;

/**
 * This object is generated by the {@link CmToGrammarConverter} along with the converted grammar to allow the caller
 * to find the CM nodes for generated grammar elements. This is used by the {@link MapagAnnotator} to place error
 * markers.
 */
public final class GrammarToCmMap {
	public final Map<TerminalDeclaration, name.martingeisse.mapag.input.cm.TerminalDeclaration> terminalDeclarations = new HashMap<>();
	public final Map<PrecedenceTable.Entry, PrecedenceDeclaration> precedenceTableEntries = new HashMap<>();
	public CmToken startSymbol;
	public final Map<Production, name.martingeisse.mapag.input.cm.Production> productions = new HashMap<>();
	public final Map<Alternative, CmNode> alternatives = new HashMap<>();
	public final Map<Alternative, RightHandSide> rightHandSides = new HashMap<>();
	public final Map<ResolveDeclaration, name.martingeisse.mapag.input.cm.ResolveDeclaration> resolveDeclarations = new HashMap<>();
	public final Map<Expression, name.martingeisse.mapag.input.cm.Expression> expressions = new HashMap<>();
}