package name.martingeisse.mapag.ide;

import com.intellij.ide.structureView.*;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import name.martingeisse.mapag.input.cm.impl.GrammarImpl;
import name.martingeisse.mapag.input.cm.impl.Grammar_PrecedenceTableImpl;
import name.martingeisse.mapag.input.cm.impl.Grammar_TerminalDeclarationsImpl;
import name.martingeisse.mapag.input.cm.impl.ProductionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MapagStructureViewFactory implements PsiStructureViewFactory {

	private static boolean shouldInclude(PsiElement element) {
		if (element instanceof Grammar_TerminalDeclarationsImpl) {
			return true;
		}
		if (element instanceof Grammar_PrecedenceTableImpl) {
			return true;
		}
		if (element instanceof ProductionImpl) {
			return true;
		}
		return false;
	}

	@Nullable
	@Override
	public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
		if (!(psiFile instanceof MapagSourceFile)) {
			return null;
		}
		GrammarImpl grammar = ((MapagSourceFile) psiFile).getGrammar();
		if (grammar == null) {
			return null;
		}
		return new TreeBasedStructureViewBuilder() {
			@NotNull
			@Override
			public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
				return new TextEditorBasedStructureViewModel(editor, psiFile) {

					@NotNull
					@Override
					public StructureViewTreeElement getRoot() {
						return new MyStructureViewElement(grammar);
					}

					@NotNull
					@Override
					protected Class[] getSuitableClasses() {
						throw new UnsupportedOperationException();
					}

					@Override
					protected boolean isSuitable(PsiElement element) {
						return shouldInclude(element);
					}
				};
			}
		};
	}

	public static class MyStructureViewElement implements StructureViewTreeElement, ItemPresentation {

		private final PsiElement element;

		public MyStructureViewElement(PsiElement element) {
			this.element = element;
		}

		@Override
		public Object getValue() {
			return element;
		}

		@NotNull
		@Override
		public ItemPresentation getPresentation() {
			return this;
		}

		@Nullable
		@Override
		public String getPresentableText() {
			if (element instanceof Grammar_TerminalDeclarationsImpl) {
				return "%terminals {...}";
			}
			if (element instanceof Grammar_PrecedenceTableImpl) {
				return "%precedence {...}";
			}
			if (element instanceof ProductionImpl) {
				String name = ((ProductionImpl) element).getName();
				return (name == null ? "(production)" : name);
			}
			return element.toString();
		}

		@Nullable
		@Override
		public String getLocationString() {
			return null;
		}

		@Nullable
		@Override
		public Icon getIcon(boolean b) {
			return null;
		}

		@NotNull
		@Override
		public TreeElement[] getChildren() {
			List<TreeElement> children = new ArrayList<>();
			checkIncludeChildrenOf(element, children);
			return children.toArray(new TreeElement[children.size()]);
		}

		private static void checkIncludeChildrenOf(PsiElement psiParent, List<TreeElement> destination) {
			for (PsiElement psiChild : psiParent.getChildren()) {
				if (shouldInclude(psiChild)) {
					destination.add(new MyStructureViewElement(psiChild));
				} else {
					checkIncludeChildrenOf(psiChild, destination);
				}
			}
		}

		@Override
		public void navigate(boolean requestFocus) {
			((Navigatable) element).navigate(requestFocus);
		}

		@Override
		public boolean canNavigate() {
			return element instanceof Navigatable && ((Navigatable) element).canNavigate();
		}

		@Override
		public boolean canNavigateToSource() {
			return canNavigate();
		}

	}
}
