package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class RightHandSide_WithExplicitResolver_1 extends ASTWrapperPsiElement {

    public RightHandSide_WithExplicitResolver_1(@NotNull ASTNode node) {
        super(node);
    }

    

            
    

            
    
        public abstract ImmutableList<ResolveDeclaration> getAll();

        public abstract void addAllTo(List<ResolveDeclaration> list);

        public abstract void addAllTo(ImmutableList.Builder<ResolveDeclaration> builder);

    
}
