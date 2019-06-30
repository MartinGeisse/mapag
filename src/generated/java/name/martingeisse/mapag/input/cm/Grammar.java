package name.martingeisse.mapag.input.cm;

public interface Grammar extends CmNode {

        	Grammar_TerminalDeclarations getTerminalDeclarations();
        	CmOptional<Grammar_PrecedenceTable> getPrecedenceTable();
        	CmToken getStartSymbolName();
        	CmList<Production> getProductions();
    
}
