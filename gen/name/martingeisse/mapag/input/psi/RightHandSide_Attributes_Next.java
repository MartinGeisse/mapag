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

	public AlternativeAttribute getElement() {
		return (AlternativeAttribute) InternalPsiUtil.getChild(this, 1);
	}

	public ImmutableList<AlternativeAttribute> getAll() {
		ImmutableList.Builder<AlternativeAttribute> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<AlternativeAttribute> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<AlternativeAttribute> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
