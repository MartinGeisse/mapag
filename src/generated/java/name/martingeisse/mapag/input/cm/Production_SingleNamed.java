package name.martingeisse.mapag.input.cm;

public interface Production_SingleNamed extends Production {

        	CmToken getNonterminalName();
        	CmToken getAlternativeName();
        	RightHandSide getRightHandSide();
    
}
