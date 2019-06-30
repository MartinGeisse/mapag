package name.martingeisse.mapag.ide;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.grammar.extended.validation.ErrorLocation;
import name.martingeisse.mapag.input.CmToGrammarConverter;
import name.martingeisse.mapag.input.GrammarToCmMap;
import name.martingeisse.mapag.input.cm.*;
import name.martingeisse.mapag.input.cm.impl.GrammarImpl;
import name.martingeisse.mapag.input.cm.impl.InternalPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagAnnotator implements Annotator {

	/**
	 * This method gets called on ALL PsiElements, post-order.
	 */
	@Override
	public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
		if (psiElement instanceof GrammarImpl) {
			GrammarImpl psiGrammar = (GrammarImpl) psiElement;
			CmToGrammarConverter converter = new CmToGrammarConverter(false);
			name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar = converter.convert(psiGrammar);
			name.martingeisse.mapag.grammar.extended.validation.GrammarValidator extendedValidator =
				new name.martingeisse.mapag.grammar.extended.validation.GrammarValidator(extendedGrammar);
			extendedValidator.validate((location, message) -> reportError(location, message, annotationHolder, converter.getBackMap()));
		}
	}

	private void reportError(ErrorLocation location, String message, AnnotationHolder annotationHolder, GrammarToCmMap backMap) {
		CmNode cmNode = determineCmNode(location, backMap);
		if (cmNode != null) {
			annotationHolder.createErrorAnnotation(InternalPsiUtil.getPsiFromCm(cmNode).getNode(), message);
		}
	}

	private CmNode determineCmNode(ErrorLocation location, GrammarToCmMap backMap) {
		if (location instanceof ErrorLocation.TerminalDeclaration) {
			return backMap.terminalDeclarations.get(((ErrorLocation.TerminalDeclaration) location).getTerminalDeclaration());
		} else if (location instanceof ErrorLocation.PrecedenceTableEntry) {
			ErrorLocation.PrecedenceTableEntry typedLocation = (ErrorLocation.PrecedenceTableEntry) location;
			PrecedenceDeclaration cmPrecedenceDeclaration = backMap.precedenceTableEntries.get(typedLocation.getEntry());
			if (cmPrecedenceDeclaration instanceof PrecedenceDeclaration_Normal) {
				return ((PrecedenceDeclaration_Normal) cmPrecedenceDeclaration).getTerminals().getAll().get(typedLocation.getSymbolIndex());
			} else {
				return cmPrecedenceDeclaration;
			}
		} else if (location instanceof ErrorLocation.StartSymbol) {
			return backMap.startSymbol;
		} else if (location instanceof ErrorLocation.ProductionLeftHandSide) {
			return backMap.productions.get(((ErrorLocation.ProductionLeftHandSide) location).getProduction());
		} else if (location instanceof ErrorLocation.PrecedenceDefiningTerminal) {
			CmList<AlternativeAttribute> cmAttributes = backMap.rightHandSides.get(((ErrorLocation.PrecedenceDefiningTerminal) location).getAlternative()).getAttributes();
			for (AlternativeAttribute attribute : cmAttributes.getAll()) {
				if (attribute instanceof AlternativeAttribute_Precedence) {
					return ((AlternativeAttribute_Precedence) attribute).getPrecedenceDefiningTerminal();
				}
			}
			return null;
		} else if (location instanceof ErrorLocation.SymbolInResolveDeclaration) {
			ErrorLocation.SymbolInResolveDeclaration typedLocation = (ErrorLocation.SymbolInResolveDeclaration) location;
			ResolveDeclaration cmResolveDeclaration = backMap.resolveDeclarations.get(((ErrorLocation.SymbolInResolveDeclaration) location).getResolveDeclaration());
			return cmResolveDeclaration.getSymbols().getAll().get(typedLocation.getSymbolIndex());
		} else if (location instanceof ErrorLocation.Expression) {
			Expression expression = backMap.expressions.get(((ErrorLocation.Expression) location).getExpression());
			if (expression instanceof Expression_Named) {
				return ((Expression_Named) expression).getExpression();
			} else {
				return expression;
			}
		} else {
			return null;
		}
	}

}
