package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RightHandSide_Attributes_Next extends RightHandSide_Attributes {

	public RightHandSide_Attributes_Next(@NotNull ASTNode node) {
		super(node);
	}

	public RightHandSide_Attributes getPrevious() {
		return (RightHandSide_Attributes) InternalPsiUtil.getChild(this, 0);
	}

	public RightHandSideAttribute getElement() {
		return (RightHandSideAttribute) InternalPsiUtil.getChild(this, 1);
	}

	public ImmutableList<RightHandSideAttribute> getAll() {
		ImmutableList.Builder<RightHandSideAttribute> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<RightHandSideAttribute> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<RightHandSideAttribute> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
