package name.martingeisse.mapag.ide;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import name.martingeisse.mapag.input.psi.Grammar;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MapagSourceFile extends PsiFileBase {

	public MapagSourceFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, MapagSpecificationLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return MapagFileType.INSTANCE;
	}

	public Grammar getGrammar() {
		for (PsiElement child : getChildren()) {
			if (child instanceof Grammar) {
				return (Grammar)child;
			}
		}
		return null;
	}

}
