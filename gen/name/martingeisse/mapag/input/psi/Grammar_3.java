package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class Grammar_3 extends ASTWrapperPsiElement {

    public Grammar_3(@NotNull ASTNode node) {
        super(node);
    }

    

            
    

            
    
        public abstract ImmutableList<Production> getAll();

        public abstract void addAllTo(List<Production> list);

        public abstract void addAllTo(ImmutableList.Builder<Production> builder);

    
}
