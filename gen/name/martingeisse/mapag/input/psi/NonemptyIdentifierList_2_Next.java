package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class NonemptyIdentifierList_2_Next extends NonemptyIdentifierList_2 {

    public NonemptyIdentifierList_2_Next(@NotNull ASTNode node) {
        super(node);
    }

        public NonemptyIdentifierList_2 getPrevious() {
            return (NonemptyIdentifierList_2)(getChildren()[0]);
        }
        public NonemptyIdentifierList_1 getElement() {
            return (NonemptyIdentifierList_1)(getChildren()[1]);
        }
    

            
    

            
    
        public ImmutableList<NonemptyIdentifierList_1> getAll() {
            ImmutableList.Builder<NonemptyIdentifierList_1> builder = ImmutableList.builder();
            addAllTo(builder);
            return builder.build();
        }

        public void addAllTo(List<NonemptyIdentifierList_1> list) {
            getPrevious().addAllTo(list);
            list.add(getElement());
        }

        public void addAllTo(ImmutableList.Builder<NonemptyIdentifierList_1> builder) {
            getPrevious().addAllTo(builder);
            builder.add(getElement());
        }

    
}
