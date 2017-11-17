package name.martingeisse.mapag.input.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ResolveDeclaration_AdditionalSymbols_Next extends ResolveDeclaration_AdditionalSymbols {

	public ResolveDeclaration_AdditionalSymbols_Next(@NotNull ASTNode node) {
		super(node);
	}

	public ResolveDeclaration_AdditionalSymbols getPrevious() {
		return (ResolveDeclaration_AdditionalSymbols) (getChildren()[0]);
	}

	public ResolveDeclaration_1 getElement() {
		return (ResolveDeclaration_1) (getChildren()[1]);
	}

	public ImmutableList<ResolveDeclaration_1> getAll() {
		ImmutableList.Builder<ResolveDeclaration_1> builder = ImmutableList.builder();
		addAllTo(builder);
		return builder.build();
	}

	public void addAllTo(List<ResolveDeclaration_1> list) {
		getPrevious().addAllTo(list);
		list.add(getElement());
	}

	public void addAllTo(ImmutableList.Builder<ResolveDeclaration_1> builder) {
		getPrevious().addAllTo(builder);
		builder.add(getElement());
	}

}
