package name.martingeisse.mapag.input.cm;

public interface Production_Multi extends Production {

        	CmToken getNonterminalName();
        	CmList<Production_Multi_Alternatives> getAlternatives();
    
}
