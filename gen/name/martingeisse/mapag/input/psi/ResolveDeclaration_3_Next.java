package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class ResolveDeclaration_3_Next extends ResolveDeclaration_3 {

    public ResolveDeclaration_3_Next(@NotNull ASTNode node) {
        super(node);
    }

        public ResolveDeclaration_3 getPrevious() {
            return (ResolveDeclaration_3)(getChildren()[0]);
        }
        public ResolveDeclaration_2 getElement() {
            return (ResolveDeclaration_2)(getChildren()[1]);
        }
    

            
    

            
    
        public ImmutableList<ResolveDeclaration_2> getAll() {
            ImmutableList.Builder<ResolveDeclaration_2> builder = ImmutableList.builder();
            addAllTo(builder);
            return builder.build();
        }

        public void addAllTo(List<ResolveDeclaration_2> list) {
            getPrevious().addAllTo(list);
            list.add(getElement());
        }

        public void addAllTo(ImmutableList.Builder<ResolveDeclaration_2> builder) {
            getPrevious().addAllTo(builder);
            builder.add(getElement());
        }

    
}
