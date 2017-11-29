package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Grammar_PrecedenceTable_PrecedenceDeclarations_Next extends Grammar_PrecedenceTable_PrecedenceDeclarations {

	public Grammar_PrecedenceTable_PrecedenceDeclarations_Next(@NotNull ASTNode node) {
		super(node);
	}

	public Grammar_PrecedenceTable_PrecedenceDeclarations getPrevious() {
		return (Grammar_PrecedenceTable_PrecedenceDeclarations) InternalPsiUtil.getChild(this, 0);
	}

	public PrecedenceDeclaration getElement() {
		return (PrecedenceDeclaration) InternalPsiUtil.getChild(this, 1);
	}

	public ImmutableList<PrecedenceDeclaration> getAll() {
		ImmutableList.Builder<PrecedenceDeclaration> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<PrecedenceDeclaration> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<PrecedenceDeclaration> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
