package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Grammar_Productions_Next extends Grammar_Productions {

	public Grammar_Productions_Next(@NotNull ASTNode node) {
		super(node);
	}

	public Grammar_Productions getPrevious() {
		return (Grammar_Productions) (getChildren()[0]);
	}

	public Production getElement() {
		return (Production) (getChildren()[1]);
	}

	public ImmutableList<Production> getAll() {
		ImmutableList.Builder<Production> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<Production> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<Production> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
