package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.modeling.annotations.Label;

@Label( standard = "conditionkind" )

public enum ConditionKind
{
    @Label( standard = "Axiom" )
    AXIOM, 
    
    @Label( standard = "Lemma" )
    LEMMA,    
    
    @Label( standard = "Test" )
    TEST      
}