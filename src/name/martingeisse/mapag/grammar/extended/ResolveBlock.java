package name.martingeisse.mapag.grammar.extended;

import com.google.common.collect.ImmutableList;
import name.martingeisse.mapag.util.ParameterUtil;

/**
 *
 */
public final class ResolveBlock {

	private final ImmutableList<ResolveDeclaration> resolveDeclarations;

	public ResolveBlock(ImmutableList<ResolveDeclaration> resolveDeclarations) {
		this.resolveDeclarations = ParameterUtil.ensureNotNull(resolveDeclarations, "resolveDeclarations");
	}

	public ImmutableList<ResolveDeclaration> getResolveDeclarations() {
		return resolveDeclarations;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		for (ResolveDeclaration resolveDeclaration : resolveDeclarations) {
			builder.append(resolveDeclaration);
		}
		builder.append('}');
		return builder.toString();
	}
}
