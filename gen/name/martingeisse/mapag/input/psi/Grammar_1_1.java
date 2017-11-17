package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class Grammar_1_1 extends ASTWrapperPsiElement {

    public Grammar_1_1(@NotNull ASTNode node) {
        super(node);
    }

    

            
    

            
    
        public abstract ImmutableList<PrecedenceDeclaration> getAll();

        public abstract void addAllTo(List<PrecedenceDeclaration> list);

        public abstract void addAllTo(ImmutableList.Builder<PrecedenceDeclaration> builder);

    
}
