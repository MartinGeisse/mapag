package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class NonemptyIdentifierList_2 extends ASTWrapperPsiElement {

    public NonemptyIdentifierList_2(@NotNull ASTNode node) {
        super(node);
    }

    

            
    

            
    
        public abstract ImmutableList<NonemptyIdentifierList_1> getAll();

        public abstract void addAllTo(List<NonemptyIdentifierList_1> list);

        public abstract void addAllTo(ImmutableList.Builder<NonemptyIdentifierList_1> builder);

    
}
