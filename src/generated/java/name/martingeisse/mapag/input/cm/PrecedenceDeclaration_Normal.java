package name.martingeisse.mapag.input.cm;

public interface PrecedenceDeclaration_Normal extends PrecedenceDeclaration {

        	PrecedenceDeclaration_Normal_Associativity getAssociativity();
        	CmList<PrecedenceDeclarationSymbol> getTerminals();
    
}
