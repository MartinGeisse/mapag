package name.martingeisse.mapag.input.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public final class Grammar_2_Present extends Grammar_2 {

    public Grammar_2_Present(@NotNull ASTNode node) {
        super(node);
    }

        public Grammar_1 getIt() {
            return (Grammar_1)(getChildren()[0]);
        }
    

            
    
        
    

            
    
}
