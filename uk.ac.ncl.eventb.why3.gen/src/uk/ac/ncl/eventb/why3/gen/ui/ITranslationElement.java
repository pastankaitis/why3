package uk.ac.ncl.eventb.why3.gen.ui;

import java.util.List;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.Transient;
import org.eclipse.sapphire.TransientProperty;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.LongString;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

public interface ITranslationElement extends ICommented {
	ElementType TYPE = new ElementType(ITranslationElement.class);
	
	@Type( base = Boolean.class )    
	ValueProperty PROP_CHECKED = new ValueProperty(TYPE, "Checked");
	Value<Boolean> isChecked();
	void setChecked( String value );
	void setChecked( Boolean value ); 
	
	@XmlBinding(path = "body")
	@LongString
	ValueProperty PROP_BODY = new ValueProperty(TYPE, "Body");
	Value<String> getBody();
	void setBody(String body);
	
    // target translation view (GUI mode)
    @Type( base = String.class )
    @DefaultValue(text = "0")
    TransientProperty PROP_GUIMODE = new TransientProperty( TYPE, "GUIMode" );
    Transient<String> getGUIMode();
    void setGUIMode(String value);  
    
    // triggers
	@XmlBinding(path = "triggerkind")
	@Type( base = TriggerKind.class )
    @DefaultValue( text = "always" )
	@Required
	ValueProperty PROP_TRIGGERKIND = new ValueProperty(TYPE, "TriggerKind");
	Value<TriggerKind> getTriggerKind();
	void setTriggerKind(TriggerKind value);	    

	@Type( base = Boolean.class )    
	ValueProperty PROP_TRIGGERGOALONLY = new ValueProperty(TYPE, "TriggerGoalOnly");
	Value<Boolean> isTriggerGoalOnly();
	void setTriggerGoalOnly( String value );
	void setTriggerGoalOnly( Boolean value ); 
    
    @Type( base = Trigger.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "trigger", type = Trigger.class ) } )
    ListProperty PROP_TRIGGERS = new ListProperty( TYPE, "Triggers" );
    ElementList<Trigger> getTriggers();  	 
    
    @Type( base = List.class )
    TransientProperty PROP_CACHEDTRIGGERS = new TransientProperty( TYPE, "CachedTriggers" );
    Transient<List<Long>> getCachedTriggers();
    void setCachedTriggers(List<Long> value);  	    
    
    // dependencies
    @Type( base = ConditionDependency.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "conditiondependency", type = ConditionDependency.class ) } )
    ListProperty PROP_CONDITION_DEPENDENCIES = new ListProperty( TYPE, "ConditionDependencies" );
    ElementList<ConditionDependency> getConditionDependencies();
    
    @Type( base = RuleDependency.class )
    @XmlListBinding( mappings = { @XmlListBinding.Mapping( element = "ruledependency", type = RuleDependency.class ) } )
    ListProperty PROP_RULE_DEPENDENCIES = new ListProperty( TYPE, "RuleDependencies" );
    ElementList<RuleDependency> getRuleDependencies();      
}
