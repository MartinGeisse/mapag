package name.martingeisse.mapag.ide;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.grammar.extended.validation.ErrorLocation;
import name.martingeisse.mapag.input.GrammarToPsiMap;
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import name.martingeisse.mapag.input.psi.*;
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
		if (psiElement instanceof Grammar) {
			Grammar psiGrammar = (Grammar)psiElement;
			PsiToGrammarConverter converter = new PsiToGrammarConverter(false);
			name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar = converter.convert(psiGrammar);
			name.martingeisse.mapag.grammar.extended.validation.GrammarValidator extendedValidator =
				new name.martingeisse.mapag.grammar.extended.validation.GrammarValidator(extendedGrammar);
			extendedValidator.validate((location, message) -> reportError(location, message, annotationHolder, converter.getBackMap()));
		}
	}

	private void reportError(ErrorLocation location, String message, AnnotationHolder annotationHolder, GrammarToPsiMap backMap) {
		PsiElement psiElement = determinePsiElement(location, backMap);
		if (psiElement != null) {
			annotationHolder.createErrorAnnotation(psiElement.getNode(), message);
		}
	}

	private PsiElement determinePsiElement(ErrorLocation location, GrammarToPsiMap backMap) {
		if (location instanceof ErrorLocation.TerminalDeclaration) {
			return backMap.terminalDeclarations.get(((ErrorLocation.TerminalDeclaration) location).getTerminalDeclaration());
		} else if (location instanceof ErrorLocation.PrecedenceTableEntry) {
			ErrorLocation.PrecedenceTableEntry typedLocation = (ErrorLocation.PrecedenceTableEntry)location;
			PrecedenceDeclaration psiPrecedenceDeclaration = backMap.precedenceTableEntries.get(typedLocation.getEntry());
			if (psiPrecedenceDeclaration instanceof PrecedenceDeclaration_Normal) {
				return ((PrecedenceDeclaration_Normal) psiPrecedenceDeclaration).getTerminals().getAll().get(typedLocation.getSymbolIndex());
			} else {
				return psiPrecedenceDeclaration;
			}
		} else if (location instanceof ErrorLocation.StartSymbol) {
			return backMap.startSymbol;
		} else if (location instanceof ErrorLocation.ProductionLeftHandSide) {
			return backMap.productions.get(((ErrorLocation.ProductionLeftHandSide) location).getProduction());
		} else if (location instanceof ErrorLocation.PrecedenceDefiningTerminal) {
			ListNode<AlternativeAttribute> psiAttributes = backMap.rightHandSides.get(((ErrorLocation.PrecedenceDefiningTerminal) location).getAlternative()).getAttributes();
			for (AlternativeAttribute attribute : psiAttributes.getAll()) {
				if (attribute instanceof AlternativeAttribute_Precedence) {
					return ((AlternativeAttribute_Precedence) attribute).getPrecedenceDefiningTerminal();
				}
			}
			return null;
		} else if (location instanceof ErrorLocation.SymbolInResolveDeclaration) {
			ErrorLocation.SymbolInResolveDeclaration typedLocation = (ErrorLocation.SymbolInResolveDeclaration)location;
			ResolveDeclaration psiResolveDeclaration = backMap.resolveDeclarations.get(((ErrorLocation.SymbolInResolveDeclaration) location).getResolveDeclaration());
			return psiResolveDeclaration.getSymbols().getAll().get(typedLocation.getSymbolIndex());
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
