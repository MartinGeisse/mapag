package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class RightHandSide_WithExplicitResolver_1_Next extends RightHandSide_WithExplicitResolver_1 {

    public RightHandSide_WithExplicitResolver_1_Next(@NotNull ASTNode node) {
        super(node);
    }

        public RightHandSide_WithExplicitResolver_1 getPrevious() {
            return (RightHandSide_WithExplicitResolver_1)(getChildren()[0]);
        }
        public ResolveDeclaration getElement() {
            return (ResolveDeclaration)(getChildren()[1]);
        }
    

            
    

            
    
        public ImmutableList<ResolveDeclaration> getAll() {
            ImmutableList.Builder<ResolveDeclaration> builder = ImmutableList.builder();
            addAllTo(builder);
            return builder.build();
        }

        public void addAllTo(List<ResolveDeclaration> list) {
            getPrevious().addAllTo(list);
            list.add(getElement());
        }

        public void addAllTo(ImmutableList.Builder<ResolveDeclaration> builder) {
            getPrevious().addAllTo(builder);
            builder.add(getElement());
        }

    
}
