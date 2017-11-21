package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Grammar_Productions_Start extends Grammar_Productions {

	public Grammar_Productions_Start(@NotNull ASTNode node) {
		super(node);
	}

	public Production getElement() {
		return (Production) InternalPsiUtil.getChild(this, 0);
	}

	public ImmutableList<Production> getAll() {
		return ImmutableList.of(getElement());
	}

	public void addAllTo(List<Production> list) {
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<Production> builder) {
		builder.add(getElement());
	}

}
