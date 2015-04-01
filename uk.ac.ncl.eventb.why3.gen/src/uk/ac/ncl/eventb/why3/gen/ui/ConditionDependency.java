package uk.ac.ncl.eventb.why3.gen.ui;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementReference;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.Unique;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface ConditionDependency extends Element {
	ElementType TYPE = new ElementType(ConditionDependency.class);
	
    @Reference( target = TranslationCondition.class )
    @ElementReference( list = "../../Conditions", key = "Id" )
    @Required
    @Unique
    @XmlBinding( path = "conditiondep")
    ValueProperty PROP_CONDITION_DEPENDENCY = new ValueProperty( TYPE, "ConditionDependency" );
    ReferenceValue<String,TranslationCondition> getConditionDependency();
    void setConditionDependency( String id );
}
