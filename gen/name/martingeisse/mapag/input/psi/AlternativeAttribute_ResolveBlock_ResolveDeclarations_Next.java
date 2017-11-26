package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class AlternativeAttribute_ResolveBlock_ResolveDeclarations_Next extends AlternativeAttribute_ResolveBlock_ResolveDeclarations {

	public AlternativeAttribute_ResolveBlock_ResolveDeclarations_Next(@NotNull ASTNode node) {
		super(node);
	}

	public AlternativeAttribute_ResolveBlock_ResolveDeclarations getPrevious() {
		return (AlternativeAttribute_ResolveBlock_ResolveDeclarations) InternalPsiUtil.getChild(this, 0);
	}

	public ResolveDeclaration getElement() {
		return (ResolveDeclaration) InternalPsiUtil.getChild(this, 1);
	}

	public ImmutableList<ResolveDeclaration> getAll() {
		ImmutableList.Builder<ResolveDeclaration> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<ResolveDeclaration> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<ResolveDeclaration> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
