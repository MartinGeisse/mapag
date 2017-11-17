package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class ResolveDeclaration_3 extends ASTWrapperPsiElement {

    public ResolveDeclaration_3(@NotNull ASTNode node) {
        super(node);
    }

    

            
    

            
    
        public abstract ImmutableList<ResolveDeclaration_2> getAll();

        public abstract void addAllTo(List<ResolveDeclaration_2> list);

        public abstract void addAllTo(ImmutableList.Builder<ResolveDeclaration_2> builder);

    
}
