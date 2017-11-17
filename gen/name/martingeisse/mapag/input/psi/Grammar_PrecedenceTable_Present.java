package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Grammar_PrecedenceTable_Present extends Grammar_PrecedenceTable {

	public Grammar_PrecedenceTable_Present(@NotNull ASTNode node) {
		super(node);
	}

	public Grammar_1 getIt() {
		return (Grammar_1) (getChildren()[0]);
	}

}
