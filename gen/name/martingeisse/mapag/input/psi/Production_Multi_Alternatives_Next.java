package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Production_Multi_Alternatives_Next extends Production_Multi_Alternatives {

	public Production_Multi_Alternatives_Next(@NotNull ASTNode node) {
		super(node);
	}

	public Production_Multi_Alternatives getPrevious() {
		return (Production_Multi_Alternatives) InternalPsiUtil.getChild(this, 0);
	}

	public Production_Multi_1 getElement() {
		return (Production_Multi_1) InternalPsiUtil.getChild(this, 1);
	}

	public ImmutableList<Production_Multi_1> getAll() {
		ImmutableList.Builder<Production_Multi_1> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<Production_Multi_1> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<Production_Multi_1> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}