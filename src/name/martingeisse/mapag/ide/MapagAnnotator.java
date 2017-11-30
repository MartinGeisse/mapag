package name.martingeisse.mapag.ide;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import name.martingeisse.mapag.input.psi.Grammar;
import name.martingeisse.mapag.sm.StateMachine;
import name.martingeisse.mapag.sm.StateMachineBuilder;
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
			try {
				Grammar psiGrammar = (Grammar)psiElement;
				name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar =
					new PsiToGrammarConverter().convert(psiGrammar);
				name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar =
					new GrammarCanonicalizer(extendedGrammar).run().getResult();
				GrammarInfo grammarInfo = new GrammarInfo(canonicalGrammar);
				new StateMachineBuilder(grammarInfo).build();
			} catch (Exception e) {
				// TODO multiple messages; attach to correct node!
				annotationHolder.createErrorAnnotation(psiElement.getNode(), e.toString());
			}
		}
	}

}
