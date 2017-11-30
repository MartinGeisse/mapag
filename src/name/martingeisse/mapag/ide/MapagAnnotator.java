package name.martingeisse.mapag.ide;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.grammar.canonical.info.GrammarInfo;
import name.martingeisse.mapag.grammar.canonicalization.GrammarCanonicalizer;
import name.martingeisse.mapag.input.PsiToGrammarConverter;
import name.martingeisse.mapag.input.psi.Grammar;
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

				// Convert the PSI to an extended grammar. This step only throws exceptions in case of an invalid PSI
				// (which should not happen), or syntax errors (in which case this annotator won't get a chance to
				// show its own errors). TODO there seems to be some wrong handling of syntax errors, and when that is
				// fixed, things may change. Then we might have to chance the PsiToGrammarConverter to support
				// proper error reporting.
				name.martingeisse.mapag.grammar.extended.Grammar extendedGrammar =
					new PsiToGrammarConverter().convert(psiGrammar);

				// use a grammar validator to check for errors TODO proper error reporting
				name.martingeisse.mapag.grammar.extended.validation.GrammarValidator extendedValidator =
					new name.martingeisse.mapag.grammar.extended.validation.GrammarValidator(extendedGrammar);
				extendedValidator.validate();

				// no errors, so lets canonicalize the grammar and validate that again (should not fail since we
				// already validated the input)
				name.martingeisse.mapag.grammar.canonical.Grammar canonicalGrammar =
					new GrammarCanonicalizer(extendedGrammar).run().getResult();
				name.martingeisse.mapag.grammar.canonical.validation.GrammarValidator canonicalValidator =
					new name.martingeisse.mapag.grammar.canonical.validation.GrammarValidator(canonicalGrammar);
				canonicalValidator.validate();

				// finally, we'll build a state machine for the grammar. This may detect some error conditions such
				// as shift/reduce and reduce/reduce conflicts that cannot be detected earlier
				GrammarInfo grammarInfo = new GrammarInfo(canonicalGrammar);
				new StateMachineBuilder(grammarInfo).build();

			} catch (Exception e) {
				// TODO multiple messages; attach to correct node!
				annotationHolder.createErrorAnnotation(psiElement.getNode(), e.toString());
			}
		}
	}

}
