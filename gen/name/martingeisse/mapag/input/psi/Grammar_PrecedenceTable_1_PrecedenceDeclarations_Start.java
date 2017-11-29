package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Grammar_PrecedenceTable_1_PrecedenceDeclarations_Start extends Grammar_PrecedenceTable_1_PrecedenceDeclarations {

	public Grammar_PrecedenceTable_1_PrecedenceDeclarations_Start(@NotNull ASTNode node) {
		super(node);
	}

	public ImmutableList<PrecedenceDeclaration> getAll() {
		return ImmutableList.of();
	}

	public void addAllTo(List<PrecedenceDeclaration> list) {
	}

	public void addAllTo(ImmutableList.Builder<PrecedenceDeclaration> builder) {
	}

}
