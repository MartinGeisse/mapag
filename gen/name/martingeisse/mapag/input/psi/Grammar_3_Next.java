package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class Grammar_3_Next extends Grammar_3 {

    public Grammar_3_Next(@NotNull ASTNode node) {
        super(node);
    }

        public Grammar_3 getPrevious() {
            return (Grammar_3)(getChildren()[0]);
        }
        public Production getElement() {
            return (Production)(getChildren()[1]);
        }
    

            
    

            
    
        public ImmutableList<Production> getAll() {
            ImmutableList.Builder<Production> builder = ImmutableList.builder();
            addAllTo(builder);
            return builder.build();
        }

        public void addAllTo(List<Production> list) {
            getPrevious().addAllTo(list);
            list.add(getElement());
        }

        public void addAllTo(ImmutableList.Builder<Production> builder) {
            getPrevious().addAllTo(builder);
            builder.add(getElement());
        }

    
}
