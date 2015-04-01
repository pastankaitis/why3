package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.modeling.annotations.Label;

@Label( standard = "linkkind" )

public enum TriggerKind
{
    @Label( standard = "Include Always" )
    ALWAYS, 
    
    @Label( standard = "Custom Trigger" )
    CUSTOM    
}